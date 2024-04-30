package com.example.pixelperfect.Sticker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.example.pixelperfect.Utils.SystemUtil;


public class BeautySticker extends Sticker {
    private Drawable drawable;
    private int drawableSizeBoobs;
    private int drawableSizeFace_Height;
    private int drawableSizeFace_Width;
    private int drawableSizeHip1_Height;
    private int drawableSizeHip1_Width;
    private int height_Width;
    private PointF mappedCenterPoint;
    private int radius;
    private Rect realBounds;
    private int type;

    public BeautySticker(Context context, int type, Drawable drawable) {
        this.drawableSizeBoobs = SystemUtil.dpToPx(context, 50);
        this.drawableSizeHip1_Width = SystemUtil.dpToPx(context, 150);
        this.drawableSizeHip1_Height = SystemUtil.dpToPx(context, 75);
        this.drawableSizeFace_Height = SystemUtil.dpToPx(context, 50);
        this.drawableSizeFace_Width = SystemUtil.dpToPx(context, 80);
        this.type = type;
        this.drawable = drawable;
        this.realBounds = new Rect(0, 0, getWidth(), getHeight());
    }


    public void draw(@NonNull Canvas paramCanvas) {
        paramCanvas.save();
        paramCanvas.concat(getMatrix());
        this.drawable.setBounds(this.realBounds);
        this.drawable.draw(paramCanvas);
        paramCanvas.restore();
    }

    public int getAlpha() {
        return this.drawable.getAlpha();
    }

    @NonNull
    public Drawable getDrawable() {
        return null;
    }

    public int getHeight() {
        return (this.type == 1 || this.type == 0) ? this.drawableSizeBoobs : ((this.type == 2) ? this.drawableSizeHip1_Height : ((this.type == 4) ? this.drawableSizeFace_Height : ((this.type == 10 || this.type == 11) ? this.drawable.getIntrinsicHeight() : 0)));
    }

    @NonNull
    public PointF getMappedCenterPoint2() {
        return this.mappedCenterPoint;
    }

    public int getRadius() {
        return this.radius;
    }

    public int getType() {
        return this.type;
    }

    /**
     * 获取贴纸的宽度。
     *
     * @return 贴纸的宽度
     */
    public int getWidth() {
        if (this.type == 1 || this.type == 0) {
            return this.drawableSizeBoobs;
        } else if (this.type == 2) {
            return this.drawableSizeHip1_Width;
        } else if (this.type == 4) {
            return this.drawableSizeFace_Width;
        } else if (this.type == 10 || this.type == 11) {
            return this.height_Width;
        } else {
            return 0;
        }
    }

    public void release() {
        super.release();
        if (this.drawable != null)
            this.drawable = null;
    }

    @NonNull
    public BeautySticker setAlpha(@IntRange(from = 0L, to = 255L) int alpha) {
        this.drawable.setAlpha(alpha);
        return this;
    }

    public BeautySticker setDrawable(@NonNull Drawable drawable) {
        return this;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void updateRadius() {
        RectF rectF = getBound();
        if (this.type == 0 || this.type == 1 || this.type == 4) {
            this.radius = (int) (rectF.left + rectF.right);
        } else if (this.type == 2) {
            this.radius = (int) (rectF.top + rectF.bottom);
        }
        this.mappedCenterPoint = getMappedCenterPoint();
    }
}
