package com.duan.android.activitystartup.screenshot;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/28
 * desc :
 * version: 1.0
 * </pre>
 */
public class AbsSocial {

    protected H mHandler = new H(Looper.getMainLooper());

    public static final int MSG_SHARE_SUCCESS = 5;
    public static final int MSG_SHARE_FAILED = 6;

    SocialShareInterface.SocialShareListener mShareListener = null;
    public Activity mActivity;
    protected String mAppKey;

    public AbsSocial() {}

    void handleMsg(Message msg) {
        switch (msg.what) {
            case MSG_SHARE_SUCCESS:
                if (mShareListener != null) {
                    mShareListener.onShareSuccess();
                }
                break;
            case MSG_SHARE_FAILED:
                if (mShareListener != null) {
                    mShareListener.onShareFailed();
                }
                break;
            default:
                break;
        }
    }

    //make handler runing in main thread
    class H extends Handler {

        H(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }
            handleMsg(msg);
        }
    }
}
