package pl.pjatk.pamo.kotlincalculatorbmi.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class BitmapElement {

    public Bitmap bitmap;
    public CannonView view; // the view that contains this GameElement
    public float velocityY; // the vertical velocity of this GameElement
    public int soundId; // the sound associated with this GameElement
    public Rect shape; // the GameElement's rectangular bounds

    public BitmapElement() {}

    public BitmapElement(CannonView view, Bitmap bitmap, int soundId, int x,
                         int y, float velocityY) {
        this.bitmap = bitmap;
        this.view = view;
        this.soundId = soundId;
        this.velocityY = velocityY;
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        this.shape = new Rect(x, y, x + bitmapWidth, y + bitmapHeight);
    }

    public Bitmap getBitmap(int drawableImageResources) {
        Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(), drawableImageResources);
        return bitmap;
    }

}
