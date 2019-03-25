package com.duan.android.activitystartup.api;

import android.content.Context;

import com.duan.android.activitystartup.propertySheet.PropertyData;

import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/21
 * desc :   获取网络数据
 * version: 1.0
 * </pre>
 */
public class NetDataModel {

    private static NetDataModel model;
    private NetDataApi mApiService;

    public NetDataModel(Context context) {
        mApiService = RetrofitWrapper
                .getInstance(TestApi.HWDZ_URL)
                .create(NetDataApi.class);
    }

    public static NetDataModel getInstance(Context context){
        if(model == null) {
            model = new NetDataModel(context);
        }
        return model;
    }

    /**
     * 获取物价表数据
     * @return
     */
    public Observable<PropertyData> getPropertyData() {
        return mApiService.getPropertyData();
    }
}
