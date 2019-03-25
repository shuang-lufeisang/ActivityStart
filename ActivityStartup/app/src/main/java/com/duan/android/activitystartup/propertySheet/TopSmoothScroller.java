package com.duan.android.activitystartup.propertySheet;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * <pre>
 * author : Duan
 * time : 2019/03/13
 * desc :
 * version: 1.0
 * </pre>
 */
public class TopSmoothScroller extends LinearSmoothScroller {

    public TopSmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }
}
