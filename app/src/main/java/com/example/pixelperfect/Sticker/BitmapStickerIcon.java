package com.example.pixelperfect.Sticker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.example.pixelperfect.Event.StickerIconEvent;

/**
 * 图片贴纸
 */
public class BitmapStickerIcon extends DrawableSticker implements StickerIconEvent {
    public static final String ALIGN_HORIZONTALLY = "ALIGN_HORIZONTALLY";

    public static final String EDIT = "EDIT";

    public static final String FLIP = "FLIP";

    public static final String REMOVE = "REMOVE";

    public static final String ROTATE = "ROTATE";

    public static final String ZOOM = "ZOOM";

    private StickerIconEvent iconEvent;

    private float iconExtraRadius = 10.0F;

    private float iconRadius = 30.0F;

    private int position = 0;

    private String tag;

    private float x;

    private float y;

    public BitmapStickerIcon(Drawable drawable, int position, String tag) {
        super(drawable);
        this.position = position;
        this.tag = tag;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(this.x, this.y, this.iconRadius, paint);
        draw(canvas);
    }

    public float getIconRadius() {
        return this.iconRadius;
    }

    public int getPosition() {
        return this.position;
    }

    public String getTag() {
        return this.tag;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void onActionDown(StickerView stickerView, MotionEvent motionEvent) {
        if (this.iconEvent != null)
            this.iconEvent.onActionDown(stickerView, motionEvent);
    }

    public void onActionMove(StickerView stickerView, MotionEvent motionEvent) {
        if (this.iconEvent != null)
            this.iconEvent.onActionMove(stickerView, motionEvent);
    }

    public void onActionUp(StickerView stickerView, MotionEvent motionEvent) {
        if (this.iconEvent != null)
            this.iconEvent.onActionUp(stickerView, motionEvent);
    }

    public void setIconEvent(StickerIconEvent iconEvent) {
        this.iconEvent = iconEvent;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

}
