// CannonView.java
// Displays and controls the Cannon Game
package pl.pjatk.pamo.kotlincalculatorbmi.game


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.SparseIntArray
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import pl.pjatk.pamo.kotlincalculatorbmi.R
import java.util.*


class CannonView(context: Context, attrs: AttributeSet?) :
    SurfaceView(context, attrs), SurfaceHolder.Callback {
    private var cannonThread // controls the game loop
            : CannonThread? = null
    private val activity // to display Game Over dialog in GUI thread
            : Activity
    private var dialogIsDisplayed = false

    // game objects
    private var cannon: Cannon? = null
    private var blocker: Blocker? = null
    private var targets: ArrayList<Target>? = null

    // get width of the game screen
    // dimension variables
    var screenWidth = 0
        private set

    // get height of the game screen
    var screenHeight = 0
        private set

    // variables for the game loop and tracking statistics
    private var gameOver // is the game over?
            = false
    private var timeLeft // time remaining in seconds
            = 0.0
    private var shotsFired // shots the user has fired
            = 0
    private var totalElapsedTime // elapsed seconds
            = 0.0
    private var soundPool // plays sound effects
            : SoundPool?
    private val soundMap // maps IDs to SoundPool
            : SparseIntArray

    // Paint variables used when drawing each item on the screen
    private val textPaint // Paint used to draw text
            : Paint
    private val backgroundPaint // Paint used to clear the drawing area
            : Paint
    var context2: Context? = null

    // called when the size of the SurfaceView changes,
    // such as when it's first added to the View hierarchy
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w // store CannonView's width
        screenHeight = h // store CannonView's height

        // configure text properties
        textPaint.setTextSize((TEXT_SIZE_PERCENT * screenHeight).toFloat())
        textPaint.isAntiAlias = true // smoothes the text
    }

    // plays a sound with the given soundId in soundMap
    fun playSound(soundId: Int) {
        soundPool!!.play(soundMap[soundId], 1f, 1f, 1, 0, 1f)
    }

    // reset all the screen elements and start a new game
    fun newGame() {
        // construct a new Cannon
        cannon = Cannon(
            this,
            (CANNON_BASE_RADIUS_PERCENT * screenHeight).toInt(),
            (CANNON_BARREL_LENGTH_PERCENT * screenWidth).toInt(),
            (CANNON_BARREL_WIDTH_PERCENT * screenHeight).toInt()
        )
        val random = Random() // for determining random velocities
        targets =
            ArrayList<Target>() // construct a new Target list

        // initialize targetX for the first Target from the left
        var targetX = (TARGET_FIRST_X_PERCENT * screenWidth).toInt()

        // calculate Y coordinate of Targets
        val targetY = ((0.5 - TARGET_LENGTH_PERCENT / 2) *
                screenHeight).toInt()

        // add TARGET_PIECES Targets to the Target list
        var n = 0
        while (n < TARGET_PIECES) {


            // determine a random velocity between min and max values
            // for Target n
            var velocity = screenHeight * (random.nextDouble() *
                    (TARGET_MAX_SPEED_PERCENT - TARGET_MIN_SPEED_PERCENT) +
                    TARGET_MIN_SPEED_PERCENT)

            // alternate Target colors between dark and light
            val color = if (n % 2 == 0) resources.getColor(
                R.color.dark,
                getContext().theme
            ) else resources.getColor(
                R.color.light,
                getContext().theme
            )
            velocity *= -1.0 // reverse the initial velocity for next Target

            // create and add a new Target to the Target list
            targets!!.add(
                Target(
                    this, color, HIT_REWARD, targetX, targetY,
                    (TARGET_WIDTH_PERCENT * screenWidth).toInt(),
                    (TARGET_LENGTH_PERCENT * screenHeight).toInt(),
                    velocity.toFloat()
                )
            )

            // increase the x coordinate to position the next Target more
            // to the right
            targetX += ((TARGET_WIDTH_PERCENT + TARGET_SPACING_PERCENT) *
                    screenWidth).toInt()
            n++
        }

        // create a new Blocker
        blocker = Blocker(
            this, Color.BLACK, MISS_PENALTY,
            (BLOCKER_X_PERCENT * screenWidth).toInt(),
            ((0.5 - BLOCKER_LENGTH_PERCENT / 2) * screenHeight).toInt(),
            (BLOCKER_WIDTH_PERCENT * screenWidth).toInt(),
            (BLOCKER_LENGTH_PERCENT * screenHeight).toInt(),
            (BLOCKER_SPEED_PERCENT * screenHeight).toFloat()
        )
        timeLeft = 20.0 // start the countdown at 10 seconds
        shotsFired = 0 // set the initial number of shots fired
        totalElapsedTime = 0.0 // set the time elapsed to zero
        if (gameOver) { // start a new game after the last game ended
            gameOver = false // the game is not over
            cannonThread = CannonThread(holder) // create thread
            cannonThread!!.start() // start the game loop thread
        }
        hideSystemBars()
    }

    // called repeatedly by the CannonThread to update game elements
    private fun updatePositions(elapsedTimeMS: Double) {
        val interval = elapsedTimeMS / 1000.0 // convert to seconds

        // update cannonball's position if it is on the screen
        if (cannon!!.cannonball != null) cannon!!.cannonball!!.update(interval)
        blocker!!.update(interval) // update the blocker's position
        for (target in targets!!) target.update(interval) // update the target's position
        timeLeft -= interval // subtract from time left

        // if the timer reached zero
        if (timeLeft <= 0) {
            timeLeft = 0.0
            gameOver = true // the game is over
            cannonThread!!.setRunning(false) // terminate thread
            showGameOverDialog(R.string.lose) // show the losing dialog
        }

        // if all pieces have been hit
        if (targets!!.isEmpty()) {
            cannonThread!!.setRunning(false) // terminate thread
            showGameOverDialog(R.string.win) // show winning dialog
            gameOver = true
        }
    }

    // aligns the barrel and fires a Cannonball if a Cannonball is not
    // already on the screen
    fun alignAndFireCannonball(event: MotionEvent) {
        // get the location of the touch in this view
        val touchPoint = Point(
            event.x.toInt(),
            event.y.toInt()
        )

        // compute the touch's distance from center of the screen
        // on the y-axis
        val centerMinusY = (screenHeight / 2 - touchPoint.y).toDouble()
        var angle = 0.0 // initialize angle to 0

        // calculate the angle the barrel makes with the horizontal
        angle = Math.atan2(touchPoint.x.toDouble(), centerMinusY)

        // point the barrel at the point where the screen was touched
        cannon!!.align(angle)

        // fire Cannonball if there is not already a Cannonball on screen
        if (cannon?.cannonball == null ||
            !cannon!!.cannonball!!.isOnScreen
        ) {
            cannon!!.fireCannonball()
            ++shotsFired
        }
    }

    // display an AlertDialog when the game ends
    private fun showGameOverDialog(messageId: Int) {
        // DialogFragment to display game stats and start new game
        val gameResult: DialogFragment = object : DialogFragment() {
            // create an AlertDialog and return it
            override fun onCreateDialog(bundle: Bundle): Dialog {
                // create dialog displaying String resource for messageId
                val builder = AlertDialog.Builder(
                    activity
                )
                builder.setTitle(resources.getString(messageId))

                // display number of shots fired and total time elapsed
                builder.setMessage(
                    resources.getString(
                        R.string.results_format, shotsFired, totalElapsedTime
                    )
                )
                builder.setPositiveButton(R.string.reset_game,
                    DialogInterface.OnClickListener { dialog, which ->

                        // called when "Reset Game" Button is pressed
                        dialogIsDisplayed = false
                        newGame() // set up and start a new game
                    }
                )
                return builder.create() // return the AlertDialog
            }
        }

        // in GUI thread, use FragmentManager to display the DialogFragment
        activity.runOnUiThread {
            showSystemBars()
            dialogIsDisplayed = true
            gameResult.isCancelable = false // modal dialog
            gameResult.show(activity.fragmentManager, "results")
        }
    }

    // draws the game to the given Canvas
    fun drawGameElements(canvas: Canvas?) {
        // clear the background
        canvas!!.drawRect(
            0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(),
            backgroundPaint
        )

        // display time remaining
        canvas.drawText(
            resources.getString(
                R.string.time_remaining_format, timeLeft
            ), 50f, 100f, textPaint
        )
        cannon!!.draw(canvas) // draw the cannon

        // draw the GameElements
        if (cannon?.cannonball != null &&
            cannon?.cannonball!!.isOnScreen
        ) cannon?.cannonball!!.draw(canvas)
        blocker!!.draw(canvas) // draw the blocker

        // draw all of the Targets
        for (target in targets!!) target.draw(canvas)
    }

    // checks if the ball collides with the Blocker or any of the Targets
    // and handles the collisions
    fun testForCollisions() {
        // remove any of the targets that the Cannonball
        // collides with
        if (cannon?.cannonball != null &&
            cannon?.cannonball!!.isOnScreen
        ) {
            var n = 0
            while (n < targets!!.size) {
                if (cannon?.cannonball!!.collidesWith(targets!![n])) {
                    targets!![n].playSound() // play Target hit sound

                    // add hit rewards time to remaining time
                    timeLeft += targets!![n].hitReward
                    cannon!!.removeCannonball() // remove Cannonball from game
                    targets!!.removeAt(n) // remove the Target that was hit
                    --n // ensures that we don't skip testing new target n
                    break
                }
                n++
            }
        } else { // remove the Cannonball if it should not beon the screen
            cannon!!.removeCannonball()
        }

        // check if ball collides with blocker
        if (cannon?.cannonball != null &&
            cannon?.cannonball!!.collidesWith(blocker!!)
        ) {
            blocker!!.playSound() // play Blocker hit sound

            // reverse ball direction
            cannon!!.cannonball!!.reverseVelocityX()

            // deduct blocker's miss penalty from remaining time
            timeLeft -= blocker!!.missPenalty.toDouble()
        }
    }

    // stops the game: called by CannonGameFragment's onPause method
    fun stopGame() {
        if (cannonThread != null) cannonThread!!.setRunning(false) // tell thread to terminate
    }

    // release resources: called by CannonGame's onDestroy method
    fun releaseResources() {
        soundPool!!.release() // release all resources used by the SoundPool
        soundPool = null
    }

    // called when surface changes size
    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int,
        width: Int, height: Int
    ) {
    }

    // called when surface is first created
    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!dialogIsDisplayed) {
            newGame() // set up and start a new game
            cannonThread = CannonThread(holder) // create thread
            cannonThread!!.setRunning(true) // start game running
            cannonThread!!.start() // start the game loop thread
        }
    }

    // called when the surface is destroyed
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // ensure that thread terminates properly
        var retry = true
        cannonThread!!.setRunning(false) // terminate cannonThread
        while (retry) {
            try {
                cannonThread!!.join() // wait for cannonThread to finish
                retry = false
            } catch (e: InterruptedException) {
                Log.e(TAG, "Thread interrupted", e)
            }
        }
    }

    // called when the user touches the screen in this activity
    override fun onTouchEvent(e: MotionEvent): Boolean {
        // get int representing the type of action which caused this event
        val action = e.action

        // the user touched the screen or dragged along the screen
        if (action == MotionEvent.ACTION_DOWN ||
            action == MotionEvent.ACTION_MOVE
        ) {
            // fire the cannonball toward the touch point
            alignAndFireCannonball(e)
        }
        return true
    }

    // Thread subclass to control the game loop
    private inner class CannonThread(  // for manipulating canvas
        private val surfaceHolder: SurfaceHolder
    ) : Thread() {
        private var threadIsRunning = true // running by default

        // changes running state
        fun setRunning(running: Boolean) {
            threadIsRunning = running
        }

        // controls the game loop
        override fun run() {
            var canvas: Canvas? = null // used for drawing
            var previousFrameTime = System.currentTimeMillis()
            while (threadIsRunning) {
                try {
                    // get Canvas for exclusive drawing from this thread
                    canvas = surfaceHolder.lockCanvas(null)

                    // lock the surfaceHolder for drawing
                    synchronized(surfaceHolder) {
                        val currentTime = System.currentTimeMillis()
                        val elapsedTimeMS =
                            (currentTime - previousFrameTime).toDouble()
                        totalElapsedTime += elapsedTimeMS / 1000.0
                        updatePositions(elapsedTimeMS) // update game state
                        testForCollisions() // test for GameElement collisions
                        drawGameElements(canvas) // draw using the canvas
                        previousFrameTime = currentTime // update previous time
                    }
                } finally {
                    // display canvas's contents on the CannonView
                    // and enable other threads to use the Canvas
                    if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }

        // initializes the surface holder
        init {
            name = "CannonThread"
        }
    }

    // hide system bars and app bar
    private fun hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_FULLSCREEN or
                    SYSTEM_UI_FLAG_IMMERSIVE
    }

    // show system bars and app bar
    private fun showSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    companion object {
        private const val TAG = "CannonView" // for logging errors

        // constants for game play
        const val MISS_PENALTY = 2 // seconds deducted on a miss
        const val HIT_REWARD = 3 // seconds added on a hit

        // constants for the Cannon
        const val CANNON_BASE_RADIUS_PERCENT = 3.0 / 40
        const val CANNON_BARREL_WIDTH_PERCENT = 3.0 / 40
        const val CANNON_BARREL_LENGTH_PERCENT = 1.0 / 10

        // constants for the Cannonball
        const val CANNONBALL_RADIUS_PERCENT = 3.0 / 80
        const val CANNONBALL_SPEED_PERCENT = 3.0 / 2

        // constants for the Targets
        const val TARGET_WIDTH_PERCENT = 1.0 / 40
        const val TARGET_LENGTH_PERCENT = 3.0 / 20
        const val TARGET_FIRST_X_PERCENT = 3.0 / 5
        const val TARGET_SPACING_PERCENT = 1.0 / 60
        const val TARGET_PIECES = 9.0
        const val TARGET_MIN_SPEED_PERCENT = 3.0 / 4
        const val TARGET_MAX_SPEED_PERCENT = 6.0 / 4

        // constants for the Blocker
        const val BLOCKER_WIDTH_PERCENT = 1.0 / 40
        const val BLOCKER_LENGTH_PERCENT = 1.0 / 4
        const val BLOCKER_X_PERCENT = 1.0 / 2
        const val BLOCKER_SPEED_PERCENT = 1.0

        // text size 1/18 of screen width
        const val TEXT_SIZE_PERCENT = 1.0 / 18

        // constants and variables for managing sounds
        const val TARGET_SOUND_ID = 0
        const val CANNON_SOUND_ID = 1
        const val BLOCKER_SOUND_ID = 2
    }

    // constructor
    init {
        activity = context as Activity // store reference to MainActivity

        // register SurfaceHolder.Callback listener
        holder.addCallback(this)

        // configure audio attributes for game audio
        val attrBuilder = AudioAttributes.Builder()
        attrBuilder.setUsage(AudioAttributes.USAGE_GAME)

        // initialize SoundPool to play the app's three sound effects
        val builder = SoundPool.Builder()
        builder.setMaxStreams(1)
        builder.setAudioAttributes(attrBuilder.build())
        soundPool = builder.build()

        // create Map of sounds and pre-load sounds
        soundMap = SparseIntArray(3) // create new SparseIntArray
        soundMap.put(
            TARGET_SOUND_ID,
            soundPool!!.load(context, R.raw.target_hit, 1)
        )
        soundMap.put(
            CANNON_SOUND_ID,
            soundPool!!.load(context, R.raw.cannon_fire, 1)
        )
        soundMap.put(
            BLOCKER_SOUND_ID,
            soundPool!!.load(context, R.raw.blocker_hit, 1)
        )
        textPaint = Paint()
        backgroundPaint = Paint()
        backgroundPaint.color = Color.WHITE
        this.context2 = context
    }
}

