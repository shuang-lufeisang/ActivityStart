package com.duan.android.activitystartup.screenshot.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/29
 * desc :
 * version: 1.0
 * </pre>
 */
public abstract class BaseActivity extends FragmentActivity {
    protected View mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(getContentView(), null);
        setContentView(mRootView);

        getIntentData();                    // intent
        getIntentData(savedInstanceState);
        initPresenter();
        initHeaderView();
        initContentView();
        initFooterView();
        initSavedInstancesState(savedInstanceState);  //
        initData();
    }
    protected abstract int getContentView();

    protected abstract void initPresenter();

    protected abstract void initData();

    protected void initContentView() {
    }

    protected void initHeaderView() {

    }

    private void initFooterView() {

    }

    protected void getIntentData() {

    }

    protected void getIntentData(Bundle savedInstanceState) {

    }

    protected void initSavedInstancesState(Bundle savedInstanceState) {

    }

    // EventBus 暂且不用
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(BaseEvent event) {
//        onEventMainThread(event);
//    }
//    protected void onEventMainThread(BaseEvent event) {
//
//    }

}
