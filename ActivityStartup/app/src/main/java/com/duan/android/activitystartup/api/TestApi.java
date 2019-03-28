package com.duan.android.activitystartup.api;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/21
 * desc :
 * version: 1.0
 * </pre>
 */
public class TestApi {
    // 正式环境
    public static final String HWDZ_URL_ROOT = "http://mb.ihwdz.com/app/";  // 获取数据二者皆可

    // 测试环境
    public static final String TEST_39 = "http://172.16.10.39:8082/app/";   // 提交数据用测试
    public static final String TEST_68 = "http://192.168.1.68:8082/app/";   // 提交数据用测试

    public static final String HWDZ_URL = TEST_39;
}
