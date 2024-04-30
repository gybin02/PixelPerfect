package com.example.pixelperfect.Listener;

/**
 * 画笔颜色监听器，定义了画笔颜色变化事件的回调方法。
 */
public interface BrushColorListener {
    /**
     * 当画笔颜色发生变化时调用。
     *
     * @param color   新的颜色值（ARGB 格式）
     * @param colorName 颜色名称
     */
    void onColorChanged(int color, String colorName);
}
