package com.duan.android.activitystartup.screenshot.adapter.delegate;

import com.duan.android.activitystartup.screenshot.adapter.ViewHolder;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/28
 * desc :
 * version: 1.0
 * </pre>
 */
public interface ItemViewDelegate<T> {
    int getItemViewLayoutId();
    boolean isForViewType(T item, int position);
    void convert(ViewHolder holder, T t, int position);
}
