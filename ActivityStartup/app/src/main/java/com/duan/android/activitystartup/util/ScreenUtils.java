package com.duan.android.activitystartup.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/09
 * desc :  获取屏幕宽高等信息、全屏切换、保持屏幕常亮、截屏等
 * version: 1.0
 * </pre>
 */
public class ScreenUtils {

    private static final String LOG_TAG = ScreenUtils.class.getSimpleName();
    static String TAG = "ScreenUtils";
    private static boolean isFullScreen = false;
    private static DisplayMetrics dm = null;

    public static DisplayMetrics displayMetrics(Context context) {
        if (null != dm) {
            return dm;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        LogUtils.printInfo(TAG,"screen width=" + dm.widthPixels +
                                     "px, screen height=" + dm.heightPixels +
                                     "px, densityDpi=" + dm.densityDpi +
                                     ", density=" + dm.density);
        return dm;
    }

    public static int widthPixels(Context context) {
        return displayMetrics(context).widthPixels;
    }

    public static int heightPixels(Context context) {
        return displayMetrics(context).heightPixels;
    }

    public static float density(Context context) {
        return displayMetrics(context).density;
    }

    public static int densityDpi(Context context) {
        return displayMetrics(context).densityDpi;
    }

    public static boolean isFullScreen() {
        return isFullScreen;
    }

    public static void toggleFullScreen(Activity activity) {
        Window window = activity.getWindow();
        int flagFullscreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (isFullScreen) {
            window.clearFlags(flagFullscreen);
            isFullScreen = false;
        } else {
            window.setFlags(flagFullscreen, flagFullscreen);
            isFullScreen = true;
        }
    }

    /**
     * 保持屏幕常亮
     */
    public static void keepBright(Activity activity) {
        // 需在setContentView前调用
        int keepScreenOn = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        activity.getWindow().setFlags(keepScreenOn, keepScreenOn);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager m = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 获取状态栏/通知栏的高度
     *
     * @return px 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        if (context instanceof Activity) {
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            if (statusBarHeight > 0) {
                return statusBarHeight;
            }

            // 反射获取高度
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
                return statusBarHeight;
            } catch (Exception e) {
                Log.e(LOG_TAG, "get status bar height error.");
            }
        }

        // 以上均失效时，使用默认高度为25dp。
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) Math.ceil(25 * metrics.density);
    }

    /**
     * 当前屏幕截图（不包含状态栏）
     *
     * @param activity 当前页面
     * @return bitmap
     */
    public static Bitmap takeScreenshot(Activity activity) {
        if (activity == null) {
            return null;
        }
        View viewScreen = activity.getWindow().getDecorView();
        viewScreen.setDrawingCacheEnabled(true);
        viewScreen.buildDrawingCache();
        Bitmap screenBitmap = viewScreen.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(screenBitmap, 0, 0, screenBitmap.getWidth(), screenBitmap.getHeight());
        viewScreen.destroyDrawingCache();
        return BitmapUtilLib.compressScale(bitmap); //压缩一下
    }

    /**
     * dp -> px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px -> dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
