package com.duan.android.dn_httpdemo.httpprocessor;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <pre>
 * author : Duan
 * time : 2019/08/13
 * desc : HttpCallback
 * 回调接口的一种实现
 * 用于把网络返回的 -json 字符串- 转换成 -参数类型-
 * 泛型 Result 就是
 * version: 1.0
 * </pre>
 */
public abstract class HttpCallback<Result,T,M,V> implements ICallback {

    @Override
    public void onSuccess(String result) {

        // result 就是网络传回来的数据
        // 把这个json 转成我们需要的对象
        Gson gson = new Gson();
        // 得到JavaBean对应的字节码
        Class<?> clz = analysisClassInfo(this);
        Result objResult = (Result)gson.fromJson(result,clz);
        // objResult 就是我们需要的结果, 把它回调给调用者
        onSuccess(objResult);
    }

    public abstract void onSuccess(Result result);

    // 反射 获取类型
    private Class<?> analysisClassInfo(Object object){
        // TODO: 2019/8/13
        // getGenericSuperclass()得到包含原始类型，参数化，数组，类型变量，基本数据
        Type genType = object.getClass().getGenericSuperclass();
        // 获取参数化类型
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    @Override
    public void onFailure(String e) {

    }
}
