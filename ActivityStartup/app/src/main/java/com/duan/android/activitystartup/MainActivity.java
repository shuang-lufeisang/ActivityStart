package com.duan.android.activitystartup;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.base.Constant;
import com.duan.android.activitystartup.furglass.FurGlassActivity;
import com.duan.android.activitystartup.js_web.WebViewActivity;
import com.duan.android.activitystartup.propertySheet.PropertySheetActivity;
import com.duan.android.activitystartup.util.LogUtils;
import com.duan.android.activitystartup.widget.WidgetActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 1. 信息修改
 * 2. WebView
 * 3. 字母表侧边栏 RecyclerView联动
 * 4. FurGlass 毛玻璃效果
 * 5. 文字自动大小 AutoSizeText ...(自定义控件)
 * 6. 跳转浏览器
 * 7. 清理缓存 （todo）
 */
public class MainActivity extends BaseActivity {

    String TAG = "MainActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.linear_price) LinearLayout linearPrice;      // 单价 linear
    @BindView(R.id.tv_price) TextView tvPrice;                  // 单价
    @BindView(R.id.iv_enter_price) ImageView ivEnterPrice;      // 单价 enter

    @BindView(R.id.linear_web_view) LinearLayout linearWebView; // WebView_linear
    @BindView(R.id.tv_web_view) TextView tvWebView;             // WebView result info

    @BindView(R.id.linear_recycler_view) LinearLayout linearRecycler;     // RecyclerView_linear
    @BindView(R.id.tv_recycler_view) TextView tvRecyclerView;             // RecyclerView result info

    private String mPrice = null;               // 单价
    private String mPriceTitle = null;          // 单价标题

    private final static int REQUEST_CODE_PRICE = 1;   // 返回的结果码 - 修改价格
    private final static int REQUEST_CODE_WEB = 2;     // 返回的结果码 - WebView
    private final static int REQUEST_CODE_RECYCLER = 3;// 返回的结果码 - RecyclerView
    private final static int REQUEST_CODE_FUR = 4;     // 返回的结果码 - FUR GLASS
    private final static int REQUEST_CODE_AUTO = 5;    // 返回的结果码 - AutoSizeTextView

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        title.setText("MyTestApp");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    // 修改信息
    @OnClick(R.id.linear_price)
    public void onPriceClicked(){
        String content;
        boolean isHint = true;
        mPrice = tvPrice.getText().toString();
        if (mPrice != null && mPrice.length() > 0){
            content = "" + mPrice;
            isHint = false;
        }else {
            content = getResources().getString(R.string.quote_range);
        }

        // InfoUpdateActivity.startInfoUpdateActivity(this, Constant.InfoUpdate.INFO_QUOTE_PRICE, mPriceTitle, content, isHint);
        Intent intent = InfoUpdateActivity.getInfoUpdateIntent(this, Constant.InfoUpdate.INFO_QUOTE_PRICE, mPriceTitle, content, isHint);

        // Intent intent = new Intent(MainActivity.this, InfoUpdateActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PRICE);
    }

    // go to WebViewActivity
    @OnClick(R.id.linear_web_view)
    public void onWebViewClicked(){
        Intent intent = WebViewActivity.getWebViewIntent(this);
        startActivityForResult(intent, REQUEST_CODE_WEB);
    }

    // go to 物性表 字母表联动
    /**
     * 字母表联动
     * 读取本地json 文件
     */
    @OnClick(R.id.linear_recycler_view)
    public void onRecyclerViewClicked(){
        Intent intent = PropertySheetActivity.getPropertySheetIntent(this);
        startActivityForResult(intent, REQUEST_CODE_RECYCLER);
    }

    // go to 高斯模糊 - 毛玻璃 fur_glass
    @OnClick(R.id.linear_fur)
    public void onFurGlassClicked(){
        Intent intent = FurGlassActivity.getFurGlassIntent(this);
        startActivityForResult(intent, REQUEST_CODE_FUR);
    }

    // go to 自动文字大小
    @OnClick(R.id.linear_auto_text)
    public void onAutoTextClicked(){
        Intent intent = WidgetActivity.getWidgetIntent(this);
        startActivityForResult(intent, REQUEST_CODE_FUR);
    }

    // go to 浏览器
    @OnClick(R.id.linear_browser)
    public void onBrowserClicked(){
        //代码实现跳转
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://mb.ihwdz.com/article/article.html?id=200979&datetime=08:59");  //此处填链接
        intent.setData(content_url);
        startActivity(intent);
    }

    // go to 清理缓存
    @OnClick(R.id.linear_clean)
    public void onCleanClicked(){
        doClean();
    }

    private void doClean() {
        // TODO: 2019/2/28  清理缓存
        getCacheDir();
    }

    /** 返回当前 Activity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.printCloseableInfo(TAG, "requestCode: "+ requestCode +"  resultCode: "+ resultCode + "  data: "+ data);
        if (data != null){
            // 从修改价格 返回
            if (requestCode == REQUEST_CODE_PRICE){
                tvPrice.setText(data.getStringExtra("value"));
            }
            // WebViewActivity 返回
            if (requestCode == REQUEST_CODE_WEB){
                tvWebView.setText(data.getStringExtra("value"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 首页广告弹窗
     */
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_ad:
//                gotoAdvertisement();
//                dialog.dismiss();
//                break;
//            case R.id.close_ad:
//                dialog.dismiss();
//                break;
//        }
//    }

    private void gotoAdvertisement(){
        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
        startActivity(intent);
    }


}
