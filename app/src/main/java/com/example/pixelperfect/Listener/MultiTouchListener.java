package com.example.pixelperfect.Listener;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.pixelperfect.Vector.DHANVINE_Vector2D;
import com.example.pixelperfect.Vector.ScaleGestureDetector;


public class MultiTouchListener implements OnTouchListener {
    Boolean forspiral;
    public boolean isRotateEnabled;
    public boolean isScaleEnabled;
    public boolean isTranslateEnabled;
    private int mActivePointerId;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector;
    public float maximumScale;
    public float minimumScale;
    private Rect rect;
//    OnRotateListner rotateListner;
//
//    public interface OnRotateListner {
//        float getRotation(float f);
//    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private DHANVINE_Vector2D mPrevSpanVector;

        public void onScaleEnd(View view, ScaleGestureDetector dHANVINE_ScaleGestureDetector) {
        }

        private ScaleGestureListener() {
            this.mPrevSpanVector = new DHANVINE_Vector2D(0.0f, 0.0f);
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector dHANVINE_ScaleGestureDetector) {
            this.mPivotX = dHANVINE_ScaleGestureDetector.getFocusX();
            this.mPivotY = dHANVINE_ScaleGestureDetector.getFocusY();
            this.mPrevSpanVector.set(dHANVINE_ScaleGestureDetector.getCurrentSpanVector());
            return true;
        }

        /**
         * 处理缩放操作。
         *
         * @param view             执行缩放操作的视图
         * @param scaleGestureDetector 缩放手势检测器
         * @return 是否消耗事件
         */
        public boolean onScale(View view, ScaleGestureDetector scaleGestureDetector) {
            TransformInfo transformInfo = new TransformInfo();
            transformInfo.deltaScale = isScaleEnabled ? scaleGestureDetector.getScaleFactor() : 1.0f;
            float deltaX = 0.0f;
            float deltaY = 0.0f;
            if (isRotateEnabled) {
                transformInfo.deltaAngle = DHANVINE_Vector2D.getAngle(this.mPrevSpanVector, scaleGestureDetector.getCurrentSpanVector());
            } else {
                transformInfo.deltaAngle = 0.0f;
            }
            if (isTranslateEnabled) {
                deltaX = scaleGestureDetector.getFocusX() - this.mPivotX;
                deltaY = scaleGestureDetector.getFocusY() - this.mPivotY;
            }
            transformInfo.deltaX = deltaX;
            transformInfo.deltaY = deltaY;
            transformInfo.pivotX = this.mPivotX;
            transformInfo.pivotY = this.mPivotY;
            transformInfo.minimumScale = minimumScale;
            transformInfo.maximumScale = maximumScale;
            move(view, transformInfo);
            return false;
        }
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    private static float adjustAngle(float f) {
        return f > 180.0f ? f - 360.0f : f < -180.0f ? f + 360.0f : f;
    }

    public MultiTouchListener(Activity activity , Boolean forspiral) {
        this.mActivity = activity;
        this.forspiral = false;
        this.forspiral = forspiral;
        this.isRotateEnabled = true;
        this.isTranslateEnabled = true;
        this.isScaleEnabled = true;
        this.maximumScale = 10.0f;
        this.mActivePointerId = -1;
        this.mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
    }
    Activity mActivity ;

    public void move(View view, TransformInfo transformInfo) {
        computeRenderOffset(view, transformInfo.pivotX, transformInfo.pivotY);
        adjustTranslation(view, transformInfo.deltaX, transformInfo.deltaY);
        float max = Math.max(transformInfo.minimumScale, Math.min(transformInfo.maximumScale, view.getScaleX() * transformInfo.deltaScale));
        view.setScaleX(max);
        view.setScaleY(max);
        view.setRotation(adjustAngle(view.getRotation() + transformInfo.deltaAngle));
    }

    private static void adjustTranslation(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }

    private static void computeRenderOffset(View view, float f, float f2) {
        if (view.getPivotX() != f || view.getPivotY() != f2) {
            float[] fArr = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr);
            view.setPivotX(f);
            view.setPivotY(f2);
            float[] fArr2 = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr2);
            float f3 = fArr2[1] - fArr[1];
            view.setTranslationX(view.getTranslationX() - (fArr2[0] - fArr[0]));
            view.setTranslationY(view.getTranslationY() - f3);
        }
    }

    /**
     * 处理触摸事件。
     *
     * @param view         执行触摸操作的视图
     * @param motionEvent  触摸事件对象
     * @return 是否消耗事件
     */
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // 处理缩放手势
        mScaleGestureDetector.onTouchEvent(view, motionEvent);

        // 如果启用了平移功能
        if (isTranslateEnabled) {
            // 获取触摸事件的动作类型
            int action = motionEvent.getAction();
            int actionMasked = motionEvent.getActionMasked() & action;

            // 根据不同的动作类型进行处理
            switch (actionMasked) {
                case MotionEvent.ACTION_DOWN:
                    // 记录按下时的位置和视图的边界矩形
                    mPrevX = motionEvent.getX();
                    mPrevY = motionEvent.getY();
                    rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    mActivePointerId = motionEvent.getPointerId(0);
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    // 处理多点触控的情况
                    int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    int pointerId = motionEvent.getPointerId(pointerIndex);
                    if (pointerId == mActivePointerId) {
                        int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mPrevX = motionEvent.getX(newPointerIndex);
                        mPrevY = motionEvent.getY(newPointerIndex);
                        mActivePointerId = motionEvent.getPointerId(newPointerIndex);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    // 处理移动事件
                    int pointerIndexMove = motionEvent.findPointerIndex(mActivePointerId);
                    if (pointerIndexMove != -1) {
                        float x = motionEvent.getX(pointerIndexMove);
                        float y = motionEvent.getY(pointerIndexMove);
                        if (!mScaleGestureDetector.isInProgress()) {
                            adjustTranslation(view, x - mPrevX, y - mPrevY);
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // 清除活动指针ID
                    mActivePointerId = -1;
                    break;
            }
        }
        return true;
    }

    public boolean onTouch2(View view, MotionEvent motionEvent) {
        this.mScaleGestureDetector.onTouchEvent(view, motionEvent);
        if (this.isTranslateEnabled) {
            int action = motionEvent.getAction();
            int actionMasked = motionEvent.getActionMasked() & action;
            int i = 0;
            if (actionMasked == 6) {
                int i2 = (65280 & action) >> 8;
                if (motionEvent.getPointerId(i2) == this.mActivePointerId) {
                    if (i2 == 0) {
                        i = 1;
                    }
                    this.mPrevX = motionEvent.getX(i);
                    this.mPrevY = motionEvent.getY(i);
                    this.mActivePointerId = motionEvent.getPointerId(i);
                }
            } else if (actionMasked == 0) {
                this.mPrevX = motionEvent.getX();
                this.mPrevY = motionEvent.getY();
                this.rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                this.mActivePointerId = motionEvent.getPointerId(0);
            } else if (actionMasked == 1) {
                this.mActivePointerId = -1;
            } else if (actionMasked == 2) {
                int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                if (findPointerIndex != -1) {
                    float x = motionEvent.getX(findPointerIndex);
                    float y = motionEvent.getY(findPointerIndex);
                    if (!this.mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, x - this.mPrevX, y - this.mPrevY);
                    }
                }
            } else if (actionMasked == 3) {
                this.mActivePointerId = -1;
            }
        }
        return true;
    }
}
