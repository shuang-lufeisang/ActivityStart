package com.duan.android.activitystartup.propertySheet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.api.NetDataModel;
import com.duan.android.activitystartup.api.RxUtil;
import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.base.BasePagerAdapter;
import com.duan.android.activitystartup.base.FragmentFactory;
import com.duan.android.activitystartup.util.LogUtils;
import com.duan.android.activitystartup.widget.NoSlidingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 *  物性表
 *   字母表联动
 *   读取本地json 文件
 *
 *  RecyclerView with alphabet slide bar
 *  data：
 *  http://mb.ihwdz.com/app/attr/home
 */
public class PropertySheetActivity extends BaseActivity {

    private String TAG = "PropertySheetActivity";

    @BindView(R.id.view_pager) NoSlidingViewPager mViewPager;

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.sidebar) RecyclerView mSideBar;    // 侧边栏
//    @BindView(R.id.tv_selected) TextView mSelectedTv; // 选中字母

    private AlphabetAdapter mAlphabetAdapter;                 // alphabet side bar
    private PropertyAdapter mAdapter;                         // 物性表 适配器

    // @Inject
    private CompositeSubscription mSubscriptions;
    private NetDataModel model;
    private RecyclerView.LayoutManager mLayoutManager;

    // ViewPager + Fragment
    private List fragments = new ArrayList();
    private BasePagerAdapter adapter;
    //private BaseStatePagerAdapter mAdapter;

    private boolean mIsFragment = false;  // false - 使用Activity 实现； true 使用Fragment 实现。

    public static Intent getPropertySheetIntent(Context context) {
        LogUtils.printInfo("PropertySheetActivity", "========================== getPropertySheetIntent ===================");
        Intent intent = new Intent(context, PropertySheetActivity.class);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_property_sheet;
    }

    @Override
    public void initView() {
        mSubscriptions = new CompositeSubscription();
        model = NetDataModel.getInstance(this);

        //initViewPager();   // 三层 fragment

        initSideBarView();
        initRecyclerView();

    }

    // 初始化 ViewPager
    private void initViewPager() {

        fragments.add(FragmentFactory.getInstance(this).getPropertyOverviewFragment());
        fragments.add(FragmentFactory.getInstance(this).getPropertyAbstractFragment());
        fragments.add(FragmentFactory.getInstance(this).getPropertyDetailFragment());

        adapter = new BasePagerAdapter(getSupportFragmentManager(), fragments);
        //mAdapter = new BaseStatePagerAdapter(getSupportFragmentManager(), fragments);

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

    @OnClick(R.id.tv1)
    public void onOverviewClicked(){
        showPromptMessage("overview clicked!");
        mViewPager.setCurrentItem(0);
    }

    @OnClick(R.id.tv2)
    public void onAbstractClicked(){
        showPromptMessage("abstract clicked!");
        mViewPager.setCurrentItem(1);
    }

    @OnClick(R.id.tv3)
    public void onDetailClicked(){
        showPromptMessage("detail clicked!");
        mViewPager.setCurrentItem(2);
    }

    // 物性表 RecyclerView
    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getAdapter().addItemClickListener(new PropertyEventListener.OnItemClickListener() {
            @Override
            public void onItemClicked(String string) {
                // TODO: 2019/3/12 spec item checked. 进入spec 列表页
                showPromptMessage(string + "  Clicked");
            }
        });
        mRecyclerView.setAdapter(getAdapter());
        mRecyclerView.addOnScrollListener(mOnScrollListener);  // 滑动联动 AlphabetSlideBar
    }

    // 侧边栏字母表 alphabet side bar
    private void initSideBarView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSideBar.setLayoutManager(layoutManager);
        getAlphabetAdapter().addItemCheckListener(new PropertyEventListener.OnItemCheckListener() {
            @Override
            public void onItemChecked(String string) {
                scrollToAlphabet(string);
            }
        });
        mSideBar.setAdapter(getAlphabetAdapter());


    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getPropertyData();
    }

    // 物性表数据
    private void getPropertyData(){
        Subscription rxSubscription = model
                .getPropertyData()
                .compose(RxUtil.<PropertyData>rxSchedulerHelper())
                .subscribe(new Subscriber<PropertyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "getPropertyData: onError: " + e.toString());
                    }

                    @Override
                    public void onNext(PropertyData data) {
                        LogUtils.printCloseableInfo(TAG, "getPropertyData: onNext: " + data.data.keywords.size());
                        if (data != null && TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                // set data for side bar
                                if (data.data.letterBreeds != null){
                                    getAdapter().setDataList(data.data.letterBreeds);
                                    getAlphabetAdapter().setDataList(data.data.letterBreeds);
                                }
                                // set data for property sheet RecyclerView

                            }else {
                                showPromptMessage(data.msg);
                            }
                        }else {
                            showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // Alphabet Adapter
    public AlphabetAdapter getAlphabetAdapter() {
        if (mAlphabetAdapter == null){
            mAlphabetAdapter = new AlphabetAdapter(this);
        }
        return mAlphabetAdapter;
    }

    // Property Adapter
    public PropertyAdapter getAdapter() {
        if (mAdapter == null){
            mAdapter = new PropertyAdapter(this);
        }
        return mAdapter;
    }

    // 提示信息
    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 滑动监听
     */
    private boolean isScrollDown;  //是否是向下滑动
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;  // true:  向下滑动中
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // 获取RecyclerView的LayoutManager
//            mLayoutManager = recyclerView.getLayoutManager();
//            LogUtils.printCloseableInfo(TAG, "================== onScrollStateChanged mLayoutManager: " +mLayoutManager);
            if (newState == SCROLL_STATE_IDLE){ // The RecyclerView is not currently scrolling.
                int firstVisibleItem; // 首个可见Item
                // 获取到最后一个可见的item
                if (mLayoutManager instanceof LinearLayoutManager) {
                    // 如果是 LinearLayoutManager
                    firstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                    LogUtils.printCloseableInfo(TAG, "firstVisibleItem: "+ firstVisibleItem);
                    View firstView = mLayoutManager.getChildAt(0);
                    LinearLayout layout = (LinearLayout) firstView;
                    TextView firstTv = layout.findViewById(R.id.tv);

                    char firstAlphabet = firstTv.getText().charAt(0);  // 第一个可见Item 的排序字母
                    LogUtils.printCloseableInfo(TAG, "firstAlphabet: "+ firstAlphabet);
                    changeAlphabetSlideBar(firstAlphabet); // 滑动RecyclerView 更新当前选择的侧边栏字母
                } else {
                    // 否则抛出异常
                    throw new RuntimeException("Unsupported LayoutManager used");
                }
            }
        }
    };

    /**
     * 更新当前选择的侧边栏字母
     */
    private void changeAlphabetSlideBar(char item){
        PropertyData.CheckableItem checkableItem = new PropertyData.CheckableItem();
        checkableItem.isChecked = true;
        checkableItem.name = ""+item;
        mAlphabetAdapter.setSelectedItem(checkableItem);
    }

    /**
     * 滑动 RecyclerView 到指定开头字母
     * RecyclerView 滑动到某一位置并置顶
     */
    private void scrollToAlphabet(String item){
        if (mLayoutManager != null){
            int position = mAdapter.getAlphabetPosition(item); // 指定 item 所在位置
            LogUtils.printCloseableInfo(TAG, "position: " + position);

            final TopSmoothScroller mScroller = new TopSmoothScroller(this);
            mScroller.setTargetPosition(position);
            mLayoutManager.startSmoothScroll(mScroller);

        }else {
            showPromptMessage("mLayoutManager == null");
        }

    }


}
