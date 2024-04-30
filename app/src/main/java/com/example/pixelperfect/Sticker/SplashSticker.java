package com.example.pixelperfect.Sticker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class SplashSticker extends Sticker {
    /**
     * 覆盖的图片
     */
    private Bitmap bitmapOver;
    /**
     * 混合的图片
     */
    private Bitmap bitmapXor;
    private Paint over;
    private Paint xor = new Paint();

    public SplashSticker(Bitmap bitmapXor, Bitmap bitmapOver) {
        this.xor.setDither(true);
        this.xor.setAntiAlias(true);
        this.xor.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        this.over = new Paint();
        this.over.setDither(true);
        this.over.setAntiAlias(true);
        this.over.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.bitmapXor = bitmapXor;
        this.bitmapOver = bitmapOver;
    }

    private Bitmap getBitmapOver() {
        return this.bitmapOver;
    }

    private Bitmap getBitmapXor() {
        return this.bitmapXor;
    }

    public void draw(@NonNull Canvas canvas) {
        canvas.drawBitmap(getBitmapXor(), getMatrix(), this.xor);
        canvas.drawBitmap(getBitmapOver(), getMatrix(), this.over);
    }

    public int getAlpha() {
        return 1;
    }

    @NonNull
    public Drawable getDrawable() {
        return null;
    }

    public int getHeight() {
        return this.bitmapOver.getHeight();
    }

    public int getWidth() {
        return this.bitmapXor.getWidth();
    }

    public void release() {
        super.release();
        this.xor = null;
        this.over = null;
        if (this.bitmapXor != null)
            this.bitmapXor.recycle();
        this.bitmapXor = null;
        if (this.bitmapOver != null)
            this.bitmapOver.recycle();
        this.bitmapOver = null;
    }

    @NonNull
    public SplashSticker setAlpha(@IntRange(from = 0L, to = 255L) int alpha) {
        return this;
    }

    public SplashSticker setDrawable(@NonNull Drawable drawable) {
        return this;
    }
}
