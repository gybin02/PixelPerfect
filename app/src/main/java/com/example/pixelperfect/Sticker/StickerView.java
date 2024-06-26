package com.example.pixelperfect.Sticker;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.example.pixelperfect.Editor.PTextView;
import com.example.pixelperfect.Event.DeleteIconEvent;
import com.example.pixelperfect.Event.FlipHorizontallyEvent;
import com.example.pixelperfect.Event.ZoomIconEvent;
import com.example.pixelperfect.R;
import com.example.pixelperfect.Utils.SystemUtil;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * 支持贴纸控件布局
 */
public class StickerView extends RelativeLayout {
    private static final int DEFAULT_MIN_CLICK_DELAY_TIME = 200;
    public static final int FLIP_HORIZONTALLY = 1;
    public static final int FLIP_VERTICALLY = 2;
    private static final String TAG = "StickerView";
    private final float[] bitmapPoints; //贴纸位置坐标数据
    private final Paint borderPaint;
    private final Paint borderPaintRed;
    private final float[] bounds;
    private boolean bringToFrontCurrentSticker;
    private int circleRadius;
    private boolean constrained;
    private final PointF currentCenterPoint;
    private BitmapStickerIcon currentIcon;
    /**
     * @see ActionMode
     */
    private int currentMode;
    private float currentMoveingX;
    private float currentMoveingY;
    private final Matrix downMatrix;
    private float downX;
    private float downY;
    private boolean drawCirclePoint;
    private Sticker handlingSticker;
    private final List<BitmapStickerIcon> icons;
    private long lastClickTime;
    private Sticker lastHandlingSticker;
    private final Paint linePaint;
    private boolean locked;
    private PointF midPoint;
    private int minClickDelayTime;
    private final Matrix moveMatrix;
    private float oldDistance;
    private float oldRotation;
    private boolean onMoving;
    private OnStickerOperationListener onStickerOperationListener;
    private Paint paintCircle;
    private final float[] point;
    private boolean showBorder;
    private boolean showIcons;
    private final Matrix sizeMatrix;
    private final RectF stickerRect;
    private final List<Sticker> stickers;//贴纸列表
    private final float[] tmp;
    private int touchSlop;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionMode {
        public static final int CLICK = 4;
        public static final int DRAG = 1;
        public static final int ICON = 3;
        public static final int NONE = 0;
        public static final int ZOOM_WITH_TWO_FINGER = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flip {
    }

    public interface OnStickerOperationListener {
        void onStickerAdded(@NonNull Sticker sticker);

        void onStickerClicked(@NonNull Sticker sticker);

        void onStickerDeleted(@NonNull Sticker sticker);

        void onStickerDoubleTapped(@NonNull Sticker sticker);

        void onStickerDragFinished(@NonNull Sticker sticker);

        void onStickerFlipped(@NonNull Sticker sticker);

        void onStickerTouchOutside();

        void onStickerTouchedDown(@NonNull Sticker sticker);

        void onStickerZoomFinished(@NonNull Sticker sticker);

        void onTouchDownForBeauty(float f, float f2);

        void onTouchDragForBeauty(float f, float f2);

        void onTouchUpForBeauty(float f, float f2);
    }

    public StickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }


