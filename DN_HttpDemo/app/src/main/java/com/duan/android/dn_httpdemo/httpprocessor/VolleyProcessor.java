package com.duan.android.dn_httpdemo.httpprocessor;

import com.android.volley.RequestQueue;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/13
 * desc :
 * version: 1.0
 * </pre>
 */
public class VolleyProcessor implements IHttpProcessor{

    private static RequestQueue mQueue;
    public VolleyProcessor(){}
    @Override
    public void post(String url, Map<String, Object> params, ICallback callback) {
        RequestParams requestParams = new RequestParams(url);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
