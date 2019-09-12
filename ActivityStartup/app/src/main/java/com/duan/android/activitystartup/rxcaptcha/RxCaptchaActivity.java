package com.duan.android.activitystartup.rxcaptcha;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.util.LogUtils;
import com.luozm.captcha.Captcha;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.duan.android.activitystartup.rxcaptcha.RxCaptcha.TYPE.NUMBER;
/** captcha 验证码 */
public class RxCaptchaActivity extends AppCompatActivity{

    String TAG = "RxCaptchaActivity 验证码";
    @BindView(R.id.tv_code) TextView tvCode;
    @BindView(R.id.iv_code) ImageView ivCode;

    @BindView(R.id.captCha) Captcha captcha;   // 滑块儿-拼图验证码 控件
    //String url;        // 滑块儿-拼图验证码 素材
    @BindView(R.id.bt_progress_mode) Button btnMode; // 点击 更改滑动条模式（无滑动条-手触滑块）
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_captch);
        ButterKnife.bind(this);

        onCodeIvClicked(); //点击图片验证码

        captcha = findViewById(R.id.captCha);
        captcha.setBitmap(R.mipmap.lucy);
        captcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                Toast.makeText(RxCaptchaActivity.this,"验证成功",Toast.LENGTH_SHORT).show();
                return "验证通过,耗时"+time+"毫秒";
            }

            @Override
            public String onFailed(int failedCount) {
                Toast.makeText(RxCaptchaActivity.this,"验证失败",Toast.LENGTH_SHORT).show();
                return "验证失败,已失败"+failedCount+"次";
            }

            @Override
            public String onMaxFailed() {
                Toast.makeText(RxCaptchaActivity.this,"验证超过次数，你的帐号被封锁",Toast.LENGTH_SHORT).show();
                return "验证失败,帐号已封锁";
            }
        });

    }


    @OnClick(R.id.iv_code)
    public void onCodeIvClicked(){
        LogUtils.printCloseableInfo(TAG, "点击图片验证码");
        RxCaptcha.build()
                .backColor(0xffffff)
                .codeLength(4)
                .fontSize(60)
                .lineNumber(2)
                .size(200, 70)
                .type(NUMBER)
                .into(ivCode);
        tvCode.setText(RxCaptcha.build().getCode());
    }


    // 点击 更改滑动条模式（无滑动条-手触滑块）
    public void changeProgressMode(View view) {
        if (captcha.getMode() == Captcha.MODE_BAR) {
            captcha.setMode(Captcha.MODE_NONBAR); // 无滑动条模式
            btnMode.setText("有滑动条模式");
        } else {
            captcha.setMode(Captcha.MODE_BAR);    // 有滑动条模式
            btnMode.setText("无滑动条模式");
        }
    }

    // 更改滑动条样式( 圆点/方块)
    boolean isSeekBar1 = false;
    public void changeProgressDrawable(View view){
        if(isSeekBar1){
            captcha.setSeekBarStyle(R.drawable.po_seekbar,R.drawable.thumb);  // 方块
        }else{
            captcha.setSeekBarStyle(R.drawable.po_seekbar1,R.drawable.thumb1);// 圆点
        }
        isSeekBar1=!isSeekBar1;
    }

    // 点击更换拼图
    boolean isCat = true;
    public void changePicture(View view){
        if(isCat){
            captcha.setBitmap("http://img4.imgtn.bdimg.com/it/u=2091068830,1003707060&fm=200&gp=0.jpg");
        }else{
            captcha.setBitmap(R.mipmap.lucy);
        }
        isCat=!isCat;
    }
}
