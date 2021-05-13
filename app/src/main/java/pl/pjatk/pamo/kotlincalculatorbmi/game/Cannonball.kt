// Cannonball.java
// Represents the Cannonball that the Cannon fires
package pl.pjatk.pamo.kotlincalculatorbmi.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect


class Cannonball : GameElement {
    private var velocityX: Float

    // returns true if this Cannonball is on the screen
    var isOnScreen: Boolean
        private set
    override var bitmap: Bitmap? = null

    // constructor
    constructor(
        view: CannonView, color: Int, soundId: Int, x: Int,
        y: Int, radius: Int, velocityX: Float, velocityY: Float
    ) : super(
        view, color, soundId, x, y,
        2 * radius, 2 * radius, velocityY
    ) {
        this.velocityX = velocityX
        isOnScreen = true
    }

    constructor(
        view: CannonView, bitmap: Bitmap?, color: Int, soundId: Int, x: Int,
        y: Int, radius: Int, velocityX: Float, velocityY: Float
    ) : super(
        view, color, soundId, x, y,
        2 * radius, 2 * radius, velocityY
    ) {
        this.velocityX = velocityX
        isOnScreen = true
        this.bitmap = bitmap
    }

    // get Cannonball's radius
    private val radius: Int
        private get() = (shape.right - shape.left) / 2

    // test whether Cannonball collides with the given GameElement
    fun collidesWith(element: GameElement): Boolean {
        return Rect.intersects(shape, element.shape) && velocityX > 0
    }

    // reverses the Cannonball's horizontal velocity
    fun reverseVelocityX() {
        velocityX *= -1f
    }

    // updates the Cannonball's position
    override fun update(interval: Double) {
        super.update(interval) // updates Cannonball's vertical position

        // update horizontal position
        shape.offset((velocityX * interval).toInt(), 0)

        // if Cannonball goes off the screen
        if (shape.top < 0 || shape.left < 0 || shape.bottom > view!!.screenHeight || shape.right > view!!.screenWidth) isOnScreen =
            false // set it to be removed
    }

    // draws the Cannonball on the given canvas
    override fun draw(canvas: Canvas) {
//      canvas.drawCircle(shape.left + getRadius(),
//         shape.top + getRadius(), getRadius(), paint);
        //Rect shSrc = new Rect(0, 0, 20, 20);
        val shDst = Rect(
            shape.left, shape.top, shape.left + radius * 7,
            shape.top + radius * 3
        )
        canvas.drawBitmap(bitmap!!, null, shDst, null)
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
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Rect;
//
//public class Cannonball extends pl.pjatk.pamo.calculatorbmi.game.GameElement {
//   private float velocityX;
//   private boolean onScreen;
//
//   public Bitmap bitmap;
//
//   // constructor
//   public Cannonball(CannonView view, int color, int soundId, int x,
//      int y, int radius, float velocityX, float velocityY) {
//      super(view, color, soundId, x, y,
//         2 * radius, 2 * radius, velocityY);
//      this.velocityX = velocityX;
//      onScreen = true;
//   }
//
//   public Cannonball(CannonView view, Bitmap bitmap, int color, int soundId, int x,
//                     int y, int radius, float velocityX, float velocityY) {
//      super(view, color, soundId, x, y,
//              2 * radius, 2 * radius, velocityY);
//      this.velocityX = velocityX;
//      onScreen = true;
//
//      this.bitmap = bitmap;
//   }
//
//   // get Cannonball's radius
//   private int getRadius() {
//      return (shape.right - shape.left) / 2;
//   }
//
//   // test whether Cannonball collides with the given GameElement
//   public boolean collidesWith(pl.pjatk.pamo.calculatorbmi.game.GameElement element) {
//      return (Rect.intersects(shape, element.shape) && velocityX > 0);
//   }
//
//   // returns true if this Cannonball is on the screen
//   public boolean isOnScreen() {
//      return onScreen;
//   }
//
//   // reverses the Cannonball's horizontal velocity
//   public void reverseVelocityX() {
//      velocityX *= -1;
//   }
//
//   // updates the Cannonball's position
//   @Override
//   public void update(double interval) {
//      super.update(interval); // updates Cannonball's vertical position
//
//      // update horizontal position
//      shape.offset((int) (velocityX * interval), 0);
//
//      // if Cannonball goes off the screen
//      if (shape.top < 0 || shape.left < 0 ||
//         shape.bottom > view.getScreenHeight() ||
//         shape.right > view.getScreenWidth())
//         onScreen = false; // set it to be removed
//   }
//
//   // draws the Cannonball on the given canvas
//   @Override
//   public void draw(Canvas canvas) {
////      canvas.drawCircle(shape.left + getRadius(),
////         shape.top + getRadius(), getRadius(), paint);
//      //Rect shSrc = new Rect(0, 0, 20, 20);
//      Rect shDst = new Rect(shape.left, shape.top, shape.left + getRadius()*7,
//              shape.top + getRadius()*3);
//      canvas.drawBitmap(bitmap, null, shDst, null );
//   }
//
//
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
