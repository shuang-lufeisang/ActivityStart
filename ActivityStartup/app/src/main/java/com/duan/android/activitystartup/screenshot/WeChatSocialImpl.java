package com.duan.android.activitystartup.screenshot;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.util.BitmapUtilLib;
import com.duan.android.activitystartup.util.StringUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/28
 * desc :
 * version: 1.0
 * </pre>
 */
public class WeChatSocialImpl extends AbsSocial implements SocialShareInterface {

    private static final String LOG_TAG = WeChatSocialImpl.class.getSimpleName();
    private static final int THUMB_SIZE = 10;

    //IWXAPI是第三方app和微信通信的openAPI接口
    private IWXAPI mApi = null;
    //分享场景
    private int mScene;

    public WeChatSocialImpl(Activity activity, int scene) {
        mActivity = activity;
        mScene = scene;
        init();
    }

    public void init() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        mApi = WXAPIFactory.createWXAPI(mActivity.getApplicationContext(), SocialConstant.WECHAT.APP_ID);
        // 将该app注册到微信
        mApi.registerApp(SocialConstant.WECHAT.APP_ID);
        mAppKey = SocialConstant.WECHAT.APP_ID;
    }

    private void doSend(String content, Bitmap thumbBitmap, String title, String weburl) {
        WXWebpageObject wxObj = new WXWebpageObject();
        wxObj.webpageUrl = weburl;
        WXMediaMessage msg = new WXMediaMessage();
        // title限制长度, hard code
        if (title == null || title.length() <= 512) {
            msg.title = title;
        } else {
            msg.title = title.substring(0, 512);
        }
        // content限制长度, hard code
        if (content == null || content.length() <= 1024) {
            msg.description = StringUtils.isNullOrEmpty(content) ? msg.title : content;
        } else {
            msg.description = content.substring(0, 1024);
        }
        msg.mediaObject = wxObj;
        if (thumbBitmap != null) {
            msg.thumbData = BitmapUtilLib.bmpToByteArray(thumbBitmap, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = getWXScene();
        mApi.sendReq(req);
    }

    private void doSendLocalImage(String path) {
        File file = new File(path);
        if (!file.exists()) {
            String tip = mActivity.getApplicationContext().getString(R.string.send_img_file_not_exist);
            Toast.makeText(mActivity.getApplicationContext(), tip + " path = " + path, Toast.LENGTH_LONG).show();
            return;
        }
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = BitmapUtilLib.bmpToByteArray(thumbBmp, false); //bitmap不回收，因为还会返回

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = getWXScene();
        mApi.sendReq(req);
    }

    private void doSendImageBitmap(Bitmap bmp) {
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / THUMB_SIZE, bmp.getHeight() / THUMB_SIZE, true);
        msg.thumbData = BitmapUtilLib.bmpToByteArray(thumbBmp, true);  // 缩略图  bitmap不回收，因为还会返回

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = getWXScene();
        mApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 分享场景
     *
     * @return 聊天界面，朋友圈，微信收藏
     */
    private int getWXScene() {
        switch (mScene) {
            case SocialConstant.WECHAT.ID:
                //发送到聊天界面
                return SendMessageToWX.Req.WXSceneSession;
            case SocialConstant.WECHAT_CIRCLE.ID:
                //发送到朋友圈
                return SendMessageToWX.Req.WXSceneTimeline;
            case SocialConstant.WECHAT_FAVORITE.ID:
                //添加到微信收藏
                return SendMessageToWX.Req.WXSceneFavorite;
            default:
                return SendMessageToWX.Req.WXSceneSession;
        }
    }

    //微信分享默认图片
    private Bitmap getDefaultBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getApplicationContext().getResources(), R.mipmap.ic_launcher);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float scale = (float) Math.max(60.0 / width, 60.0 / height);
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (scale * width), (int) (scale * height), true);
        }
        return bitmap;
    }

    @Override
    public void shareImage(String content, String imagePath, SocialShareListener listener) {
        if (mApi == null) {
            init();
        }
        if (!mApi.isWXAppInstalled()) {
            Toast.makeText(mActivity.getApplicationContext(),
                    mActivity.getApplicationContext().getString(R.string.wechat_not_install), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isNullOrEmpty(imagePath)) {
            doSendLocalImage(imagePath);
        }
    }

    @Override
    public void shareImage(String content, Bitmap bitmap, SocialShareListener listener) {
        if (mApi == null) {
            init();
        }
        if (!mApi.isWXAppInstalled()) {
            Toast.makeText(mActivity.getApplicationContext(),
                    mActivity.getApplicationContext().getString(R.string.wechat_not_install), Toast.LENGTH_SHORT).show();
            return;
        }
        if (bitmap != null) {
            doSendImageBitmap(bitmap);
        }
    }

    @Override
    public void sharePage(String content, SocialShareListener listener, String... extras) {
        if (mApi == null) {
            init();
        }
        if (!mApi.isWXAppInstalled()) {
            Toast.makeText(mActivity.getApplicationContext(),
                    mActivity.getApplicationContext().getString(R.string.wechat_not_install), Toast.LENGTH_SHORT).show();
            return;
        }
        String imageUrl = extras[0];
        String title = extras[1];
        String shareUrl = extras[2];
        if (!StringUtils.isNullOrEmpty(imageUrl)) {
            doSendLocalImage(imageUrl);
        } else {
            doSend(content, getDefaultBitmap(), title, shareUrl);
        }
    }

    @Override
    public String getRegisterAppKey() {
        return mAppKey;
    }

}
