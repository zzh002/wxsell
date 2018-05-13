package com.hnust.wxsell.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Create by HJT
 * 2018/3/16 15:54
 **/
public class DateUtil {

    /**
     * 获取当天时间
     * @return
     */
    public static String getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(today);
    }

    /**
     * 获取隔天时间
     * @return
     */
    public static String getTomorrowTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        Date tomorrow = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(tomorrow);
    }

    /**
     * .....
     * @param date
     * @return
     */
    public static String getTimeBefore(Integer date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -date);
        Date tomorrow = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(tomorrow);
    }
}
