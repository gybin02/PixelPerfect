package com.example.pixelperfect.Event;

import android.view.MotionEvent;

import com.example.pixelperfect.Sticker.StickerView;

/**
 * 贴纸图标事件接口，定义了触摸事件的回调方法。
 */
public interface StickerIconEvent {
    /**
     * 当手指按下贴纸图标时调用。
     *
     * @param stickerView 贴纸视图
     * @param event       触摸事件对象
     */
    void onActionDown(StickerView stickerView, MotionEvent event);

    /**
     * 当手指在贴纸图标上移动时调用。
     *
     * @param stickerView 贴纸视图
     * @param event       触摸事件对象
     */
    void onActionMove(StickerView stickerView, MotionEvent event);

    /**
     * 当手指抬起贴纸图标时调用。
     *
     * @param stickerView 贴纸视图
     * @param event       触摸事件对象
     */
    void onActionUp(StickerView stickerView, MotionEvent event);
}
