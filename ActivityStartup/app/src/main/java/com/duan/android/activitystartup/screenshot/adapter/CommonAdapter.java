package com.duan.android.activitystartup.screenshot.adapter;

import android.content.Context;

import com.duan.android.activitystartup.screenshot.adapter.delegate.ItemViewDelegate;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/28
 * desc :
 * version: 1.0
 * </pre>
 */
public abstract class CommonAdapter <T> extends MultiItemTypeAdapter<T> {

//    public CommonAdapter(Context context, List<T> datas) {
//        super(context, datas);
//    }

    public CommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    @Override
    protected void convert(ViewHolder viewHolder, T item, int position) {
        super.convert(viewHolder, item, position);
    }
}
