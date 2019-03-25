package com.duan.android.activitystartup.util;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.util.Properties;

/**
 * <pre>
 * author : Duan
 * time : 2019/02/26
 * desc :   清理缓存
 * version: 1.0
 * </pre>
 */
public class CleanManager {

    /**
     * 计算缓存的大小
     */
    private void caculateCacheSize() {
//        long fileSize = 0;
//        String cacheSize = "0KB";
//        File filesDir = getActivity().getFilesDir();
//        File cacheDir = getActivity().getCacheDir();
//
//        fileSize += FileUtil.getDirSize(filesDir);
//        fileSize += FileUtil.getDirSize(cacheDir);
//        // 2.2版本才有将应用缓存转移到sd卡的功能
//        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
//            File externalCacheDir = MethodsCompat
//                    .getExternalCacheDir(getActivity());
//            fileSize += FileUtil.getDirSize(externalCacheDir);
//            fileSize += FileUtil.getDirSize(new File(
//                    org.kymjs.kjframe.utils.FileUtils.getSDCardPath()
//                            + File.separator + "KJLibrary/cache"));
//        }
//        if (fileSize > 0)
//            cacheSize = FileUtil.formatFileSize(fileSize);
//        tvCache.setText(cacheSize);
    }

    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }
    /**
     * 清除app缓存
     */
    public void myclearaAppCache() {
//        DataCleanManager.cleanDatabases(getActivity());
//        // 清除数据缓存
//        DataCleanManager.cleanInternalCache(getActivity());
//        // 2.2版本才有将应用缓存转移到sd卡的功能
//        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
//            DataCleanManager.cleanCustomCache(MethodsCompat
//                    .getExternalCacheDir(getActivity()));
//        }
//        // 清除编辑器保存的临时内容
//        Properties props = getProperties();
//        for (Object key : props.keySet()) {
//            String _key = key.toString();
//            if (_key.startsWith("temp"))
//                removeProperty(_key);
//        }
//        Core.getKJBitmap().cleanCache();
    }

    /**
     * 清除保存的缓存
     */
//    public Properties getProperties() {
//        return AppConfig.getAppConfig(getActivity()).get();
//    }
    public void removeProperty(String... key) {
//        AppConfig.getAppConfig(getActivity()).remove(key);
    }
    /**
     * 清除app缓存
     *
     * @param
     */
    public void clearAppCache() {

//        new Thread() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                try {
//                    myclearaAppCache();
//                    msg.what = CLEAN_SUC;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    msg.what = CLEAN_FAIL;
//                }
//                handler.sendMessage(msg);
//            }
//        }.start();
    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
//                case CLEAN_FAIL:
//                    ToastUtils.show(SxApplication.getInstance(),"清除失败");
//                    break;
//                case CLEAN_SUC:
//                    ToastUtils.show(SxApplication.getInstance(),"清除成功");
//                    break;
            }
        };
    };

}
