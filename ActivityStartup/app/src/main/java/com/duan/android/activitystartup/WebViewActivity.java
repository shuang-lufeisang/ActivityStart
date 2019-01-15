package com.duan.android.activitystartup;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.js_web.MJavascriptInterface;
import com.duan.android.activitystartup.js_web.MyWebViewClient;
import com.duan.android.activitystartup.js_web.tools.StringUtils;
import com.duan.android.activitystartup.util.LogUtils;
import com.duan.android.activitystartup.util.ScreenUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * WebView
 *
 * 1. 控制文章左右对齐
 * 2. 点击查看大图 - 保存到本地
 * */
public class WebViewActivity extends BaseActivity {

    String TAG = "WebViewActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_right) TextView saveBt;

    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.web_view) WebView mWebView;

    private static final String WEBVIEW_CONTENT = "<html><head></head><body style=\"text-align:justify;margin:0;\">%s</body></html>";
    private static final String       IMG_HEARD = "<html><head><meta name=\\\"viewport\\\" content=\\\"width=device-width,initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes\\\"/><style>img{max-width:100% !important;height:auto !important;}</style></head><body style=\"text-align:justify;margin:0;\">%s</body></html>";
    String content;

    private String[] imageUrls;  // get images url from html

    public static void startWebViewActivity(Context context) {
        LogUtils.printInfo("WebViewActivity", "=================================== startWebViewActivity ===================");
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public static Intent getWebViewIntent(Context context) {
        LogUtils.printInfo("WebViewActivity", "=================================== getWebViewIntent ===================");
        Intent intent = new Intent(context, WebViewActivity.class);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView() {
        initToolbar();
        initWebView();
    }

    @OnClick(R.id.fl_title_menu)
    public void onBackClicked() {
        onBackPressed();
    }

    public void initToolbar(){
        backBt.setVisibility(View.VISIBLE);
        saveBt.setVisibility(View.VISIBLE);
        title.setText("WebViewActivity");
    }

    // 点击保存
    @OnClick(R.id.fl_title_menu_right)
    public void onSaveClicked(){
        Intent data = new Intent();
        data.putExtra("value", "I'm from WebViewActivity!");
        setResult(3, data);
        finish();   //关闭该窗口

    }

    // init WebView
    private void initWebView() {

        String htmlContent = getAssetsString("news.html", this);
        LogUtils.printCloseableInfo(TAG, "htmlContent: " + htmlContent);

        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        imageUrls = StringUtils.returnImageUrlsFromHtml(htmlContent);  // get images url from html

        webSettings.setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        webSettings.setBuiltInZoomControls(false);
//        webSettings.setSupportZoom(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setHorizontalScrollBarEnabled(false);




        // mWebView.loadDataWithBaseURL(null, String.format(WEBVIEW_CONTENT, content),"text/html", "UTF-8",null);
        // mWebView.loadDataWithBaseURL(null, getHtmlContent(htmlContent),"text/html", "UTF-8",null);

        mWebView.loadUrl("file:///android_asset/news.html");


        // 本地 css
//        String linkCss = "<link rel=\"stylesheet\" href=\"file:///android_asset/content.css\" type=\"text/css\">";
//        String body = "<html><header>" + linkCss + "</header>" + content + "</body></html>";
//        LogUtils.printCloseableInfo(TAG, "body: " +body);
//        mWebView.loadDataWithBaseURL(linkCss, body, "text/html", "UTF-8", null);

        mWebView.addJavascriptInterface(new MJavascriptInterface(this,imageUrls), "imagelistener");
        mWebView.setWebViewClient(new MyWebViewClient());


    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    /**
     * WebViewClient
     * WebChromeClient
     * @return
     */
    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(android.webkit.WebView view, String url) { //页面加载完成
            mProgressBar.setVisibility(View.GONE);

            String javascript = "javascript:function ResizeImages() {" +
                    "var myimg,oldwidth;" +
                    "var maxwidth = document.body.clientWidth;" +
                    "for(i=0;i <document.images.length;i++){" +
                    "myimg = document.images[i];" +
                    "if(myimg.width > maxwidth){" +
                    "oldwidth = myimg.width;" +
                    "myimg.width = maxwidth-30;" +
                    "}" +
                    "}" +
                    "}";
            String width = String.valueOf(ScreenUtils.widthPixels(getApplicationContext()));
            view.loadUrl(javascript);
            view.loadUrl("javascript:ResizeImages();");

        }

        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) { //页面开始加载
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            Log.i(TAG,"拦截url:"+ url);
            if(url.equals("http://www.google.com/")){
                Log.e(TAG, "国内不能访问google,拦截该url");
                return true; //表示已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient(){
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(android.webkit.WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定",null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();
            Log.i(TAG,"onJsAlert:");

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(android.webkit.WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i(TAG,"onReceivedTitle ==== 网页标题:"+ title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(android.webkit.WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
        }
    };


    // 拼接成 html 格式
    private String getHtmlContent(String content) {
        StringBuffer sb = new StringBuffer();
        //sb.append("<!doctype html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\\\"UTF-8\\\">");
        sb.append("<meta name=\\\"viewport\\\" content=\\\"width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no\\\"/>");
        sb.append("<style type=\\\"text/css\\\"> p{font-size: 14px; color: #333;line-height:22px; margin-bottom: -10px;} p:last-child{ margin-bottom:0} .content{padding:0 15px;text-align: justify; color: #A4C639;}</style>");
        sb.append("</head>");
        sb.append("<body>");
        //sb.append("<div class=\\\"allbg\\\" id=\\\"allbg\\\"></div>");
        sb.append("<section class=\\\"article-content\\\">");
        sb.append("<section class=\\\"text\\\">");
        sb.append("<div class=\\\"content\\\">");
        sb.append(content);
        sb.append("</div></section></section>");
        sb.append("</body>");
        sb.append("</html>");
        LogUtils.printCloseableInfo(TAG, sb.toString());
        return sb.toString();
    }

    // 拼接成 html 格式
    private String getHtmlContent2(String content) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\\\"UTF-8\\\">");
        sb.append("<meta name=\\\"viewport\\\" content=\\\"width=device-width,initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0,user-scalable=yes\\\"/>");
        sb.append("<style type=\\\"text/css\\\"> p{font-size: 14px; color: #333;line-height:22px; margin-bottom: -10px;} p:last-child{ margin-bottom:0} .content{padding:0 15px;text-align: justify; color: #A4C639;}</style>");
        sb.append("<style>img{max-width:100% !important;height:auto !important;}</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<div class=\\\"content\\\">");
        sb.append(content);
        sb.append("</div></section></section>");
        sb.append("</body>");
        sb.append("</html>");
        LogUtils.printCloseableInfo(TAG, sb.toString());
        return sb.toString();
    }

    /**
     * 获取 Assets html文件
     * @param fileName
     * @return
     */
    public static String getAssetsString(String fileName,Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf =
                    new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获取 Assets html文件
     * @param fileName
     * @return
     */
    public String getFromAssets(String fileName){
        try{
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null){
                result += line;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
