package com.example.pixelperfect.Listener;

import com.example.pixelperfect.Editor.BrushDrawingView;

/**
 * 笔刷事件
 */
public interface BrushViewChangeListener {
    void onStartDrawing();

    void onStopDrawing();

    void onViewAdd(BrushDrawingView brushDrawingView);

    void onViewRemoved(BrushDrawingView brushDrawingView);
}
