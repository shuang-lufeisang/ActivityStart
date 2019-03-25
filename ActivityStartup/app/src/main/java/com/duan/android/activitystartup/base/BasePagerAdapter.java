package com.duan.android.activitystartup.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2019/03/14
 * desc :   FragmentPagerAdapter use for ViewPager
 * version: 1.0
 * </pre>
 */
public class BasePagerAdapter extends FragmentPagerAdapter{

    private List<?> mFragments;

    public BasePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public BasePagerAdapter(FragmentManager fm, List<?> fragmentList) {
        super(fm);
        mFragments = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

}
