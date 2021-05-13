// Cannon.java
// Represents Cannon and fires the Cannonball
package pl.pjatk.pamo.kotlincalculatorbmi.game


import android.graphics.*
import pl.pjatk.pamo.calculatorbmi.R


class Cannon(// view containing the Cannon
    private val view: CannonView, // Cannon base's radius
    private val baseRadius: Int, barrelLength: Int,
    barrelWidth: Int
) {
    private val barrelLength // Cannon barrel's length
            : Int
    private val barrelEnd = Point() // endpoint of Cannon's barrel
    private var barrelAngle // angle of the Cannon's barrel
            = 0.0

    // returns the Cannonball that this Cannon fired
    var cannonball // the Cannon's Cannonball
            : Cannonball? = null
        private set
    private val paint = Paint() // Paint used to draw the cannon

    // aligns the Cannon's barrel to the given angle
    fun align(barrelAngle: Double) {
        this.barrelAngle = barrelAngle
        barrelEnd.x = (barrelLength * Math.sin(barrelAngle)).toInt()
        barrelEnd.y = (-barrelLength * Math.cos(barrelAngle)).toInt() +
                view.screenHeight / 2
    }

    // creates and fires Cannonball in the direction Cannon points
    fun fireCannonball() {
        // calculate the Cannonball velocity's x component
        val velocityX = (CannonView.CANNONBALL_SPEED_PERCENT *
                view.screenWidth * Math.sin(barrelAngle)).toInt()

        // calculate the Cannonball velocity's y component
        val velocityY = (CannonView.CANNONBALL_SPEED_PERCENT *
                view.screenWidth * -Math.cos(barrelAngle)).toInt()

        // calculate the Cannonball's radius
        val radius = (view.screenHeight *
                CannonView.CANNONBALL_RADIUS_PERCENT).toInt()

        // bitmap source
        // https://www.dreamstime.com/syringe-medicine-medicine-single-icon-cartoon-style-vector-symbol-stock-illustration-web-image90533259
        val bm = BitmapFactory.decodeResource(view.resources, R.drawable.syringe_reverse)
        //Bitmap bm = loadBitmap("android.resource://pl.pjatk.pamo.calculatorbmi/R.drawable.syringe_reverse_bitmap");
        cannonball = Cannonball(
            view, bm, Color.BLACK,
            CannonView.CANNON_SOUND_ID, -radius,
            view.screenHeight / 2 - radius, radius, velocityX.toFloat(),
            velocityY.toFloat()
        )

//      // construct Cannonball and position it in the Cannon
//      cannonball = new Cannonball(view, Color.BLACK,
//         CannonView.CANNON_SOUND_ID, -radius,
//         view.getScreenHeight() / 2 - radius, radius, velocityX,
//         velocityY);
        cannonball.playSound() // play fire Cannonball sound
    }

    // draws the Cannon on the Canvas
    fun draw(canvas: Canvas) {
        // draw cannon barrel
        canvas.drawLine(
            0f, (view.screenHeight / 2).toFloat(), barrelEnd.x.toFloat(),
            barrelEnd.y.toFloat(), paint
        )

        // draw cannon base
        canvas.drawCircle(
            0f, (view.screenHeight / 2).toFloat(),
            baseRadius as Int.toFloat(), paint
        )
    }

    // removes the Cannonball from the game
    fun removeCannonball() {
        cannonball = null
    }

    // constructor
    init {
        baseRadius = baseRadius
        this.barrelLength = barrelLength
        paint.strokeWidth = barrelWidth.toFloat() // set width of barrel
        paint.color = Color.BLACK // Cannon's color is Black
        align(Math.PI / 2) // Cannon barrel facing straight right
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
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Point;
//
//import pl.pjatk.pamo.calculatorbmi.R;
//
//public class Cannon {
//   private int baseRadius; // Cannon base's radius
//   private int barrelLength; // Cannon barrel's length
//   private Point barrelEnd = new Point(); // endpoint of Cannon's barrel
//   private double barrelAngle; // angle of the Cannon's barrel
//   private Cannonball cannonball; // the Cannon's Cannonball
//   private Paint paint = new Paint(); // Paint used to draw the cannon
//   private CannonView view; // view containing the Cannon
//
//   // constructor
//   public Cannon(CannonView view, int baseRadius, int barrelLength,
//      int barrelWidth) {
//      this.view = view;
//      this.baseRadius = baseRadius;
//      this.barrelLength = barrelLength;
//      paint.setStrokeWidth(barrelWidth); // set width of barrel
//      paint.setColor(Color.BLACK); // Cannon's color is Black
//      align(Math.PI / 2); // Cannon barrel facing straight right
//   }
//
//   // aligns the Cannon's barrel to the given angle
//   public void align(double barrelAngle) {
//      this.barrelAngle = barrelAngle;
//      barrelEnd.x = (int) (barrelLength * Math.sin(barrelAngle));
//      barrelEnd.y = (int) (-barrelLength * Math.cos(barrelAngle)) +
//         view.getScreenHeight() / 2;
//   }
//
//   // creates and fires Cannonball in the direction Cannon points
//   public void fireCannonball() {
//      // calculate the Cannonball velocity's x component
//      int velocityX = (int) (CannonView.CANNONBALL_SPEED_PERCENT *
//         view.getScreenWidth() * Math.sin(barrelAngle));
//
//      // calculate the Cannonball velocity's y component
//      int velocityY = (int) (CannonView.CANNONBALL_SPEED_PERCENT *
//         view.getScreenWidth() * -Math.cos(barrelAngle));
//
//      // calculate the Cannonball's radius
//      int radius = (int) (view.getScreenHeight() *
//         CannonView.CANNONBALL_RADIUS_PERCENT);
//
//      // bitmap source
//      // https://www.dreamstime.com/syringe-medicine-medicine-single-icon-cartoon-style-vector-symbol-stock-illustration-web-image90533259
//      Bitmap bm = BitmapFactory.decodeResource(view.getResources(), R.drawable.syringe_reverse);
//      //Bitmap bm = loadBitmap("android.resource://pl.pjatk.pamo.calculatorbmi/R.drawable.syringe_reverse_bitmap");
//
//      cannonball = new Cannonball(view, bm, Color.BLACK,
//              CannonView.CANNON_SOUND_ID, -radius,
//              view.getScreenHeight() / 2 - radius, radius, velocityX,
//              velocityY);
//
////      // construct Cannonball and position it in the Cannon
////      cannonball = new Cannonball(view, Color.BLACK,
////         CannonView.CANNON_SOUND_ID, -radius,
////         view.getScreenHeight() / 2 - radius, radius, velocityX,
////         velocityY);
//
//      cannonball.playSound(); // play fire Cannonball sound
//   }
//
//   // draws the Cannon on the Canvas
//   public void draw(Canvas canvas) {
//      // draw cannon barrel
//      canvas.drawLine(0, view.getScreenHeight() / 2, barrelEnd.x,
//         barrelEnd.y, paint);
//
//      // draw cannon base
//      canvas.drawCircle(0, (int) view.getScreenHeight() / 2,
//         (int) baseRadius, paint);
//   }
//
//   // returns the Cannonball that this Cannon fired
//   public Cannonball getCannonball() {
//      return cannonball;
//   }
//
//   // removes the Cannonball from the game
//   public void removeCannonball() {
//      cannonball = null;
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
