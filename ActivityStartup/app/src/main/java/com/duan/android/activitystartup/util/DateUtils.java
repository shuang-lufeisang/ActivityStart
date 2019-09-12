package com.duan.android.activitystartup.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/21
 * desc : 日期、时间展示
 * version: 1.0
 * </pre>
 */
public class DateUtils {

    static String TAG = "DateUtils";
    ///////Date Time String Converter//////

    // yyyyMMdd  20180803
    static public String getDateAsName() {
        return getDateAsName(new Date());
    }
    static public String getDateAsName(Date date) {
        return getDateTimeFormattedString(date, "yyyyMMdd", Locale.US);
    }

    // yyyyMMdd-hhmmss  20180803-033555
    static public String getDateTimeAsName() {
        return getDateTimeAsName(new Date());
    }
    static public String getDateTimeAsName(Date date) {
        return getDateTimeFormattedString(date, "yyyyMMdd-hhmmss", Locale.US);
    }

    // "yyyy-MM-dd' 'kk:mm"  2018-08-03 15:35
    static public String getDateTimeDisplayString() {
        return getDateTimeFormattedString(new Date(), "yyyy-MM-dd' 'kk:mm", Locale.US);
    }

    // "yyyy/MM/dd"  2018/08/03
    static public String getDateTodayString() {
        //return getDateTimeFormattedString(new Date(), "yyyy/MM/dd", Locale.CHINESE);
        return getDateTimeFormattedString(new Date(), "yyyy-MM-dd", Locale.CHINESE);
    }


    static public String getDateTimeFormattedString(Date date, String template, Locale locale) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(template, locale);
            String dateString = dateFormat.format(date);
            return dateString;
        }catch (Exception e)
        {
            return e.getMessage();
        }
    }

    /**
     *  判断日期大小
     * 参数格式: yyyy-MM-dd hh:mm
     * @param DATE1
     * @param DATE2
     * @return 0 -相等；1-DATE1 更晚；-1 -DATE2 更晚
     */
    static public int compareDate(String DATE1, String DATE2){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                //System.out.println("dt1 在dt2前");
                LogUtils.printCloseableInfo(TAG, DATE1 +"更晚于："+DATE2);
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //System.out.println("dt1在dt2后");

                LogUtils.printCloseableInfo(TAG, DATE1 +"更早于："+DATE2);
                return -1;
            } else {
                LogUtils.printCloseableInfo(TAG, DATE1 +"等于："+DATE2);
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    /**
     *  判断日期比此刻大小
     * 参数格式: yyyy-MM-dd hh:mm
     * @param DATE
     * @return 0 -相等；1-比此刻晚；-1 -比此刻早
     */
    static public int compareToday(String DATE){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE);
            Date today = new Date();
            if (dt1.getTime() > today.getTime()) {
                LogUtils.printCloseableInfo(TAG, DATE +"比此刻晚");
                return 1;
            } else if (dt1.getTime() < today.getTime()) {
                LogUtils.printCloseableInfo(TAG, DATE +"比此刻早");
                return -1;
            } else {
                LogUtils.printCloseableInfo(TAG, DATE +"正是此刻");
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }




    // 毫秒转化成 日期格式
    public static String getDateFromMillisecond(String millisecond){
        if(millisecond == null)
            return " ";
        else{
            Date date = new Date();
            try{
                date.setTime(Long.parseLong(millisecond));
            }catch(NumberFormatException nfe){
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(date);
        }
    }
    // 秒转化成 日期格式
    public static String getDateFromSecondsString(String seconds){
        if(seconds == null)
            return " ";
        else{
            Date date = new Date();
            try{
                date.setTime(Long.parseLong(seconds)*1000); // 转为millisecond
            }catch(NumberFormatException nfe){
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }
    // 毫秒转化成 日期格式
    public static String getDateFromMilliSecondsLong(long millisecond){
        if(millisecond <= 0)
            return " ";
        else{
            Date date = new Date();
            try{
                date.setTime(millisecond);    // 转为millisecond
            }catch(NumberFormatException nfe){
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }
    // 秒转化成 日期格式
    public static String getDateFromSecondsLong(long seconds){
        if(seconds <= 0)
            return " ";
        else{
            Date date = new Date();
            try{
                date.setTime(seconds*1000);    // 转为millisecond
            }catch(NumberFormatException nfe){
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }

    static public String getStringWithResourceName(Context context, String resourceName) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(resourceName,
                "string", packageName);
        return context.getString(resId);
    }

    public static String DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static String DATE_Y_M_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    /** 获取时间戳 */
    static public long getLongDate(String dateStr, String format){
        if (StringUtils.isNotBlank(dateStr) && StringUtils.isNotBlank(format)){

            SimpleDateFormat sf = new SimpleDateFormat(format);
            try {
                return sf.parse(dateStr).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0l;
    }

}
