package com.duan.android.dn_httpdemo.httpprocessor;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/13
 * desc :  回调的顶层接口（网络接口返回的数据可能是：JSON,XML...）
 * version: 1.0
 * </pre>
 */
public interface ICallback {
    void onSuccess(String result);
    void onFailure(String e);
}
