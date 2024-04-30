package com.example.pixelperfect.Listener;

/**
 * 滤镜选择监听器，定义了滤镜选择事件的回调方法。
 */
public interface FilterListener {
    /**
     * 当选择了滤镜时调用。
     *
     * @param currentSelected 当前选中的滤镜索引
     * @param filterName      选中的滤镜名称
     */
    void onFilterSelected(int currentSelected, String filterName);
}