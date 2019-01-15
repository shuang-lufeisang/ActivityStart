package com.duan.android.activitystartup.js_web;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.js_web.tools.StringUtils;
import com.duan.android.activitystartup.util.LogUtils;

import butterknife.BindView;

/**
 *  WebView  JS JAVA 交互
 *  点击查看大图
 *  保存图片
 */
public class MWebViewActivity extends BaseActivity {

    @BindView(R.id.webView) WebView mWebView;

    private String[] imageUrls ;

    public static Intent getMWebViewIntent(Context context) {
        LogUtils.printInfo("MWebViewActivity", "=================================== getMWebViewIntent ===================");
        Intent intent = new Intent(context, MWebViewActivity.class);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }



    @Override
    public int getContentView() {
        return R.layout.activity_mweb_view;
    }

    @Override
    public void initView() {

        initWebView();
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        imageUrls = StringUtils.returnImageUrlsFromHtml(null);
        mWebView.loadUrl("http://a.mp.uc.cn/article.html?uc_param_str=frdnsnpfvecpntnwprdssskt&client=ucweb&wm_aid=c51bcf6c1553481885da371a16e33dbe&wm_id=482efebe15ed4922a1f24dc42ab654e6&pagetype=share&btifl=100");
        mWebView.addJavascriptInterface(new MJavascriptInterface(this,imageUrls), "imagelistener");
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(MWebViewActivity.this).clearDiskCache(); //清理磁盘缓存需要在子线程中执行
            }
        }).start();
        Glide.get(MWebViewActivity.this).clearMemory();//清理内存缓存可以在UI主线程中进行
        super.onDestroy();
    }


}
