package com.duan.android.activitystartup.base;

import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <pre>
 * author : Duan
 * time : 2019/03/14
 * desc :
 * version: 1.0
 * </pre>
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    /**
     * 将代理类通用行为抽出来
     */
    protected T mPresenter;

    Unbinder mButterKnifeUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(getContentView(), container , false);
        mButterKnifeUnbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null){
            mPresenter.subscribe();
        }
        initView();
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null){
            mPresenter.subscribe();
        }
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mButterKnifeUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.unSubscribe();
        }
    }

    /** 返回一个用于显示界面的布局id */
    public abstract int getContentView();

    /** 初始化View的代码写在这个方法中 */
    public abstract void initView();

    /** 初始化监听器的代码写在这个方法中 */
    public abstract void  initListener();

    /** 初始数据的代码写在这个方法中，用于从服务器获取数据 */
    public abstract void initData();


//////////////////////////////////////////////////////////////////////////////
    // 界面跳转
    /** 通过Class跳转界面 */
    public void startActivity(Class<?> cls){
        startActivity(cls, null);
    }

    /** 通过Class跳转界面 包含Bundle */
    public void startActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /** startActivityForResult  请求码 */
    public void startActivityForResult(Class<?> cls, int requestCode){
        startActivityForResult(cls, null, requestCode);
    }

    /** startActivityForResult 包含Bundle */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode){
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
}
