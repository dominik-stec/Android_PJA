package pl.pjatk.pamo.kotlincalculatorbmi.game

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect


open class BitmapElement {
    var bitmap: Bitmap? = null
    var view // the view that contains this GameElement
            : CannonView? = null
    var velocityY // the vertical velocity of this GameElement
            = 0f
    var soundId // the sound associated with this GameElement
            = 0
    var shape // the GameElement's rectangular bounds
            : Rect? = null

    constructor() {}
    constructor(
        view: CannonView?, bitmap: Bitmap, soundId: Int, x: Int,
        y: Int, velocityY: Float
    ) {
        this.bitmap = bitmap
        this.view = view
        this.soundId = soundId
        this.velocityY = velocityY
        val bitmapHeight = bitmap.height
        val bitmapWidth = bitmap.width
        shape = Rect(x, y, x + bitmapWidth, y + bitmapHeight)
    }

    fun getBitmap(drawableImageResources: Int): Bitmap {
        return BitmapFactory.decodeResource(view!!.getContext().resources, drawableImageResources)
    }
}

//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Rect;
//
//public class BitmapElement {
//
//    public Bitmap bitmap;
//    public CannonView view; // the view that contains this GameElement
//    public float velocityY; // the vertical velocity of this GameElement
//    public int soundId; // the sound associated with this GameElement
//    public Rect shape; // the GameElement's rectangular bounds
//
//    public BitmapElement() {}
//
//    public BitmapElement(CannonView view, Bitmap bitmap, int soundId, int x,
//                         int y, float velocityY) {
//        this.bitmap = bitmap;
//        this.view = view;
//        this.soundId = soundId;
//        this.velocityY = velocityY;
//        int bitmapHeight = bitmap.getHeight();
//        int bitmapWidth = bitmap.getWidth();
//        this.shape = new Rect(x, y, x + bitmapWidth, y + bitmapHeight);
//    }
//
//    public Bitmap getBitmap(int drawableImageResources) {
//        Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(), drawableImageResources);
//        return bitmap;
//    }
//
//}
