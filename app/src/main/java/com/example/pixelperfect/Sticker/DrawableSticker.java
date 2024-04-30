package com.example.pixelperfect.Sticker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
/**
 * 图片贴纸
 */
public class DrawableSticker extends Sticker {
    private Drawable drawable;

    private Rect realBounds; //真实的矩形位置

    public DrawableSticker(Drawable drawable) {
        this.drawable = drawable;
        this.realBounds = new Rect(0, 0, getWidth(), getHeight());
    }

    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());
        this.drawable.setBounds(this.realBounds);
        this.drawable.draw(canvas);
        canvas.restore();
    }

    public int getAlpha() {
        return this.drawable.getAlpha();
    }

    @NonNull
    public Drawable getDrawable() {
        return this.drawable;
    }

    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public void release() {
        super.release();
        if (this.drawable != null)
            this.drawable = null;
    }

    @NonNull
    public DrawableSticker setAlpha(@IntRange(from = 0L, to = 255L) int alpha) {
        this.drawable.setAlpha(alpha);
        return this;
    }

    public DrawableSticker setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        return this;
    }
}
