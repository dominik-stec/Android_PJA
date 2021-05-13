// GameElement.java
// Represents a rectangle-bounded game element
package pl.pjatk.pamo.kotlincalculatorbmi.game


import android.graphics.*
import pl.pjatk.pamo.kotlincalculatorbmi.R


open class GameElement(// the view that contains this GameElement
     override var view: CannonView?, color: Int, soundId: Int, x: Int,
    y: Int, width: Int, length: Int, velocityY: Float
) :
    BitmapElement() {
    protected var paint = Paint() // Paint to draw this GameElement
    override var shape // the GameElement's rectangular bounds
            : Rect? = null
    override var velocityY // the vertical velocity of this GameElement
            : Float = 0f
    override var soundId // the sound associated with this GameElement
            : Int = 0
    override var bitmap: Bitmap? = null

    // update GameElement position and check for wall collisions
    open fun update(interval: Double) {
        // update vertical position
        shape!!.offset(0, (velocityY * interval).toInt())

        // if this GameElement collides with the wall, reverse direction
        if (shape!!.top < 0 && velocityY < 0 ||
            shape!!.bottom > view!!.screenHeight && velocityY > 0
        ) velocityY *= -1f // reverse this GameElement's velocity
    }

    // draws this GameElement on the given Canvas
    //   public void draw(Canvas canvas) {
    //      canvas.drawRect(shape, paint);
    //   }
    open fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap!!, null, shape!!, null)
    }

    // plays the sound that corresponds to this type of GameElement
    fun playSound() {
        view!!.playSound(soundId)
    }

    // public constructor
    init {
        paint.color = color
        shape = Rect(x, y, x + width, y + length) // set bounds
        this.soundId = soundId
        this.velocityY = velocityY

        //https://openclipart.org/image/800px/205971
        bitmap = BitmapFactory.decodeResource(view!!.resources, R.drawable.virus)
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
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//
//import pl.pjatk.pamo.calculatorbmi.R;
//
//public class GameElement extends BitmapElement{
//   protected CannonView view; // the view that contains this GameElement
//   protected Paint paint = new Paint(); // Paint to draw this GameElement
//   protected Rect shape; // the GameElement's rectangular bounds
//   private float velocityY; // the vertical velocity of this GameElement
//   private int soundId; // the sound associated with this GameElement
//
//   public Bitmap bitmap;
//
//   // public constructor
//   public GameElement(CannonView view, int color, int soundId, int x,
//      int y, int width, int length, float velocityY) {
//      this.view = view;
//      paint.setColor(color);
//      shape = new Rect(x, y, x + width, y + length); // set bounds
//      this.soundId = soundId;
//      this.velocityY = velocityY;
//
//      //https://openclipart.org/image/800px/205971
//      bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.virus);
//   }
//
//   // update GameElement position and check for wall collisions
//   public void update(double interval) {
//      // update vertical position
//      shape.offset(0, (int) (velocityY * interval));
//
//      // if this GameElement collides with the wall, reverse direction
//      if (shape.top < 0 && velocityY < 0 ||
//         shape.bottom > view.getScreenHeight() && velocityY > 0)
//         velocityY *= -1; // reverse this GameElement's velocity
//   }
//
//   // draws this GameElement on the given Canvas
////   public void draw(Canvas canvas) {
////      canvas.drawRect(shape, paint);
////   }
//
//   public void draw(Canvas canvas) {
//      canvas.drawBitmap(bitmap, null, shape, null );
//   }
//
//   // plays the sound that corresponds to this type of GameElement
//   public void playSound() {
//      view.playSound(soundId);
//   }
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
