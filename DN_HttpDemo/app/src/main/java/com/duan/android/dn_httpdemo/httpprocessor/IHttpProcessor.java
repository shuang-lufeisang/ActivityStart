package com.duan.android.dn_httpdemo.httpprocessor;

import java.util.Map;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/13
 * desc :   房产公司
 * version: 1.0
 * </pre>
 */
public interface IHttpProcessor {
    /**
     * 网络操作 get post del update put select
     */
    void post(String url, Map<String,Object> params, ICallback callback);
}
