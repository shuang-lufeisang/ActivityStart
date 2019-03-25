package com.duan.android.activitystartup.base;

import com.duan.android.activitystartup.util.FileUtils;
import com.duan.android.activitystartup.util.Utils;


/**
 * <pre>
 * author : Duan
 * time : 2019/01/07
 * desc :
 * version: 1.0
 * </pre>
 */
public class Constant {

    /**
     *   修改信息: startInfoUpdateActivity  修改- 保存
     *   用户信息
     *   报价-单价
     */
    public interface InfoUpdate{
        int INFO_USER = 0;              // 用户信息
        int INFO_QUOTE_PRICE = 1;       // 报价-单价

    }


    public class status{
        public static final int success = 200;
        public static final int error = -1;
    }


    public static String PATH_DATA = FileUtils.createRootPath(Utils.getApp()) + "/cache";
    public static String PATH_COLLECT = FileUtils.createRootPath(Utils.getApp()) + "/collect";



    /**-------------------------------------键-------------------------------------------------**/
    //Sp键
    public static final String KEY_FIRST_SPLASH = "first_splash";                 //是否第一次启动
    public static final String KEY_IS_LOGIN = "is_login";                         //登录



    /**-------------------------------------腾讯x5页面-------------------------------------------------**/
    public static final String SP_NO_IMAGE = "no_image";
    public static final String SP_AUTO_CACHE = "auto_cache";



}
