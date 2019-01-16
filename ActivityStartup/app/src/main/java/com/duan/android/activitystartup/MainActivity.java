package com.duan.android.activitystartup;

import android.content.Intent;
<<<<<<< Updated upstream
=======
import android.view.View;
>>>>>>> Stashed changes
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duan.android.activitystartup.base.BaseActivity;
import com.duan.android.activitystartup.base.Constant;
import com.duan.android.activitystartup.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    String TAG = "MainActivity";

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.linear_price) LinearLayout linearPrice;      // 单价 linear
    @BindView(R.id.tv_price) TextView tvPrice;                  // 单价
    @BindView(R.id.iv_enter_price) ImageView ivEnterPrice;      // 单价 enter

    @BindView(R.id.linear_web_view) LinearLayout linearWebView; // WebView_linear
    @BindView(R.id.tv_web_view) TextView tvWebView;             // WebView result info

    private String mPrice = null;               // 单价
    private String mPriceTitle = null;          // 单价标题

    private final static int REQUEST_CODE_PRICE = 1;   // 返回的结果码 - 修改价格
    private final static int REQUEST_CODE_WEB = 2;     // 返回的结果码 - WebView

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        title.setText("MyApp");
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

        //InfoUpdateActivity.startInfoUpdateActivity(this, Constant.InfoUpdate.INFO_QUOTE_PRICE, mPriceTitle, content, isHint);
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

<<<<<<< Updated upstream
=======

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ad:
                gotoAdvertisement();
                dialog.dismiss();
                break;
            case R.id.close_ad:
                dialog.dismiss();
                break;
        }
    }

    private void gotoAdvertisement(){
        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
        startActivity(intent);
    }


>>>>>>> Stashed changes
}
