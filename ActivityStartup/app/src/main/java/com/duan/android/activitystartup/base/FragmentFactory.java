package com.duan.android.activitystartup.base;

import android.content.Context;

import com.duan.android.activitystartup.propertySheet.fragment.PropertyAbstractFragment;
import com.duan.android.activitystartup.propertySheet.fragment.PropertyDetailFragment;
import com.duan.android.activitystartup.propertySheet.fragment.PropertyOverviewFragment;
import com.duan.android.activitystartup.util.LogUtils;

/**
 * <pre>
 * author : Duan
 * time : 2019/03/14
 * desc : FragmentFactory
 *        物性表 三层 PropertyFragment: Overview Abstract Detail
 * version: 1.0
 * </pre>
 */
public class FragmentFactory {

    private String TAG = "FragmentFactory";
    private static FragmentFactory mInstance;
    private PropertyOverviewFragment mOverviewFragment; // 物性表-概述
    private PropertyAbstractFragment mAbstractFragment; // 物性表-摘要
    private PropertyDetailFragment mDetailFragment;     // 物性表-详情

    private Context mContext;
    private FragmentFactory(Context context) {
        this.mContext = context;
    }

    public static FragmentFactory getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FragmentFactory.class) {
                if (mInstance == null) {
                    mInstance = new FragmentFactory(context);
                }
            }
        }
        return mInstance;
    }

    /** 物性表概观 */
    public PropertyOverviewFragment getPropertyOverviewFragment(){
        LogUtils.printCloseableInfo(TAG, "================= getPropertyOverviewFragment");

        if (mOverviewFragment == null){
            LogUtils.printCloseableInfo(TAG, "================= new PropertyOverviewFragment()");
            mOverviewFragment = new PropertyOverviewFragment();
        }
        return mOverviewFragment;
    }

    /** 物性表摘要 */
    public PropertyAbstractFragment getPropertyAbstractFragment(){
        LogUtils.printCloseableInfo(TAG, "================= getPropertyAbstractFragment");

        if (mAbstractFragment == null){
            LogUtils.printCloseableInfo(TAG, "================= new PropertyAbstractFragment()");
            mAbstractFragment = new PropertyAbstractFragment();
        }
        return mAbstractFragment;
    }

    /** 物性表详情 */
    public PropertyDetailFragment getPropertyDetailFragment(){
        LogUtils.printCloseableInfo(TAG, "================= getPropertyDetailFragment");

        if (mDetailFragment == null){
            LogUtils.printCloseableInfo(TAG, "================= new PropertyDetailFragment()");
            mDetailFragment = new PropertyDetailFragment();
        }
        return mDetailFragment;
    }
}