/*********************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and * Pearson Education, *
 * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
 * book have used their * best efforts in preparing the book. These efforts      *
 * include the * development, research, and testing of the theories and programs *
 * * to determine their effectiveness. The authors and publisher make * no       *
 * warranty of any kind, expressed or implied, with regard to these * programs   *
 * or to the documentation contained in these books. The authors * and publisher *
 * shall not be liable in any event for incidental or * consequential damages in *
 * connection with, or arising out of, the * furnishing, performance, or use of  *
 * these programs.                                                               *
 *********************************************************************************/


//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.media.AudioAttributes;
//import android.media.SoundPool;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.util.SparseIntArray;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//import pl.pjatk.pamo.calculatorbmi.R;
//
//public class CannonView extends SurfaceView
//   implements SurfaceHolder.Callback {
//
//   private static final String TAG = "CannonView"; // for logging errors
//
//   // constants for game play
//   public static final int MISS_PENALTY = 2; // seconds deducted on a miss
//   public static final int HIT_REWARD = 3; // seconds added on a hit
//
//   // constants for the Cannon
//   public static final double CANNON_BASE_RADIUS_PERCENT = 3.0 / 40;
//   public static final double CANNON_BARREL_WIDTH_PERCENT = 3.0 / 40;
//   public static final double CANNON_BARREL_LENGTH_PERCENT = 1.0 / 10;
//
//   // constants for the Cannonball
//   public static final double CANNONBALL_RADIUS_PERCENT = 3.0 / 80;
//   public static final double CANNONBALL_SPEED_PERCENT = 3.0 / 2;
//
//   // constants for the Targets
//   public static final double TARGET_WIDTH_PERCENT = 1.0 / 40;
//   public static final double TARGET_LENGTH_PERCENT = 3.0 / 20;
//   public static final double TARGET_FIRST_X_PERCENT = 3.0 / 5;
//   public static final double TARGET_SPACING_PERCENT = 1.0 / 60;
//   public static final double TARGET_PIECES = 9;
//   public static final double TARGET_MIN_SPEED_PERCENT = 3.0 / 4;
//   public static final double TARGET_MAX_SPEED_PERCENT = 6.0 / 4;
//
//   // constants for the Blocker
//   public static final double BLOCKER_WIDTH_PERCENT = 1.0 / 40;
//   public static final double BLOCKER_LENGTH_PERCENT = 1.0 / 4;
//   public static final double BLOCKER_X_PERCENT = 1.0 / 2;
//   public static final double BLOCKER_SPEED_PERCENT = 1.0;
//
//   // text size 1/18 of screen width
//   public static final double TEXT_SIZE_PERCENT = 1.0 / 18;
//
//   private CannonThread cannonThread; // controls the game loop
//   private Activity activity; // to display Game Over dialog in GUI thread
//   private boolean dialogIsDisplayed = false;
//
//   // game objects
//   private pl.pjatk.pamo.calculatorbmi.game.Cannon cannon;
//   private Blocker blocker;
//   private ArrayList<pl.pjatk.pamo.calculatorbmi.game.Target> targets;
//
//   // dimension variables
//   private int screenWidth;
//   private int screenHeight;
//
//   // variables for the game loop and tracking statistics
//   private boolean gameOver; // is the game over?
//   private double timeLeft; // time remaining in seconds
//   private int shotsFired; // shots the user has fired
//   private double totalElapsedTime; // elapsed seconds
//
//   // constants and variables for managing sounds
//   public static final int TARGET_SOUND_ID = 0;
//   public static final int CANNON_SOUND_ID = 1;
//   public static final int BLOCKER_SOUND_ID = 2;
//   private SoundPool soundPool; // plays sound effects
//   private SparseIntArray soundMap; // maps IDs to SoundPool
//
//   // Paint variables used when drawing each item on the screen
//   private Paint textPaint; // Paint used to draw text
//   private Paint backgroundPaint; // Paint used to clear the drawing area
//
//   Context context;
//
//   // constructor
//   public CannonView(Context context, AttributeSet attrs) {
//      super(context, attrs); // call superclass constructor
//      activity = (Activity) context; // store reference to MainActivity
//
//      // register SurfaceHolder.Callback listener
//      getHolder().addCallback(this);
//
//      // configure audio attributes for game audio
//      AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
//      attrBuilder.setUsage(AudioAttributes.USAGE_GAME);
//
//      // initialize SoundPool to play the app's three sound effects
//      SoundPool.Builder builder = new SoundPool.Builder();
//      builder.setMaxStreams(1);
//      builder.setAudioAttributes(attrBuilder.build());
//      soundPool = builder.build();
//
//      // create Map of sounds and pre-load sounds
//      soundMap = new SparseIntArray(3); // create new SparseIntArray
//      soundMap.put(TARGET_SOUND_ID,
//         soundPool.load(context, R.raw.target_hit, 1));
//      soundMap.put(CANNON_SOUND_ID,
//         soundPool.load(context, R.raw.cannon_fire, 1));
//      soundMap.put(BLOCKER_SOUND_ID,
//         soundPool.load(context, R.raw.blocker_hit, 1));
//
//      textPaint = new Paint();
//      backgroundPaint = new Paint();
//      backgroundPaint.setColor(Color.WHITE);
//
//      this.context = context;
//   }
//
//   // called when the size of the SurfaceView changes,
//   // such as when it's first added to the View hierarchy
//   @Override
//   protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//      super.onSizeChanged(w, h, oldw, oldh);
//
//      screenWidth = w; // store CannonView's width
//      screenHeight = h; // store CannonView's height
//
//      // configure text properties
//      textPaint.setTextSize((int) (TEXT_SIZE_PERCENT * screenHeight));
//      textPaint.setAntiAlias(true); // smoothes the text
//   }
//
//   // get width of the game screen
//   public int getScreenWidth() {
//      return screenWidth;
//   }
//
//   // get height of the game screen
//   public int getScreenHeight() {
//      return screenHeight;
//   }
//
//   // plays a sound with the given soundId in soundMap
//   public void playSound(int soundId) {
//      soundPool.play(soundMap.get(soundId), 1, 1, 1, 0, 1f);
//   }
//
//   // reset all the screen elements and start a new game
//   public void newGame() {
//      // construct a new Cannon
//      cannon = new pl.pjatk.pamo.calculatorbmi.game.Cannon(this,
//         (int) (CANNON_BASE_RADIUS_PERCENT * screenHeight),
//         (int) (CANNON_BARREL_LENGTH_PERCENT * screenWidth),
//         (int) (CANNON_BARREL_WIDTH_PERCENT * screenHeight));
//
//      Random random = new Random(); // for determining random velocities
//      targets = new ArrayList<>(); // construct a new Target list
//
//      // initialize targetX for the first Target from the left
//      int targetX = (int) (TARGET_FIRST_X_PERCENT * screenWidth);
//
//      // calculate Y coordinate of Targets
//      int targetY = (int) ((0.5 - TARGET_LENGTH_PERCENT / 2) *
//         screenHeight);
//
//      // add TARGET_PIECES Targets to the Target list
//      for (int n = 0; n < TARGET_PIECES; n++) {
//
//         // determine a random velocity between min and max values
//         // for Target n
//         double velocity = screenHeight * (random.nextDouble() *
//            (TARGET_MAX_SPEED_PERCENT - TARGET_MIN_SPEED_PERCENT) +
//            TARGET_MIN_SPEED_PERCENT);
//
//         // alternate Target colors between dark and light
//         int color =  (n % 2 == 0) ?
//            getResources().getColor(R.color.dark,
//               getContext().getTheme()) :
//            getResources().getColor(R.color.light,
//               getContext().getTheme());
//
//         velocity *= -1; // reverse the initial velocity for next Target
//
//         // create and add a new Target to the Target list
//         targets.add(new pl.pjatk.pamo.calculatorbmi.game.Target(this, color, HIT_REWARD, targetX, targetY,
//            (int) (TARGET_WIDTH_PERCENT * screenWidth),
//            (int) (TARGET_LENGTH_PERCENT * screenHeight),
//            (int) velocity));
//
//         // increase the x coordinate to position the next Target more
//         // to the right
//         targetX += (TARGET_WIDTH_PERCENT + TARGET_SPACING_PERCENT) *
//            screenWidth;
//      }
//
//      // create a new Blocker
//      blocker = new Blocker(this, Color.BLACK, MISS_PENALTY,
//         (int) (BLOCKER_X_PERCENT * screenWidth),
//         (int) ((0.5 - BLOCKER_LENGTH_PERCENT / 2) * screenHeight),
//         (int) (BLOCKER_WIDTH_PERCENT * screenWidth),
//         (int) (BLOCKER_LENGTH_PERCENT * screenHeight),
//         (float) (BLOCKER_SPEED_PERCENT * screenHeight));
//
//      timeLeft = 20; // start the countdown at 10 seconds
//
//      shotsFired = 0; // set the initial number of shots fired
//      totalElapsedTime = 0.0; // set the time elapsed to zero
//
//      if (gameOver) { // start a new game after the last game ended
//         gameOver = false; // the game is not over
//         cannonThread = new CannonThread(getHolder()); // create thread
//         cannonThread.start(); // start the game loop thread
//      }
//
//      hideSystemBars();
//   }
//
//   // called repeatedly by the CannonThread to update game elements
//   private void updatePositions(double elapsedTimeMS) {
//      double interval = elapsedTimeMS / 1000.0; // convert to seconds
//
//      // update cannonball's position if it is on the screen
//      if (cannon.getCannonball() != null)
//         cannon.getCannonball().update(interval);
//
//      blocker.update(interval); // update the blocker's position
//
//      for (pl.pjatk.pamo.calculatorbmi.game.GameElement target : targets)
//         target.update(interval); // update the target's position
//
//      timeLeft -= interval; // subtract from time left
//
//      // if the timer reached zero
//      if (timeLeft <= 0) {
//         timeLeft = 0.0;
//         gameOver = true; // the game is over
//         cannonThread.setRunning(false); // terminate thread
//         showGameOverDialog(R.string.lose); // show the losing dialog
//      }
//
//      // if all pieces have been hit
//      if (targets.isEmpty()) {
//         cannonThread.setRunning(false); // terminate thread
//         showGameOverDialog(R.string.win); // show winning dialog
//         gameOver = true;
//      }
//   }
//
//   // aligns the barrel and fires a Cannonball if a Cannonball is not
//   // already on the screen
//   public void alignAndFireCannonball(MotionEvent event) {
//      // get the location of the touch in this view
//      Point touchPoint = new Point((int) event.getX(),
//         (int) event.getY());
//
//      // compute the touch's distance from center of the screen
//      // on the y-axis
//      double centerMinusY = (screenHeight / 2 - touchPoint.y);
//
//      double angle = 0; // initialize angle to 0
//
//      // calculate the angle the barrel makes with the horizontal
//      angle = Math.atan2(touchPoint.x, centerMinusY);
//
//      // point the barrel at the point where the screen was touched
//      cannon.align(angle);
//
//      // fire Cannonball if there is not already a Cannonball on screen
//      if (cannon.getCannonball() == null ||
//         !cannon.getCannonball().isOnScreen()) {
//         cannon.fireCannonball();
//         ++shotsFired;
//      }
//   }
//
//   // display an AlertDialog when the game ends
//   private void showGameOverDialog(final int messageId) {
//      // DialogFragment to display game stats and start new game
//      final DialogFragment gameResult =
//         new DialogFragment() {
//            // create an AlertDialog and return it
//            @Override
//            public Dialog onCreateDialog(Bundle bundle) {
//               // create dialog displaying String resource for messageId
//               AlertDialog.Builder builder =
//                  new AlertDialog.Builder(getActivity());
//               builder.setTitle(getResources().getString(messageId));
//
//               // display number of shots fired and total time elapsed
//               builder.setMessage(getResources().getString(
//                  R.string.results_format, shotsFired, totalElapsedTime));
//               builder.setPositiveButton(R.string.reset_game,
//                  new DialogInterface.OnClickListener() {
//                     // called when "Reset Game" Button is pressed
//                     @Override
//                     public void onClick(DialogInterface dialog,
//                                         int which) {
//                        dialogIsDisplayed = false;
//                        newGame(); // set up and start a new game
//                     }
//                  }
//               );
//
//               return builder.create(); // return the AlertDialog
//            }
//         };
//
//      // in GUI thread, use FragmentManager to display the DialogFragment
//      activity.runOnUiThread(
//         new Runnable() {
//            public void run() {
//               showSystemBars();
//               dialogIsDisplayed = true;
//               gameResult.setCancelable(false); // modal dialog
//               gameResult.show(activity.getFragmentManager(), "results");
//            }
//         }
//      );
//   }
//
//   // draws the game to the given Canvas
//   public void drawGameElements(Canvas canvas) {
//      // clear the background
//      canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(),
//         backgroundPaint);
//
//      // display time remaining
//      canvas.drawText(getResources().getString(
//         R.string.time_remaining_format, timeLeft), 50, 100, textPaint);
//
//      cannon.draw(canvas); // draw the cannon
//
//      // draw the GameElements
//      if (cannon.getCannonball() != null &&
//         cannon.getCannonball().isOnScreen())
//         cannon.getCannonball().draw(canvas);
//
//      blocker.draw(canvas); // draw the blocker
//
//      // draw all of the Targets
//      for (pl.pjatk.pamo.calculatorbmi.game.GameElement target : targets)
//         target.draw(canvas);
//   }
//
//   // checks if the ball collides with the Blocker or any of the Targets
//   // and handles the collisions
//   public void testForCollisions() {
//      // remove any of the targets that the Cannonball
//      // collides with
//      if (cannon.getCannonball() != null &&
//         cannon.getCannonball().isOnScreen()) {
//         for (int n = 0; n < targets.size(); n++) {
//            if (cannon.getCannonball().collidesWith(targets.get(n))) {
//               targets.get(n).playSound(); // play Target hit sound
//
//               // add hit rewards time to remaining time
//               timeLeft += targets.get(n).getHitReward();
//
//               cannon.removeCannonball(); // remove Cannonball from game
//               targets.remove(n); // remove the Target that was hit
//               --n; // ensures that we don't skip testing new target n
//               break;
//            }
//         }
//      }
//      else { // remove the Cannonball if it should not beon the screen
//         cannon.removeCannonball();
//      }
//
//      // check if ball collides with blocker
//      if (cannon.getCannonball() != null &&
//         cannon.getCannonball().collidesWith(blocker)) {
//         blocker.playSound(); // play Blocker hit sound
//
//         // reverse ball direction
//         cannon.getCannonball().reverseVelocityX();
//
//         // deduct blocker's miss penalty from remaining time
//         timeLeft -= blocker.getMissPenalty();
//      }
//   }
//
//   // stops the game: called by CannonGameFragment's onPause method
//   public void stopGame() {
//      if (cannonThread != null)
//         cannonThread.setRunning(false); // tell thread to terminate
//   }
//
//   // release resources: called by CannonGame's onDestroy method
//   public void releaseResources() {
//      soundPool.release(); // release all resources used by the SoundPool
//      soundPool = null;
//   }
//
//   // called when surface changes size
//   @Override
//   public void surfaceChanged(SurfaceHolder holder, int format,
//                              int width, int height) { }
//
//   // called when surface is first created
//   @Override
//   public void surfaceCreated(SurfaceHolder holder) {
//      if (!dialogIsDisplayed) {
//         newGame(); // set up and start a new game
//         cannonThread = new CannonThread(holder); // create thread
//         cannonThread.setRunning(true); // start game running
//         cannonThread.start(); // start the game loop thread
//      }
//   }
//
//   // called when the surface is destroyed
//   @Override
//   public void surfaceDestroyed(SurfaceHolder holder) {
//      // ensure that thread terminates properly
//      boolean retry = true;
//      cannonThread.setRunning(false); // terminate cannonThread
//
//      while (retry) {
//         try {
//            cannonThread.join(); // wait for cannonThread to finish
//            retry = false;
//         }
//         catch (InterruptedException e) {
//            Log.e(TAG, "Thread interrupted", e);
//         }
//      }
//   }
//
//   // called when the user touches the screen in this activity
//   @Override
//   public boolean onTouchEvent(MotionEvent e) {
//      // get int representing the type of action which caused this event
//      int action = e.getAction();
//
//      // the user touched the screen or dragged along the screen
//      if (action == MotionEvent.ACTION_DOWN ||
//         action == MotionEvent.ACTION_MOVE) {
//         // fire the cannonball toward the touch point
//         alignAndFireCannonball(e);
//      }
//
//      return true;
//   }
//
//   // Thread subclass to control the game loop
//   private class CannonThread extends Thread {
//      private SurfaceHolder surfaceHolder; // for manipulating canvas
//      private boolean threadIsRunning = true; // running by default
//
//      // initializes the surface holder
//      public CannonThread(SurfaceHolder holder) {
//         surfaceHolder = holder;
//         setName("CannonThread");
//      }
//
//      // changes running state
//      public void setRunning(boolean running) {
//         threadIsRunning = running;
//      }
//
//      // controls the game loop
//      @Override
//      public void run() {
//         Canvas canvas = null; // used for drawing
//         long previousFrameTime = System.currentTimeMillis();
//
//         while (threadIsRunning) {
//            try {
//               // get Canvas for exclusive drawing from this thread
//               canvas = surfaceHolder.lockCanvas(null);
//
//               // lock the surfaceHolder for drawing
//               synchronized(surfaceHolder) {
//                  long currentTime = System.currentTimeMillis();
//                  double elapsedTimeMS = currentTime - previousFrameTime;
//                  totalElapsedTime += elapsedTimeMS / 1000.0;
//                  updatePositions(elapsedTimeMS); // update game state
//                  testForCollisions(); // test for GameElement collisions
//                  drawGameElements(canvas); // draw using the canvas
//                  previousFrameTime = currentTime; // update previous time
//               }
//            }
//            finally {
//               // display canvas's contents on the CannonView
//               // and enable other threads to use the Canvas
//               if (canvas != null)
//                  surfaceHolder.unlockCanvasAndPost(canvas);
//            }
//         }
//      }
//   }
//
//   // hide system bars and app bar
//   private void hideSystemBars() {
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//         setSystemUiVisibility(
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//            View.SYSTEM_UI_FLAG_FULLSCREEN |
//            View.SYSTEM_UI_FLAG_IMMERSIVE);
//   }
//
//   // show system bars and app bar
//   private void showSystemBars() {
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//         setSystemUiVisibility(
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//   }
//}
//
///*********************************************************************************
// * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and * Pearson Education, *
// * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
// * book have used their * best efforts in preparing the book. These efforts      *
// * include the * development, research, and testing of the theories and programs *
// * * to determine their effectiveness. The authors and publisher make * no       *
// * warranty of any kind, expressed or implied, with regard to these * programs   *
// * or to the documentation contained in these books. The authors * and publisher *
// * shall not be liable in any event for incidental or * consequential damages in *
// * connection with, or arising out of, the * furnishing, performance, or use of  *
// * these programs.                                                               *
// *********************************************************************************/
