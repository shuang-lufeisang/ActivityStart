package com.duan.android.activitystartup.widget.tablayout;

/**
 * <pre>
 * author : Duan
 * time : 2019/09/09
 * desc :
 * version: 2.2.0
 * </pre>
 */

import android.support.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();
}