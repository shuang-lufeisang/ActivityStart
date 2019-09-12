package com.duan.android.activitystartup;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.base.Constant;
import com.duan.android.activitystartup.furglass.FurGlassActivity;
import com.duan.android.activitystartup.js_web.WebViewActivity;
import com.duan.android.activitystartup.location.LocationActivity;
import com.duan.android.activitystartup.propertySheet.PropertySheetActivity;
import com.duan.android.activitystartup.rxcaptcha.RxCaptchaActivity;
import com.duan.android.activitystartup.screenshot.ScreenShotActivity;
import com.duan.android.activitystartup.sensor.SensorActivity;
import com.duan.android.activitystartup.util.ChineseCharToEn;
import com.duan.android.activitystartup.util.LogUtils;
import com.duan.android.activitystartup.widget.CountDownHMSTextView;
import com.duan.android.activitystartup.widget.WidgetActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 0. 图形验证码
 * 1. 信息修改
 * 2. WebView
 * 3. 字母表侧边栏 RecyclerView联动
 * 4. FurGlass 毛玻璃效果
 * 5. 文字自动大小 AutoSizeText ...(自定义控件)
 * 6. 跳转浏览器
 * 7. 清理缓存 （todo）
 */
public class MainActivity extends BaseActivity {

    private String TAG = "MainActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.linear_price) LinearLayout linearPrice;      // 单价 linear
    @BindView(R.id.tv_price) TextView tvPrice;                  // 单价
    @BindView(R.id.iv_enter_price) ImageView ivEnterPrice;      // 单价 enter

    @BindView(R.id.linear_web_view) LinearLayout linearWebView; // WebView_linear
    @BindView(R.id.tv_web_view) TextView tvWebView;             // WebView result info

    @BindView(R.id.linear_recycler_view) LinearLayout linearRecycler;     // RecyclerView_linear
    @BindView(R.id.tv_recycler_view) TextView tvRecyclerView;             // RecyclerView result info

    @BindView(R.id.tv_count_down) CountDownHMSTextView mCountDownTv;      // 30分钟倒计时

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
        testChineseHeader();
        count(); // 倒计时
        mCountDownTv.startCountDown(30);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // 默认隐藏软键盘
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    // 获取当前定位城市
    @OnClick(R.id.linear_location)
    public void onLocationGet(){
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    // 截屏处理+分享
    @OnClick(R.id.linear_screen_shot)
    public void onScreenShot(){
        Intent intent = new Intent(this, ScreenShotActivity.class);
        startActivity(intent);
    }


    // 图形验证码
    @OnClick(R.id.linear_captcha)
    public void onCaptchaClicked(){
        Intent captchaIntent = new Intent(MainActivity.this, RxCaptchaActivity.class);
        startActivity(captchaIntent);
    }


    // 修改信息
    @OnClick(R.id.linear_price)
    public void onPriceClicked(){
        String content;         // 展示内容
        boolean isHint = true;  // setHint/setText
        mPrice = tvPrice.getText().toString();
        // 当前有值则充当默认值；否则单独设置默认值；
        // isHint=true 展示提示文字 setHint; 否则直接填值 setText;
        if (mPrice != null && mPrice.length() > 0){
            content = mPrice;
            isHint = false;
        }else {
            content = getResources().getString(R.string.quote_range);
        }

        // InfoUpdateActivity.startInfoUpdateActivity(this, Constant.InfoUpdate.INFO_QUOTE_PRICE, mPriceTitle, content, isHint);
        Intent intent = InfoUpdateActivity.getInfoUpdateIntent(this, Constant.InfoUpdate.INFO_QUOTE_PRICE, mPriceTitle, content, isHint);
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
                String value = data.getStringExtra("value");

                tvPrice.setText(value);
            }
            // WebViewActivity 返回
            if (requestCode == REQUEST_CODE_WEB){
                tvWebView.setText(data.getStringExtra("value"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 倒计时
    private void count() {
        mCountDownTv
                .setNormalText("29:59")
                .setCountDownText("", "")
                //.setCountDownText("重新获取(", "s)")
                .setCloseKeepCountDown(true)//关闭页面保持倒计时开关
                .setCountDownClickable(false)//倒计时期间点击事件是否生效开关
                .setShowFormatTime(true)//是否格式化时间
                .setIntervalUnit(TimeUnit.SECONDS)
                .setOnCountDownStartListener(new CountDownHMSTextView.OnCountDownStartListener() {
                    @Override
                    public void onStart() {
                        showPromptMessage("开始计时");
                    }
                })
                .setOnCountDownTickListener(new CountDownHMSTextView.OnCountDownTickListener() {
                    @Override
                    public void onTick(long untilFinished) {
                        LogUtils.printError(TAG,  "onTick: " + untilFinished);
                    }
                })
                .setOnCountDownFinishListener(new CountDownHMSTextView.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
                        showPromptMessage("倒计时完毕");
                        mCountDownTv.setText("倒计时完毕");
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showPromptMessage("点击");
                    }
                });
    }

    // 获取设备传感器
    @OnClick(R.id.linear_sensor)
    public void onSensorClicked(){
        Intent intent = new Intent(this, SensorActivity.class);
        startActivity(intent);
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

    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void testChineseHeader(){
        ChineseCharToEn cte = new ChineseCharToEn();
        Log.e(TAG, "获取拼音首字母 上海："+ cte.getAllFirstLetter("上海"));
    }

}
