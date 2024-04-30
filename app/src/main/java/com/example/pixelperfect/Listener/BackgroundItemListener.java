package com.example.pixelperfect.Listener;

import android.view.View;

/**
 * 背景项点击监听器，定义了背景列表点击事件的回调方法。
 */
public interface BackgroundItemListener {
    /**
     * 当背景列表项被点击时调用。
     *
     * @param view 被点击的视图
     * @param position 被点击的项的位置
     */
    void onBackgroundListClick(View view, int position);
}

