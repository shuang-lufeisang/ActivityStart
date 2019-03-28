package com.duan.android.activitystartup.scheme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.duan.android.activitystartup.MainActivity;
import com.duan.android.activitystartup.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 通过外部链接调起APP
 */
public class SchemeActivity extends AppCompatActivity {

    public static final String TYPE_INTENT = "type";
    public static final String URL_INTENT = "url";
    public static final String NAME_INTENT = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);

        // initIntent();
    }

    private void initIntent(){
        // 从网页打开
        Intent intent = getIntent();
        if (intent.getData() != null){
            Uri uri = intent.getData();
            uri.getScheme();   // 获取scheme
            uri.getHost();     // 获取host
            uri.getAuthority();// 获取authority
            String type = uri.getQueryParameter(TYPE_INTENT);
            String url = uri.getQueryParameter(URL_INTENT);
            String name = uri.getQueryParameter(NAME_INTENT);

            // 标题转 UTF-8 码
            if (!TextUtils.isEmpty(name)){
                try {
                    name = URLDecoder.decode(name, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            // 获取到的参数跳转
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.putExtra(TYPE_INTENT, type);
            startIntent.putExtra(URL_INTENT, url);
            startIntent.putExtra(NAME_INTENT, name);
            startActivity(startIntent);
            finish();
        }
    }
}
