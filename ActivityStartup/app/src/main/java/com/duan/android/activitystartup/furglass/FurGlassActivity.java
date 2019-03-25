package com.duan.android.activitystartup.furglass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.propertySheet.PropertySheetActivity;
import com.duan.android.activitystartup.util.LogUtils;

import static com.duan.android.activitystartup.util.furglass.Fglass.blur;

public class FurGlassActivity extends AppCompatActivity {

    private LinearLayout linear;
    private ImageView image;
    private TextView text;
    private TextView statusText;
    private CheckBox cb_fastBlur;

    public static Intent getFurGlassIntent(Context context) {
        LogUtils.printInfo("PropertySheetActivity", "=================================== getPropertySheetIntent ===================");
        Intent intent = new Intent(context, FurGlassActivity.class);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fur_glass);

        initView();
    }

    private void initView() {
        linear = (LinearLayout) findViewById(R.id.linear);
        image = (ImageView) findViewById(R.id.picture);
        text = (TextView) findViewById(R.id.text);
        cb_fastBlur = (CheckBox) findViewById(R.id.main_cb_fastblur);

        // 新增 TextView statusText 展示处理时间
        statusText = addStatusText((ViewGroup) findViewById(R.id.controls));
        cb_fastBlur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                long startMs = System.currentTimeMillis();
                if (isChecked) {
                    //设置高斯模糊
                    blur(linear, text, 2, 8);
//                    blur(image, text, 2, 8);
                } else {
                    text.setBackgroundColor(Color.parseColor("#00ffffff"));
                }
                statusText.setText(System.currentTimeMillis() - startMs + "ms");
            }
        });
    }

    private TextView addStatusText(ViewGroup container) {
        TextView result = new TextView(this);
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        result.setTextColor(0xFFFFFFFF);
        container.addView(result);
        return result;
    }
}
