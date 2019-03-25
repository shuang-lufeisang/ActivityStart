package com.duan.android.activitystartup.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.util.LogUtils;

import butterknife.BindView;

/**
 * SHOW some widget
 * 1. AutoSizeableTextView(一个支持文字自动大小的接口，AppCompatTextView 谷歌为兼容老版本提供的新类 实现了该接口)
 */

public class WidgetActivity extends BaseActivity {

    String TAG = "WidgetActivity";
    @BindView(R.id.autoSizeTv) AppCompatTextView autoSizeTv;
    @BindView(R.id.autoSizeTv2) AppCompatTextView autoSizeTv2;
    @BindView(R.id.autoSizeTv3) AppCompatTextView autoSizeTv3;

//    String testString = "AutoSizeableTextView:一个支持文字自动大小的接口";
    String testString = "一个支持文字自动大小的接口";

    public static Intent getWidgetIntent(Context context) {
        LogUtils.printInfo("WidgetActivity", "=================================== getWidgetIntent ===================");
        Intent intent = new Intent(context, WidgetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_widget;
    }

    @Override
    public void initView() {
        // 方式一： 设置TextView字号支持改变大小模式
        // TextViewCompat.setAutoSizeTextTypeWithDefaults(autoSizeTv3,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        // 方式二
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(autoSizeTv3, 8,25,1, TypedValue.COMPLEX_UNIT_SP);

        autoSizeTv.setText(testString);
        autoSizeTv2.setText(testString);
        autoSizeTv3.setText(testString);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

}
