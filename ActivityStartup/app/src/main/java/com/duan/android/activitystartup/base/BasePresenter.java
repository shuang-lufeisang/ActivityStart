package com.duan.android.activitystartup.base;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/07
 * desc :
 * version: 1.0
 * </pre>
 */
public interface BasePresenter {

    //绑定数据
    void subscribe();
    //解除绑定
    void unSubscribe();

}
