package com.duan.android.activitystartup.api;

import com.duan.android.activitystartup.propertySheet.PropertyData;

import retrofit2.http.GET;
import rx.Observable;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/21
 * desc :   网络数据
 * version: 1.0
 * </pre>
 */
public interface NetDataApi {

    /**
     * 物价表数据
     * http://mb.ihwdz.com/app/attr/home
     * @return
     */
    @GET("attr/home")
    Observable<PropertyData> getPropertyData();
}
