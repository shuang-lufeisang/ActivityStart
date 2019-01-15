package com.duan.android.activitystartup.js_web;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/11
 * desc :  js接口类
 * version: 1.0
 * </pre>
 */
public class MJavascriptInterface {
    private Context context;
    private String [] imageUrls;

    public MJavascriptInterface(Context context,String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        Intent intent = new Intent();
        intent.putExtra("imageUrls", imageUrls);
        intent.putExtra("curImageUrl", img);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
        for (int i = 0; i < imageUrls.length; i++) {
            Log.e("图片地址"+i,imageUrls[i].toString());
        }
    }
}
