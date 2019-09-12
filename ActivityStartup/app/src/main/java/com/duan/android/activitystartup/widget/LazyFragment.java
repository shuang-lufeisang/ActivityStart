package com.duan.android.activitystartup.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duan.android.activitystartup.util.LogUtils;

/**
 * <pre>
 * author : Duan
 * time : 2019/07/31
 * desc :  ViewPager + Fragment 优化 - 懒加载
 * version: 1.0
 * </pre>
 */
public abstract class LazyFragment extends Fragment{

    String TAG = "LazyFragment";
    View rootView;
    boolean isViewCreated = false;        // 标志View 是否创建了
    boolean currentVisibleState = false;  // 当前是否可见
    boolean isFirstVisible = true;        // 第一次可见 另外操作（例如首次展示内容占位框）

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }

        initView(rootView);
        isViewCreated = true;

        if (getUserVisibleHint() && isHidden()){
            dispatchUserVisibleHint(true);
        }

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract int getLayoutRes();
    protected abstract void initView(View view);

    // 对用户是否可见
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isViewCreated){  // 视图创建后才开始分发事件
            if (isVisibleToUser && !currentVisibleState){
                dispatchUserVisibleHint(true);
            }else if (currentVisibleState && !isVisibleToUser){
                dispatchUserVisibleHint(false);
            }
        }
    }

    private void dispatchUserVisibleHint(boolean isVisible){
        if (currentVisibleState == isVisible){
            return;
        }
        currentVisibleState = isVisible;
        if (isVisible){

            if (isFirstVisible){
                onFragmentFirstVisible();
            }
            onFragmentResume();
        }else {

            onFragmentPause();
        }
    }

    // fragment 第一次可见
    public void onFragmentFirstVisible(){
        // TODO: 2019/7/31  fragment 第一次可见

    }

    private void onFragmentResume(){
        LogUtils.printCloseableInfo(TAG, "onFragmentResume: 真正的Resume，停止耗时操作");
    }
    private void onFragmentPause(){
        LogUtils.printCloseableInfo(TAG, "onFragmentPause: 真正的Pause，开始耗时操作");
    }

    // FragmentTransaction show hide
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden() && !currentVisibleState && getUserVisibleHint()){
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentVisibleState && getUserVisibleHint()){
            dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstVisible = true;
        isViewCreated = false;
        currentVisibleState = false;
    }
}
