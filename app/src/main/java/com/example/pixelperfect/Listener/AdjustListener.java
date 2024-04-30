package com.example.pixelperfect.Listener;

import com.example.pixelperfect.Adapters.AdjustAdapter;

/**
 * 调整监听器，定义了调整选项被选中的回调方法。
 */
public interface AdjustListener {
    /**
     * 当调整选项被选中时调用。
     *
     * @param adjustModel 被选中的调整选项模型
     */
    void onAdjustSelected(AdjustAdapter.AdjustModel adjustModel);
}