    @SuppressLint("ResourceType")
    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArray;
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        this.borderPaint = new Paint();
        this.borderPaintRed = new Paint();
        this.linePaint = new Paint();
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.drawCirclePoint = false;
        this.onMoving = false;
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode = ActionMode.NONE;
        this.lastClickTime = 0;
        this.minClickDelayTime = 200;
        this.paintCircle = new Paint();
        this.paintCircle.setAntiAlias(true);
        this.paintCircle.setDither(true);
        this.paintCircle.setColor(getContext().getResources().getColor(R.color.mainColor));
        this.paintCircle.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.paintCircle.setStyle(Paint.Style.STROKE);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            int[] StickerView = {R.attr.borderAlpha, R.attr.borderColor, R.attr.bringToFrontCurrentSticker, R.attr.showBorder, R.attr.showIcons};
            typedArray = context.obtainStyledAttributes(attributeSet, StickerView);
            try {
                this.showIcons = typedArray.getBoolean(4, false);
                this.showBorder = typedArray.getBoolean(3, false);
                this.bringToFrontCurrentSticker = typedArray.getBoolean(2, false);
                this.borderPaint.setAntiAlias(true);
                this.borderPaint.setColor(typedArray.getColor(1, Color.parseColor("#03A9F4")));
                this.borderPaint.setAlpha(typedArray.getInteger(0, 255));
                this.borderPaintRed.setAntiAlias(true);
                this.borderPaintRed.setColor(typedArray.getColor(1, Color.parseColor("#03A9F4")));
                this.borderPaintRed.setAlpha(typedArray.getInteger(0, 255));
                configDefaultIcons();
                if (typedArray != null) {
                    typedArray.recycle();
                }
            } catch (Throwable th) {
                th = th;
                if (typedArray != null) {
                    typedArray.recycle();
                }
                throw th;
            }
        } catch (Throwable th2) {

            typedArray = null;
            if (typedArray != null) {
            }

        }
    }

    @RequiresApi(api = 21)
    public StickerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        this.borderPaint = new Paint();
        this.borderPaintRed = new Paint();
        this.linePaint = new Paint();
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.drawCirclePoint = false;
        this.onMoving = false;
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode =ActionMode.NONE;
        this.lastClickTime = 0;
        this.minClickDelayTime = 200;
    }

    public Matrix getSizeMatrix() {
        return this.sizeMatrix;
    }

    public Matrix getDownMatrix() {
        return this.downMatrix;
    }

    public Matrix getMoveMatrix() {
        return this.moveMatrix;
    }

    public List<Sticker> getStickers() {
        return this.stickers;
    }

    public void configDefaultIcons() {
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_close), 0, BitmapStickerIcon.REMOVE);
        bitmapStickerIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_scale), 3, BitmapStickerIcon.ZOOM);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon3 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_flip), 1, BitmapStickerIcon.FLIP);
        bitmapStickerIcon3.setIconEvent(new FlipHorizontallyEvent());
        BitmapStickerIcon bitmapStickerIcon4 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_edit), 2, BitmapStickerIcon.EDIT);
        bitmapStickerIcon4.setIconEvent(new FlipHorizontallyEvent());
        this.icons.clear();
        this.icons.add(bitmapStickerIcon);
        this.icons.add(bitmapStickerIcon2);
        this.icons.add(bitmapStickerIcon3);
        this.icons.add(bitmapStickerIcon4);
    }
    /**
     * 图层交换
     */
    public void swapLayers(int before, int after) {
        if (this.stickers.size() >= before && this.stickers.size() >= after) {
            Collections.swap(this.stickers, before, after);
            invalidate();
        }
    }
    /**
     * 选中的贴纸
     */
    public void setHandlingSticker(Sticker sticker) {
        this.lastHandlingSticker = this.handlingSticker;
        this.handlingSticker = sticker;
        invalidate();
    }

    public void showLastHandlingSticker() {
        if (this.lastHandlingSticker != null && !this.lastHandlingSticker.isShow()) {
            this.lastHandlingSticker.setShow(true);
            invalidate();
        }
    }

    public void sendToLayer(int old, int newPos) {
        if (this.stickers.size() >= old && this.stickers.size() >= newPos) {
            this.stickers.remove(old);
            this.stickers.add(newPos, this.stickers.get(old));
            invalidate();
        }
    }


    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            this.stickerRect.left = (float) left;
            this.stickerRect.top = (float) top;
            this.stickerRect.right = (float) right;
            this.stickerRect.bottom = (float) bottom;
        }
    }


    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.drawCirclePoint && this.onMoving) {
            canvas.drawCircle(this.downX, this.downY, (float) this.circleRadius, this.paintCircle);
            canvas.drawLine(this.downX, this.downY, this.currentMoveingX, this.currentMoveingY, this.paintCircle);
        }
        drawStickers(canvas);
    }

    public void setCircleRadius(int i) {
        this.circleRadius = i;
    }

    public void drawStickers(Canvas canvas) {
        float bottomLeftX;
        float bottomLeftY;
        float bottomRightX;
        float bottomRightY;
        Canvas canvas2 = canvas;
        int i = 0;
        for (int i2 = 0; i2 < this.stickers.size(); i2++) {
            Sticker sticker = this.stickers.get(i2);
            if (sticker != null && sticker.isShow()) {
                sticker.draw(canvas2);
            }
        }
        if (this.handlingSticker != null && !this.locked && (this.showBorder || this.showIcons)) {
            getStickerPoints(this.handlingSticker, this.bitmapPoints);
            float f5 = this.bitmapPoints[0];
            int i3 = 1;
            float f6 = this.bitmapPoints[1];
            float stopX = this.bitmapPoints[2];
            float stopY = this.bitmapPoints[3];
            float f9 = this.bitmapPoints[4];
            float f10 = this.bitmapPoints[5];
            float f11 = this.bitmapPoints[6];
            float f12 = this.bitmapPoints[7];
            if (this.showBorder) {
                Canvas canvas3 = canvas;
                float startX = f5;
                bottomRightY = f12;
                float startY = f6;
                bottomRightX = f11;
                bottomLeftY = f10;
                bottomLeftX = f9;
                canvas3.drawLine(startX, startY, stopX, stopY, this.borderPaint);
                canvas3.drawLine(startX, startY, bottomLeftX, bottomLeftY, this.borderPaint);
                canvas3.drawLine(stopX, stopY, bottomRightX, bottomRightY, this.borderPaint);
                canvas3.drawLine(bottomRightX, bottomRightY, bottomLeftX, bottomLeftY, this.borderPaint);
            } else {
                bottomRightY = f12;
                bottomRightX = f11;
                bottomLeftY = f10;
                bottomLeftX = f9;
            }
            if (this.showIcons) {
                float f15 = bottomRightY;
                float f16 = bottomRightX;
                float f17 = bottomLeftY;
                float f18 = bottomLeftX;
                float calculateRotation = calculateRotation(f16, f15, f18, f17);
                while (i < this.icons.size()) {
                    BitmapStickerIcon bitmapStickerIcon = this.icons.get(i);
                    switch (bitmapStickerIcon.getPosition()) {
                        case 0:
                            configIconMatrix(bitmapStickerIcon, f5, f6, calculateRotation);
                            bitmapStickerIcon.draw(canvas2, this.borderPaintRed);
                            break;
                        case 1:
                            if (((this.handlingSticker instanceof PTextView) && bitmapStickerIcon.getTag().equals(BitmapStickerIcon.EDIT)) || ((this.handlingSticker instanceof DrawableSticker) && bitmapStickerIcon.getTag().equals(BitmapStickerIcon.FLIP))) {
                                configIconMatrix(bitmapStickerIcon, stopX, stopY, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                break;
                            }
                        case 2:
                            if (this.handlingSticker instanceof BeautySticker) {
                                if (((BeautySticker) this.handlingSticker).getType() != 0) {
                                    break;
                                } else {
                                    configIconMatrix(bitmapStickerIcon, f18, f17, calculateRotation);
                                    bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                    break;
                                }
                            } else {
                                configIconMatrix(bitmapStickerIcon, f18, f17, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                break;
                            }
                        case 3:
                            if ((!(this.handlingSticker instanceof PTextView) || !bitmapStickerIcon.getTag().equals(BitmapStickerIcon.ROTATE)) && (!(this.handlingSticker instanceof DrawableSticker) || !bitmapStickerIcon.getTag().equals(BitmapStickerIcon.ZOOM))) {
                                if (this.handlingSticker instanceof BeautySticker) {
                                    BeautySticker beautySticker = (BeautySticker) this.handlingSticker;
                                    if (beautySticker.getType() != i3) {
                                        if (beautySticker.getType() != 2 && beautySticker.getType() != 8) {
                                            if (beautySticker.getType() != 4) {
                                                break;
                                            }
                                        }
                                        configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                                        bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                        break;
                                    } else {
                                        configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                                        bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                    }
                                }
                            } else {
                                configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, this.borderPaint);
                            }
                            break;
                    }
                    i++;
                    i3 = 1;
                }
            }
        }
        invalidate();
    }


    public void configIconMatrix(@NonNull BitmapStickerIcon bitmapStickerIcon, float f, float f2, float f3) {
        bitmapStickerIcon.setX(f);
        bitmapStickerIcon.setY(f2);
        bitmapStickerIcon.getMatrix().reset();
        bitmapStickerIcon.getMatrix().postRotate(f3, (float) (bitmapStickerIcon.getWidth() / 2), (float) (bitmapStickerIcon.getHeight() / 2));
        bitmapStickerIcon.getMatrix().postTranslate(f - ((float) (bitmapStickerIcon.getWidth() / 2)), f2 - ((float) (bitmapStickerIcon.getHeight() / 2)));
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.locked) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() != 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        return (findCurrentIconTouched() == null && findHandlingSticker() == null) ? false : true;
    }

    public void setDrawCirclePoint(boolean z) {
        this.drawCirclePoint = z;
        this.onMoving = false;
    }

    /**
     * 处理触摸事件。
     *
     * @param motionEvent 触摸事件对象
     * @return 是否消耗事件
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (locked) {
            return super.onTouchEvent(motionEvent);
        }

        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case MotionEvent.ACTION_DOWN:
                if (!onTouchDown(motionEvent)) {
                    if (onStickerOperationListener != null) {
                        onStickerOperationListener.onStickerTouchOutside();
                        invalidate();
                        if (!drawCirclePoint) {
                            return false;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                onTouchUp(motionEvent);
                break;

            case MotionEvent.ACTION_MOVE:
                handleCurrentMode(motionEvent);
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDistance = calculateDistance(motionEvent);
                oldRotation = calculateRotation(motionEvent);
                midPoint = calculateMidPoint(motionEvent);
                if (handlingSticker != null && isInStickerArea(handlingSticker, motionEvent.getX(1), motionEvent.getY(1)) && findCurrentIconTouched() == null) {
                    currentMode = ActionMode.ZOOM_WITH_TWO_FINGER;
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (currentMode == ActionMode.ZOOM_WITH_TWO_FINGER && handlingSticker != null && onStickerOperationListener != null) {
                    onStickerOperationListener.onStickerZoomFinished(handlingSticker);
                }
                currentMode =ActionMode.NONE;
                break;
        }
        return true;
    }



    public boolean onTouchDown(@NonNull MotionEvent motionEvent) {
        this.currentMode = ActionMode.DRAG;
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        this.onMoving = true;
        this.currentMoveingX = motionEvent.getX();
        this.currentMoveingY = motionEvent.getY();
        this.midPoint = calculateMidPoint();
        this.oldDistance = calculateDistance(this.midPoint.x, this.midPoint.y, this.downX, this.downY);
        this.oldRotation = calculateRotation(this.midPoint.x, this.midPoint.y, this.downX, this.downY);
        this.currentIcon = findCurrentIconTouched();
        if (this.currentIcon != null) {
            this.currentMode = ActionMode.ICON;
            this.currentIcon.onActionDown(this, motionEvent);
        } else {
            this.handlingSticker = findHandlingSticker();
        }
        if (this.handlingSticker != null) {
            this.downMatrix.set(this.handlingSticker.getMatrix());
            if (this.bringToFrontCurrentSticker) {
                this.stickers.remove(this.handlingSticker);
                this.stickers.add(this.handlingSticker);
            }
            if (this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerTouchedDown(this.handlingSticker);
            }
        }
        if (this.drawCirclePoint) {
            this.onStickerOperationListener.onTouchDownForBeauty(this.currentMoveingX, this.currentMoveingY);
            invalidate();
            return true;
        } else if (this.currentIcon == null && this.handlingSticker == null) {
            return false;
        } else {
            invalidate();
            return true;
        }
    }


    public void onTouchUp(@NonNull MotionEvent motionEvent) {
        long uptimeMillis = SystemClock.uptimeMillis();
        this.onMoving = false;
        if (this.drawCirclePoint) {
            this.onStickerOperationListener.onTouchUpForBeauty(motionEvent.getX(), motionEvent.getY());
        }
        if (!(this.currentMode != ActionMode.ICON || this.currentIcon == null || this.handlingSticker == null)) {
            this.currentIcon.onActionUp(this, motionEvent);
        }
        if (this.currentMode == ActionMode.DRAG && Math.abs(motionEvent.getX() - this.downX) < ((float) this.touchSlop) && Math.abs(motionEvent.getY() - this.downY) < ((float) this.touchSlop) && this.handlingSticker != null) {
            this.currentMode = ActionMode.CLICK;
            if (this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerClicked(this.handlingSticker);
            }
            if (uptimeMillis - this.lastClickTime < ((long) this.minClickDelayTime) && this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerDoubleTapped(this.handlingSticker);
            }
        }
        if (!(this.currentMode != ActionMode.DRAG || this.handlingSticker == null || this.onStickerOperationListener == null)) {
            this.onStickerOperationListener.onStickerDragFinished(this.handlingSticker);
        }
        this.currentMode =ActionMode.NONE;
        this.lastClickTime = uptimeMillis;
    }


    public void handleCurrentMode(@NonNull MotionEvent motionEvent) {
        switch (this.currentMode) {
            case ActionMode.DRAG:
                this.currentMoveingX = motionEvent.getX();
                this.currentMoveingY = motionEvent.getY();
                if (this.drawCirclePoint) {
                    this.onStickerOperationListener.onTouchDragForBeauty(this.currentMoveingX, this.currentMoveingY);
                }
                if (this.handlingSticker != null) {
                    this.moveMatrix.set(this.downMatrix);
                    if (this.handlingSticker instanceof BeautySticker) {
                        BeautySticker beautySticker = (BeautySticker) this.handlingSticker;
                        if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                            this.moveMatrix.postTranslate(0.0f, motionEvent.getY() - this.downY);
                        } else {
                            this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                        }
                    } else {
                        this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                    }
                    this.handlingSticker.setMatrix(this.moveMatrix);
                    if (this.constrained) {
                        constrainSticker(this.handlingSticker);
                        return;
                    }
                    return;
                }
                return;
            case ActionMode.ZOOM_WITH_TWO_FINGER:
                if (this.handlingSticker != null) {
                    float calculateDistance = calculateDistance(motionEvent);
                    float calculateRotation = calculateRotation(motionEvent);
                    this.moveMatrix.set(this.downMatrix);
                    this.moveMatrix.postScale(calculateDistance / this.oldDistance, calculateDistance / this.oldDistance, this.midPoint.x, this.midPoint.y);
                    this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
                    this.handlingSticker.setMatrix(this.moveMatrix);
                    return;
                }
                return;
            case ActionMode.ICON:
                if (this.handlingSticker != null && this.currentIcon != null) {
                    this.currentIcon.onActionMove(this, motionEvent);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void zoomAndRotateCurrentSticker(@NonNull MotionEvent motionEvent) {
        zoomAndRotateSticker(this.handlingSticker, motionEvent);
    }

    public void alignHorizontally() {
        this.moveMatrix.set(this.downMatrix);
        this.moveMatrix.postRotate(-getCurrentSticker().getCurrentAngle(), this.midPoint.x, this.midPoint.y);
        this.handlingSticker.setMatrix(this.moveMatrix);
    }

    /**
     * 缩放和旋转贴纸。
     *
     * @param sticker 要缩放和旋转的贴纸对象
     * @param motionEvent 缩放和旋转操作的触摸事件
     */
    public void zoomAndRotateSticker(@Nullable Sticker sticker, @NonNull MotionEvent motionEvent) {
        if (sticker != null) {
            // 判断贴纸是否为 BeautySticker 类型，并且类型为 10 或 11，则不进行缩放和旋转操作
            if (sticker instanceof BeautySticker) {
                BeautySticker beautySticker = (BeautySticker) sticker;
                if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                    return;
                }
            }

            // 计算触摸事件和中心点之间的距离
            float distance;
            if (sticker instanceof PTextView) {
                // 如果是 PTextView 贴纸，则使用保存的旧距离
                distance = this.oldDistance;
            } else {
                // 否则，计算触摸事件和中心点之间的距离
                distance = calculateDistance(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            }

            // 计算旋转角度
            float rotation = calculateRotation(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());

            // 设置移动矩阵
            this.moveMatrix.set(this.downMatrix);
            this.moveMatrix.postScale(distance / this.oldDistance, distance / this.oldDistance, this.midPoint.x, this.midPoint.y);

            // 如果贴纸不是 BeautySticker 类型，则进行旋转操作
            if (!(sticker instanceof BeautySticker)) {
                this.moveMatrix.postRotate(rotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
            }

            // 设置贴纸的变换矩阵
            this.handlingSticker.setMatrix(this.moveMatrix);
        }
    }


    public void constrainSticker(@NonNull Sticker sticker) {
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(this.currentCenterPoint, this.point, this.tmp);
        float dy = 0.0f;
        float dx = this.currentCenterPoint.x < 0.0f ? -this.currentCenterPoint.x : 0.0f;
        float widthF = (float) width;
        if (this.currentCenterPoint.x > widthF) {
            dx = widthF - this.currentCenterPoint.x;
        }
        if (this.currentCenterPoint.y < 0.0f) {
            dy = -this.currentCenterPoint.y;
        }
        float heightF = (float) height;
        if (this.currentCenterPoint.y > heightF) {
            dy = heightF - this.currentCenterPoint.y;
        }
        sticker.getMatrix().postTranslate(dx, dy);
    }


    @Nullable
    public BitmapStickerIcon findCurrentIconTouched() {
        for (BitmapStickerIcon next : this.icons) {
            float x = next.getX() - this.downX;
            float y = next.getY() - this.downY;
            if (((double) ((x * x) + (y * y))) <= Math.pow((double) (next.getIconRadius() + next.getIconRadius()), 2.0d)) {
                return next;
            }
        }
        return null;
    }


    @Nullable
    public Sticker findHandlingSticker() {
        for (int size = this.stickers.size() - 1; size >= 0; size--) {
            if (isInStickerArea(this.stickers.get(size), this.downX, this.downY)) {
                return this.stickers.get(size);
            }
        }
        return null;
    }


    public boolean isInStickerArea(@NonNull Sticker sticker, float f, float f2) {
        this.tmp[0] = f;
        this.tmp[1] = f2;
        return sticker.contains(this.tmp);
    }


    @NonNull
    public PointF calculateMidPoint(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        this.midPoint.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
        return this.midPoint;
    }


    @NonNull
    public PointF calculateMidPoint() {
        if (this.handlingSticker == null) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        this.handlingSticker.getMappedCenterPoint(this.midPoint, this.point, this.tmp);
        return this.midPoint;
    }


    public float calculateRotation(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateRotation(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }


    public float calculateRotation(float f, float f2, float f3, float f4) {
        return (float) Math.toDegrees(Math.atan2((double) (f2 - f4), (double) (f - f3)));
    }


    public float calculateDistance(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }


    public float calculateDistance(float f, float f2, float f3, float f4) {
        double d = (double) (f - f3);
        double d2 = (double) (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }


    public void transformSticker(@Nullable Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }
        this.sizeMatrix.reset();
        float width = (float) getWidth();
        float height = (float) getHeight();
        float width2 = (float) sticker.getWidth();
        float height2 = (float) sticker.getHeight();
        this.sizeMatrix.postTranslate((width - width2) / 2.0f, (height - height2) / 2.0f);
        float sx = (width < height ? width / width2 : height / height2) / 2.0f;
        this.sizeMatrix.postScale(sx, sx, width / 2.0f, height / 2.0f);
        sticker.getMatrix().reset();
        sticker.setMatrix(this.sizeMatrix);
        invalidate();
    }


    public void onSizeChanged(int w, int h, int oldW, int oldY) {
        super.onSizeChanged(w, h, oldW, oldY);
        for (int i = 0; i < this.stickers.size(); i++) {
            Sticker sticker = this.stickers.get(i);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    public void flipCurrentSticker(int i) {
        flip(this.handlingSticker, i);
    }

    public void flip(@Nullable Sticker sticker, int index) {
        if (sticker != null) {
            sticker.getCenterPoint(this.midPoint);
            if ((index & 1) > 0) {
                sticker.getMatrix().preScale(-1.0f, 1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((index & 2) > 0) {
                sticker.getMatrix().preScale(1.0f, -1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }
            if (this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerFlipped(sticker);
            }
            invalidate();
        }
    }

    public boolean replace(@Nullable Sticker sticker) {
        return replace(sticker, true);
    }

    public Sticker getLastHandlingSticker() {
        return this.lastHandlingSticker;
    }

    public boolean replace(@Nullable Sticker sticker, boolean reset) {
        float f;
        if (this.handlingSticker == null) {
            this.handlingSticker = this.lastHandlingSticker;
        }
        if (this.handlingSticker == null || sticker == null) {
            return false;
        }
        float width = (float) getWidth();
        float height = (float) getHeight();
        if (reset) {
            sticker.setMatrix(this.handlingSticker.getMatrix());
            sticker.setFlippedVertically(this.handlingSticker.isFlippedVertically());
            sticker.setFlippedHorizontally(this.handlingSticker.isFlippedHorizontally());
        } else {
            this.handlingSticker.getMatrix().reset();
            sticker.getMatrix().postTranslate((width - ((float) this.handlingSticker.getWidth())) / 2.0f, (height - ((float) this.handlingSticker.getHeight())) / 2.0f);
            if (width < height) {
                if (this.handlingSticker instanceof PTextView) {
                    f = width / ((float) this.handlingSticker.getWidth());
                } else {
                    f = width / ((float) this.handlingSticker.getDrawable().getIntrinsicWidth());
                }
            } else if (this.handlingSticker instanceof PTextView) {
                f = height / ((float) this.handlingSticker.getHeight());
            } else {
                f = height / ((float) this.handlingSticker.getDrawable().getIntrinsicHeight());
            }
            float f2 = f / 2.0f;
            sticker.getMatrix().postScale(f2, f2, width / 2.0f, height / 2.0f);
        }
        this.stickers.set(this.stickers.indexOf(this.handlingSticker), sticker);
        this.handlingSticker = sticker;
        invalidate();
        return true;
    }

    public boolean remove(@Nullable Sticker sticker) {
        if (this.stickers.contains(sticker)) {
            this.stickers.remove(sticker);
            if (this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerDeleted(sticker);
            }
            if (this.handlingSticker == sticker) {
                this.handlingSticker = null;
            }
            invalidate();
            return true;
        }
        Log.d(TAG, "remove: the stickers is not in this StickerView");
        return false;
    }

    public boolean removeCurrentSticker() {
        return remove(this.handlingSticker);
    }

    public void removeAllStickers() {
        this.stickers.clear();
        if (this.handlingSticker != null) {
            this.handlingSticker.release();
            this.handlingSticker = null;
        }
        invalidate();
    }

    @NonNull
    public StickerView addSticker(@NonNull Sticker sticker) {
        return addSticker(sticker, 1);
    }

    public StickerView addSticker(@NonNull final Sticker sticker, final int index) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, index);
        } else {
            post(new Runnable() {
                public void run() {
                    StickerView.this.addStickerImmediately(sticker, index);
                }
            });
        }
        return this;
    }


    public void addStickerImmediately(@NonNull Sticker sticker, int index) {
        setStickerPosition(sticker, index);
        sticker.getMatrix().postScale(1.0f, 1.0f, (float) getWidth(), (float) getHeight());
        this.handlingSticker = sticker;
        this.stickers.add(sticker);
        if (this.onStickerOperationListener != null) {
            this.onStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();
    }

    /**
     * 设置贴纸位置
     */
    public void setStickerPosition(@NonNull Sticker sticker, int index) {
        float dy;
        float width = ((float) getWidth()) - ((float) sticker.getWidth());
        float height = ((float) getHeight()) - ((float) sticker.getHeight());
        if (sticker instanceof BeautySticker) {
            BeautySticker beautySticker = (BeautySticker) sticker;
            dy = height / 2.0f;
            if (beautySticker.getType() == 0) {
                width /= 3.0f;
            } else if (beautySticker.getType() == 1) {
                width = (width * 2.0f) / 3.0f;
            } else if (beautySticker.getType() == 2) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 4) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 10) {
                width /= 2.0f;
                dy = (dy * 2.0f) / 3.0f;
            } else if (beautySticker.getType() == 11) {
                width /= 2.0f;
                dy = (dy * 3.0f) / 2.0f;
            }
        } else {
            dy = (index & 2) > 0 ? height / 4.0f : (index & 16) > 0 ? height * 0.75f : height / 2.0f;
            width = (index & 4) > 0 ? width / 4.0f : (index & 8) > 0 ? width * 0.75f : width / 2.0f;
        }
        sticker.getMatrix().postTranslate(width, dy);
    }

    public void editTextSticker() {
        this.onStickerOperationListener.onStickerDoubleTapped(this.handlingSticker);
    }

    @NonNull
    public float[] getStickerPoints(@Nullable Sticker sticker) {
        float[] fArr = new float[8];
        getStickerPoints(sticker, fArr);
        return fArr;
    }

    public void getStickerPoints(@Nullable Sticker sticker, @NonNull float[] fArr) {
        if (sticker == null) {
            Arrays.fill(fArr, 0.0f);
            return;
        }
        sticker.getBoundPoints(this.bounds);
        sticker.getMappedPoints(fArr, this.bounds);
    }

    public void save(@NonNull File file) {
        try {
            StickerUtils.saveImageToGallery(file, createBitmap());
            StickerUtils.notifySystemGallery(getContext(), file);
        } catch (IllegalArgumentException | IllegalStateException unused) {
        }
    }

    @NonNull
    public Bitmap createBitmap() throws OutOfMemoryError {
        this.handlingSticker = null;
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public int getStickerCount() {
        return this.stickers.size();
    }

    public boolean isNoneSticker() {
        return getStickerCount() == 0;
    }

    @NonNull
    public StickerView setLocked(boolean locked) {
        this.locked = locked;
        invalidate();
        return this;
    }

    @NonNull
    public StickerView setMinClickDelayTime(int minClickDelayTime) {
        this.minClickDelayTime = minClickDelayTime;
        return this;
    }

    public int getMinClickDelayTime() {
        return this.minClickDelayTime;
    }

    public boolean isConstrained() {
        return this.constrained;
    }

    @NonNull
    public StickerView setConstrained(boolean constrained) {
        this.constrained = constrained;
        postInvalidate();
        return this;
    }

    @NonNull
    public StickerView setOnStickerOperationListener(@Nullable OnStickerOperationListener onStickerOperationListener2) {
        this.onStickerOperationListener = onStickerOperationListener2;
        return this;
    }

    @Nullable
    public OnStickerOperationListener getOnStickerOperationListener() {
        return this.onStickerOperationListener;
    }

    @Nullable
    public Sticker getCurrentSticker() {
        return this.handlingSticker;
    }

    @NonNull
    public List<BitmapStickerIcon> getIcons() {
        return this.icons;
    }

    public void setIcons(@NonNull List<BitmapStickerIcon> list) {
        this.icons.clear();
        this.icons.addAll(list);
        invalidate();
    }
}