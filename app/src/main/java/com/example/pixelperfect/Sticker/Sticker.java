package com.example.pixelperfect.Sticker;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
/**
 * 贴纸基类
 */
public abstract class Sticker {
    /**
     * 坐标
     */
    private final float[] boundPoints = new float[8];

    private boolean isFlippedHorizontally;

    private boolean isFlippedVertically;

    private boolean isShow = true;

    private final float[] mappedBounds = new float[8];

    private final Matrix matrix = new Matrix();

    private final float[] matrixValues = new float[9];

    private final RectF trappedRect = new RectF();

    private final float[] unrotatedPoint = new float[2];

    private final float[] unrotatedWrapperCorner = new float[8];

    public boolean contains(float paramFloat1, float paramFloat2) {
        return contains(new float[]{paramFloat1, paramFloat2});
    }
    /**
     * 检查指定点是否在贴纸范围内。
     */
    public boolean contains(@NonNull float[] point) {
        Matrix matrix = new Matrix();
        matrix.setRotate(-getCurrentAngle());
        getBoundPoints(this.boundPoints);
        getMappedPoints(this.mappedBounds, this.boundPoints);
        matrix.mapPoints(this.unrotatedWrapperCorner, this.mappedBounds);
        matrix.mapPoints(this.unrotatedPoint, point);
        // 将点限制在矩形范围内
        StickerUtils.trapToRect(this.trappedRect, this.unrotatedWrapperCorner);
        // 检查指定点是否在矩形范围内
        return this.trappedRect.contains(this.unrotatedPoint[0], this.unrotatedPoint[1]);
    }

    public abstract void draw(@NonNull Canvas paramCanvas);

    public abstract int getAlpha();

    @NonNull
    public RectF getBound() {
        RectF rectF = new RectF();
        getBound(rectF);
        return rectF;
    }

    public void getBound(@NonNull RectF rectF) {
        rectF.set(0.0F, 0.0F, getWidth(), getHeight());
    }

    public void getBoundPoints(@NonNull float[] boundPoints) {
        if (!this.isFlippedHorizontally) {
            if (!this.isFlippedVertically) {
                boundPoints[0] = 0.0F;
                boundPoints[1] = 0.0F;//topLeft
                boundPoints[2] = getWidth(); //topRight
                boundPoints[3] = 0.0F;
                boundPoints[4] = 0.0F;//bottomLeft
                boundPoints[5] = getHeight();
                boundPoints[6] = getWidth();//bottomRight
                boundPoints[7] = getHeight();
                return;
            }
            boundPoints[0] = 0.0F;
            boundPoints[1] = getHeight();
            boundPoints[2] = getWidth();
            boundPoints[3] = getHeight();
            boundPoints[4] = 0.0F;
            boundPoints[5] = 0.0F;
            boundPoints[6] = getWidth();
            boundPoints[7] = 0.0F;
            return;
        }
        if (!this.isFlippedVertically) {
            boundPoints[0] = getWidth();
            boundPoints[1] = 0.0F;
            boundPoints[2] = 0.0F;
            boundPoints[3] = 0.0F;
            boundPoints[4] = getWidth();
            boundPoints[5] = getHeight();
            boundPoints[6] = 0.0F;
            boundPoints[7] = getHeight();
            return;
        }
        boundPoints[0] = getWidth();
        boundPoints[1] = getHeight();
        boundPoints[2] = 0.0F;
        boundPoints[3] = getHeight();
        boundPoints[4] = getWidth();
        boundPoints[5] = 0.0F;
        boundPoints[6] = 0.0F;
        boundPoints[7] = 0.0F;
    }

    public float[] getBoundPoints() {
        float[] arrayOfFloat = new float[8];
        getBoundPoints(arrayOfFloat);
        return arrayOfFloat;
    }

    @NonNull
    public PointF getCenterPoint() {
        PointF pointF = new PointF();
        getCenterPoint(pointF);
        return pointF;
    }

    public void getCenterPoint(@NonNull PointF pointF) {
        pointF.set(getWidth() * 1.0F / 2.0F, getHeight() * 1.0F / 2.0F);
    }

    public float getCurrentAngle() {
        return getMatrixAngle(this.matrix);
    }


    @NonNull
    public abstract Drawable getDrawable();

    public abstract int getHeight();

    @NonNull
    public RectF getMappedBound() {
        RectF rectF = new RectF();
        getMappedBound(rectF, getBound());
        return rectF;
    }

    /**
     * 将给定的边界矩形映射到变换后的边界矩形。
     *
     * @param mappedBound 变换后的边界矩形
     * @param originalBound 原始边界矩形
     */
    public void getMappedBound(@NonNull RectF mappedBound, @NonNull RectF originalBound) {
        this.matrix.mapRect(mappedBound, originalBound);
    }


    @NonNull
    public PointF getMappedCenterPoint() {
        PointF pointF = getCenterPoint();
        getMappedCenterPoint(pointF, new float[2], new float[2]);
        return pointF;
    }

    /**
     * 将给定点的中心点映射到变换后的位置。
     *
     * @param centerPoint  给定点的中心点
     * @param mappedBounds 映射后的边界点数组
     * @param boundPoints  原始边界点数组
     */
    public void getMappedCenterPoint(@NonNull PointF centerPoint, @NonNull float[] mappedBounds, @NonNull float[] boundPoints) {
        // 获取给定点的中心点
        getCenterPoint(centerPoint);

        // 将中心点的坐标放入原始边界点数组
        boundPoints[0] = centerPoint.x;
        boundPoints[1] = centerPoint.y;

        // 将原始边界点数组映射到变换后的位置
        getMappedPoints(mappedBounds, boundPoints);

        // 更新中心点的坐标为映射后的位置
        centerPoint.set(mappedBounds[0], mappedBounds[1]);
    }

    public void getMappedPoints(@NonNull float[] dstPoint, @NonNull float[] srcPoint) {
        this.matrix.mapPoints(dstPoint, srcPoint);
    }


    @NonNull
    public Matrix getMatrix() {
        return this.matrix;
    }

    public float getMatrixAngle(@NonNull Matrix paramMatrix) {
        return (float) Math.toDegrees(-Math.atan2(getMatrixValue(paramMatrix, 1), getMatrixValue(paramMatrix, 0)));
    }


    public float getMatrixValue(@NonNull Matrix paramMatrix, @IntRange(from = 0L, to = 9L) int paramInt) {
        paramMatrix.getValues(this.matrixValues);
        return this.matrixValues[paramInt];
    }

    public abstract int getWidth();

    public boolean isFlippedHorizontally() {
        return this.isFlippedHorizontally;
    }

    public boolean isFlippedVertically() {
        return this.isFlippedVertically;
    }

    public boolean isShow() {
        return this.isShow;
    }

    public void release() {
    }

    @NonNull
    public abstract Sticker setAlpha(@IntRange(from = 0L, to = 255L) int alpha);

    public abstract Sticker setDrawable(@NonNull Drawable drawable);

    @NonNull
    public Sticker setFlippedHorizontally(boolean isFlippedHorizontally) {
        this.isFlippedHorizontally = isFlippedHorizontally;
        return this;
    }

    @NonNull
    public Sticker setFlippedVertically(boolean isFlippedVertically) {
        this.isFlippedVertically = isFlippedVertically;
        return this;
    }

    public Sticker setMatrix(@Nullable Matrix matrix) {
        this.matrix.set(matrix);
        return this;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

}
