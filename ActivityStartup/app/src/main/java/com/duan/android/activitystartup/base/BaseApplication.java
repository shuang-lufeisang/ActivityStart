package com.duan.android.activitystartup.base;

import android.app.Application;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/07
 * desc :
 * version: 1.0
 * </pre>
 */
public class BaseApplication extends Application{

    private static BaseApplication instance;

    public static synchronized BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public BaseApplication(){}

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


}
