package com.duan.android.activitystartup.lazy;

import android.view.View;

import com.duan.android.activitystartup.base.BaseLazyFragment;
import com.duan.android.activitystartup.widget.LazyFragment;

/**
 * <pre>
 * author : Duan
 * time : 2019/09/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class Fragment1 extends BaseLazyFragment {
    @Override
    protected int getLayoutRes() {
        return 0;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }
}
