package com.example.pixelperfect.Listener;

import com.example.pixelperfect.Editor.ViewType;

/**
 * 图片编辑器事件监听器，定义了图片编辑器中各种操作的回调方法。
 */
public interface OnPhotoEditorListener {
    /**
     * 当添加视图时调用。
     *
     * @param viewType 添加的视图类型
     * @param viewIndex 添加的视图索引
     */
    void onAddViewListener(ViewType viewType, int viewIndex);

    /**
     * 当移除视图时调用。
     *
     * @param viewIndex 移除的视图索引
     */
    void onRemoveViewListener(int viewIndex);

    /**
     * 当移除视图时调用。
     *
     * @param viewType  移除的视图类型
     * @param viewIndex 移除的视图索引
     */
    void onRemoveViewListener(ViewType viewType, int viewIndex);

    /**
     * 当开始改变视图时调用。
     *
     * @param viewType 开始改变的视图类型
     */
    void onStartViewChangeListener(ViewType viewType);

    /**
     * 当停止改变视图时调用。
     *
     * @param viewType 停止改变的视图类型
     */
    void onStopViewChangeListener(ViewType viewType);
}

