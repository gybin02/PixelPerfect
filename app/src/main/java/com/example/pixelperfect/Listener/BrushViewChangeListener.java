package com.example.pixelperfect.Listener;

import com.example.pixelperfect.Editor.BrushDrawingView;

/**
 * 画笔视图变化监听器，定义了画笔视图变化事件的回调方法。
 */
public interface BrushViewChangeListener {
    /**
     * 当开始绘制时调用。
     */
    void onStartDrawing();

    /**
     * 当停止绘制时调用。
     */
    void onStopDrawing();

    /**
     * 当添加画笔绘制视图时调用。
     *
     * @param brushDrawingView 添加的画笔绘制视图
     */
    void onViewAdd(BrushDrawingView brushDrawingView);

    /**
     * 当移除画笔绘制视图时调用。
     *
     * @param brushDrawingView 被移除的画笔绘制视图
     */
    void onViewRemoved(BrushDrawingView brushDrawingView);
}

