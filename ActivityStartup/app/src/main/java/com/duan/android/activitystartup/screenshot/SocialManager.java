package com.duan.android.activitystartup.screenshot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/28
 * desc :
 * version: 1.0
 * </pre>
 */
public class SocialManager {
    private SocialShareInterface mWeChat;             // 微信
    private SocialShareInterface mWeChatCircle;       // 微信朋友圈
    private SocialShareInterface mWeChatFavorite;     // 微信收藏
    private Context mContext;

    public SocialManager(Activity ctx) {
        mWeChat = new WeChatSocialImpl(ctx, SocialConstant.WECHAT.ID);
        mWeChatCircle = new WeChatSocialImpl(ctx, SocialConstant.WECHAT_CIRCLE.ID);
        mWeChatFavorite = new WeChatSocialImpl(ctx, SocialConstant.WECHAT_FAVORITE.ID);
        mContext = ctx.getApplicationContext();
    }

    public void sharePage(int type, String share, SocialShareInterface.SocialShareListener listener, String... extras) {
        switch (type) {
            case SocialConstant.WECHAT.ID:
                mWeChat.sharePage(share, listener, extras);
                break;
            case SocialConstant.WECHAT_CIRCLE.ID:
                mWeChatCircle.sharePage(share, listener, extras);
                break;
            case SocialConstant.WECHAT_FAVORITE.ID:
                mWeChatFavorite.sharePage(share, listener, extras);
            default:
                break;
        }
    }

    public void shareImage(int type, String title, String imgPath, SocialShareInterface.SocialShareListener listener) {
        switch (type) {
            case SocialConstant.WECHAT.ID:
                mWeChat.shareImage(title, imgPath, listener);
                break;
            case SocialConstant.WECHAT_CIRCLE.ID:
                mWeChatCircle.shareImage(title, imgPath, listener);
                break;
            default:
                break;
        }
    }

    public void shareImage(int type, String title, Bitmap bitmap, SocialShareInterface.SocialShareListener listener) {
        switch (type) {
            case SocialConstant.WECHAT.ID:
                mWeChat.shareImage(title, bitmap, listener);
                break;
            case SocialConstant.WECHAT_CIRCLE.ID:
                mWeChatCircle.shareImage(title, bitmap, listener);
                break;
            default:
                break;
        }
    }
}
