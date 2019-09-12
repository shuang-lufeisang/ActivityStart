package com.duan.android.activitystartup.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duan.android.activitystartup.lazy.FragmentDelegate;

/**
 * <pre>
 * author : Duan
 * time : 2019/09/02
 * desc :   LazyFragment
 * fragment 生命周期：onAttach -> onCreate -> onCreateView -> onViewCreated -> onActivityCreated -> onStart -> onResume -> onStop -> onDestroyView -> onDestroy -> onDetach
 * ViewPager+ Fragment 需关注的生命周期：onCreateView + onActivityCreated + onResume + onPause + onDestroyView
 * version: 1.0
 * </pre>
 */
public abstract class BaseLazyFragment extends Fragment{

    private static final String TAG = "BaseLazyFragment";

    protected View rootView = null;

    // view 是否已经创建 onCreateView 中置为 true; onDestroyView 中置为 false;
    boolean isViewCreated = false;

    // 为了获得 Fragment 不可见的状态，和再次回到可见状态的判断，我们还需要增加一个 currentVisibleState 标志位，
    // 该标志位在 onResume 中和 onPause 中结合 getUserVisibleHint 的返回值来决定是否应该回调可见和不可见状态函数
    boolean currentVisibleState = false;

    // 是否第一次创建的标志位
    boolean mIsFirstVisible = true;

    FragmentDelegate mFragmentDelegate;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null){
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }

        // initView 用于添加默认的界面
        initView(rootView);

        // 将 View 创建完成标志位设为 true
        isViewCreated = true;
        logD( "onCreateView: ");

        // 初始化的时候，判断当前fragment可见状态
        // TODO: 2019/9/2  !isHidden()什么时候调用？原理
        if (!isHidden() && getUserVisibleHint()){
            dispatchUserVisibleHint(true);
        }
        return rootView;
    }

    protected abstract int getLayoutRes();
    protected abstract void initView(View view);

    /**
     * 事件的分发(用户可见性)
     * 分第一次可见，可见，不可见分发
     * @param isVisible
     */
    private void dispatchUserVisibleHint(boolean isVisible){
        logD( "dispatchUserVisibleHint: " + isVisible);
        // 在负责分发事件的方法中判断一下当前父 fragment 是否可见，
        // 如果父 fragment 不可见我们就不进行可见事件的分发
        if (isVisible && isParentInvisible()){
            return;
        }
        // 为了代码严谨 （可见状态变化才会分发。此处正常不会出现）
        if (currentVisibleState == isVisible){
            return;
        }
        // 更新当前可见状态
        currentVisibleState = isVisible;
        if (isVisible){ // 要分发为：可见 （判断是否是：首次可见 -单独处理）
            if (mIsFirstVisible){
                mIsFirstVisible = false;
                onFragmentFirstVisible(); // 首次可见 -单独处理
            }
            // 通知用户此时变为了 可见状态
            onFragmentResume();
        }else {         // 要分发为：不可见
            // 通知用户此时变为了 不可见状态
            onFragmentPause();

        }
    }

    /** 父Fragment是否不可见*/
    private boolean isParentInvisible(){
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BaseLazyFragment){
            BaseLazyFragment fragment = (BaseLazyFragment) parentFragment;
            return !fragment.isSupportVisible();
        }
        return false;
    }

    private boolean isSupportVisible(){
        return currentVisibleState;
    }

    protected abstract void onFragmentFirstVisible();

    protected void onFragmentResume(){
        logD("onFragmentResume " + " 真正的resume,开始相关操作耗时");
    }

    protected void onFragmentPause(){
        logD("onFragmentPause " + " 真正的pause,结束相关耗时操作");
    }

    // 修改fragment的可见性
    // setUserVisibleHint 被调用有两种情况：
    // 1） 在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数，可以看log，
    //     对于默认 tab 和 间隔 checked tab 需要等到 isViewCreated = true 后才可以通过此通知用户可见
    // 2）对于之前已经调用过setUserVisibleHint 方法的fragment后，让fragment从可见到不可见之间状态的变化
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        logD("setUserVisibleHint: " + isVisibleToUser);

        //  对于情况1）不予处理，用 isViewCreated 进行判断，如果isViewCreated false，说明它没有被创建
        if (isViewCreated){
            // 对于情况2）要分情况考虑：2.1）如果是 *不可见->可见* 是下面的情况 ；2.2）如果是 *可见->不可见* 是下面的情况
            // 对于2.1）我们需要如何判断呢？首先必须是可见的（isVisibleToUser 为true）
            // 而且只有当可见状态进行改变的时候才需要切换（此时就添加了currentVisibleState来辅助判断），否则会出现反复调用的情况
            // 从而导致事件分发带来的多次更新
            // 对于2.2）如果是可见->不可见，判断条件恰好和 2.1）相反
            if (isVisibleToUser && !currentVisibleState){       // 不可见->可见
                // 分发可见状态
                dispatchUserVisibleHint(true);
            }else if (!isVisibleToUser && currentVisibleState){ // 可见->不可见
                // 分发不可见状态
                dispatchUserVisibleHint(false);
            }
        }
    }

    /**
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide
     * 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     * https://blog.csdn.net/u013278099/article/details/72869175
     *
     * 你会发现使用hide和show这时fragment的生命周期不再执行，
     * 不走任何的生命周期，
     * 这样在有的情况下，数据将无法通过生命周期方法进行刷新，
     * 所以你可以使用onHiddenChanged方法来解决这问题。
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        logD("onHiddenChanged: " + hidden);
        super.onHiddenChanged(hidden);
        if (hidden){
            dispatchUserVisibleHint(false);
        }else {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        logD( "onResume: ");
        // 在滑动或者跳转的过程中，第一次创建fragment的时候均会调用onResume方法，类似于在tab1 滑到tab2，此时tab3会缓存，
        // 这个时候会调用tab3 fragment的onResume，然而，此时是不需要去调用 dispatchUserVisibleHint(true)的
        // 为此出现了下面的if
        if (!mIsFirstVisible){
            // Activity1 中如果有多个fragment，然后从Activity1 跳转到Activity2，此时会有多个fragment会在activity1缓存，如果再从activity2跳转回activity1，
            // 这个时候会将所有的缓存的fragment进行onResume生命周期的重复，这个时候我们无需对所有缓存的fragment 调用dispatchUserVisibleHint(true)
            // 我们只需要对可见的fragment进行加载，因此就有下面的if
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()){
                dispatchUserVisibleHint(true);
            }
        }
    }

    /**
     * 只有当当前页面由 可见状态 -> 不可见状态 时才需要调用 dispatchUserVisibleHint
     * currentVisibleState && getUserVisibleHint() 能够限定是当前可见的 Fragment
     * 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
     *  子 fragment 走到这里的时候自身又会调用一遍
     */
    @Override
    public void onPause() {
        super.onPause();
        logD( "onPause: ");
        if (currentVisibleState && getUserVisibleHint()){
            dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        logD("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logD("onDestroyView");
        isViewCreated = false;
        mIsFirstVisible = false;
    }

    public void setFragmentDelegate(FragmentDelegate fragmentDelegate){
        mFragmentDelegate = fragmentDelegate;
    }

    // FragmentDelegate 打印生命周期
    private void logD(String info){
        if (mFragmentDelegate != null){
            mFragmentDelegate.dumpLifeCycle(info);
        }
    }

}
