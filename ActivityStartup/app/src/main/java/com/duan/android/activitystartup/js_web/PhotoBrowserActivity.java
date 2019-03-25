package com.duan.android.activitystartup.js_web;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.util.FileUtils;
import com.duan.android.activitystartup.util.share.BottomSheetDialog;
import com.duan.android.activitystartup.util.share.ShareAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  WebView 点击查看大图; 图片分享 收藏
 *
 */
public class PhotoBrowserActivity extends BaseActivity {

    @BindView(R.id.pager) ViewPager mPager;
    // @BindView(R.id.crossIv) ImageView crossIv;         // 十字叉 图标
    @BindView(R.id.photoOrderTv) TextView photoOrderTv;// 图片序号
    @BindView(R.id.saveTv) TextView saveTv;            // 保存
    @BindView(R.id.centerIv)
    ImageView centerIv;       // 加载中 图标

    private String curImageUrl = "";
    private String[] imageUrls = new String[]{}; // 网页中图片数组

    private int curPosition = -1;
    private int[] initialedPositions = null;
    private ObjectAnimator objectAnimator;
    private View curPage;

    // 动态申请 读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    // 请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    // share
    RecyclerView shareRecyclerView;
    TextView shareCancel;
    BottomSheetDialog dialog;    // share dialog
    ShareAdapter mShareAdapter;
    // private IWXAPI api;
    private String shareUrlRoot;  // need id and shortDate
    private String shareUrl;
    private String mCurrentId = null;
    private String mShortDate;
    private String mShareTitle;
    private String mShareDescription;
    private String weChatFriend;               // 微信朋友
    private String weChatFriendsCircle;        // 微信朋友圈
    private String weChatCollections;          // 微信收藏

    String TAG = "PhotoBrowserActivity";

    @Override
    public int getContentView() {
        return R.layout.activity_photo_browser;
    }

    @Override
    public void initView() {
        // WeChat Share
        // api = WXEntryActivity.getShareApiInstance(this);

        imageUrls = getIntent().getStringArrayExtra("imageUrls");
        curImageUrl = getIntent().getStringExtra("curImageUrl");
        initialedPositions = new int[imageUrls.length];
        initInitialedPositions();

        initViewPager();  // init ViewPager


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    private void initViewPager() {

        mPager.setPageMargin((int)(getResources().getDisplayMetrics().density * 15));
        /**
         *  PagerAdapter
         */
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                if (imageUrls[position] != null && !"".equals(imageUrls[position])) {
                    final PhotoView view = new PhotoView(PhotoBrowserActivity.this);
                    view.enable();
                    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    Glide.with(PhotoBrowserActivity.this).load(imageUrls[position]).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).fitCenter().crossFade().listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (position == curPosition) {
                                hideLoadingAnimation();
                            }
                            showErrorLoading();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            occupyOnePosition(position);
                            if (position == curPosition) {
                                hideLoadingAnimation();
                            }
                            return false;
                        }
                    }).into(view);

                    // PhotoView on click
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(PhotoBrowserActivity.this, "clicked picture", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    // PhotoView on long click
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //Toast.makeText(PhotoBrowserActivity.this, "ON LONG CLICK picture", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                    container.addView(view);
                    return view;
                }
                return null;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                releaseOnePosition(position);
                container.removeView((View) object);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                curPage = (View) object;
            }
        });

        curPosition = returnClickedPosition() == -1 ? 0 : returnClickedPosition();
        mPager.setCurrentItem(curPosition);
        mPager.setTag(curPosition);
        if (initialedPositions[curPosition] != curPosition) {//如果当前页面未加载完毕，则显示加载动画，反之相反；
            showLoadingAnimation();   // 加载动画
        }
        photoOrderTv.setText((curPosition + 1) + "/" + imageUrls.length);//设置页面的编号

        /**
         * on page change listener
         */
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (initialedPositions[position] != position) { //如果当前页面未加载完毕，则显示加载动画，反之相反；
                    showLoadingAnimation();
                } else {
                    hideLoadingAnimation();
                }
                curPosition = position;
                photoOrderTv.setText((position + 1) + "/" + imageUrls.length);//设置页面的编号
                mPager.setTag(position);//为当前view设置tag
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    // 点击分享图片
    @OnClick(R.id.shareTv)
    public void onShareClicked(){
        showShareDialog();
    }

    private void showShareDialog() {
        // TODO: 2019/1/21 do show share dialog
    }

    // 点击保存图片
    @OnClick(R.id.saveTv)
    public void onSaveClicked(){
        savePhotoToLocal();
    }

    // image position
    private void initInitialedPositions(){
        for (int i = 0; i < initialedPositions.length; i++) {
            initialedPositions[i] = -1;
        }
    }

    // click position
    private int returnClickedPosition(){
        if (imageUrls == null || curImageUrl == null) {
            return -1;
        }
        for (int i = 0; i < imageUrls.length; i++) {
            if (curImageUrl.equals(imageUrls[i])) {
                return i;
            }
        }
        return -1;
    }

    // loading animation
    private void showLoadingAnimation() {
        centerIv.setVisibility(View.VISIBLE);
        centerIv.setImageResource(R.drawable.loading);
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(centerIv, "rotation", 0f, 360f);
            objectAnimator.setDuration(2000);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                objectAnimator.setAutoCancel(true);
            }
        }
        objectAnimator.start();
    }

    private void hideLoadingAnimation() {
        releaseResource();
        centerIv.setVisibility(View.GONE);
    }

    private void showErrorLoading() {
        centerIv.setVisibility(View.VISIBLE);
        releaseResource();
        centerIv.setImageResource(R.drawable.load_error);
    }

    private void releaseResource() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        if (centerIv.getAnimation() != null) {
            centerIv.getAnimation().cancel();
        }
    }

    private void occupyOnePosition(int position) {
        initialedPositions[position] = position;
    }

    private void releaseOnePosition(int position) {
        initialedPositions[position] = -1;
    }

    // save photo to local
    private void savePhotoToLocal() {
//        ViewGroup containerTemp = (ViewGroup) mPager.findViewWithTag(mPager.getCurrentItem());
//        if (containerTemp == null) {
//            return;
//        }
//        PhotoView photoViewTemp = (PhotoView) containerTemp.getChildAt(0);
        PhotoView photoViewTemp = (PhotoView) curPage;
        if (photoViewTemp != null) {
            GlideBitmapDrawable glideBitmapDrawable = (GlideBitmapDrawable) photoViewTemp.getDrawable();
            if (glideBitmapDrawable == null) {
                return;
            }
            Bitmap bitmap = glideBitmapDrawable.getBitmap();
            if (bitmap == null) {
                return;
            }
            FileUtils.savePhoto(this, bitmap, new FileUtils.SaveResultCallback() {
                @Override
                public void onSavedSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PhotoBrowserActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onSavedFailed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PhotoBrowserActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

}
