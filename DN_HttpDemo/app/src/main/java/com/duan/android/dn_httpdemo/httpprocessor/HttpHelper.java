package com.duan.android.dn_httpdemo.httpprocessor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/13
 * desc :  代理类(业务员)
 * version: 1.0
 * </pre>
 */
public class HttpHelper implements IHttpProcessor{


    // TODO: 2019/8/13 单例
    private static HttpHelper instance;
    public static HttpHelper obtain(){
        synchronized (HttpHelper.class){
            if (instance == null){
                instance = new HttpHelper();
            }
        }
        return instance;
    }

    private HttpHelper(){}

    private static IHttpProcessor mIHttpProcessor = null;
    public static void init(IHttpProcessor httpProcessor){
        mIHttpProcessor = httpProcessor;
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallback callback) {

        // 统一格式
        // http://www.aaa.bbb/index
        // user=jett&pwd=123
        // http://www.aaa.bbb/index?user=jett&pwd=123
        // 自己不处理,交给有房的人去处理
        String finalURL= appendParams(url, params);
        mIHttpProcessor.post(finalURL, params, callback);
        //mIHttpProcessor.post(url, params, callback);
    }

    public static String appendParams(String url, Map<String, Object> params){
        if (params==null || params.isEmpty()){
            return url;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        if (urlBuilder.indexOf("?")<=0){
            urlBuilder.append("?");
        }else {
            if (!urlBuilder.toString().endsWith("?")){
                urlBuilder.append("&");
            }
        }

        for (Map.Entry<String,Object> entry: params.entrySet()){
            urlBuilder.append("&"+entry.getKey())
                    .append("=")
                    .append(encode(entry.getValue().toString()));
        }
        return urlBuilder.toString();
    }

    private static String encode(String str){
        try {
            return URLEncoder.encode(str,"utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
