package com.duan.android.activitystartup.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <pre>
 * author : Duan
 * time : 2019/03/14
 * desc :
 * version: 1.0
 * </pre>
 */
public class NoSlidingViewPager extends ViewPager{

    public NoSlidingViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
        // 去掉 ViewPager 默认的滑动效果，不消费事件
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        // 不拦截事件
        return false;
    }
}
