package com.duan.android.activitystartup.screenshot;

import android.graphics.Bitmap;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/28
 * desc :  分享相关
 * version: 1.0
 * </pre>
 */
public interface SocialShareInterface {

    /**
     * 分享图片
     *
     * @param content   文字描述
     * @param imagePath 图片路径
     * @param listener  结果监听
     */
    void shareImage(String content, String imagePath, SocialShareListener listener);

    void shareImage(String content, Bitmap bitmap, SocialShareListener listener);

    /**
     * 分享文字，页面
     *
     * @param content  文字描述
     * @param listener 结果监听
     * @param extras   其他信息（跳转url，缩略图等）
     */
    void sharePage(String content, SocialShareListener listener, String... extras);

    /**
     * 获取 AppKey
     * @return
     */
    String getRegisterAppKey();

    interface SocialShareListener {
        void onShareSuccess();
        void onShareFailed();
    }
}
