package com.hnust.wxsell.utils;

import java.text.DateFormat;
import java.text.ParsePosition;
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

    /**
     * Date-String
     * @param date
     * @return
     */
    public static String getDateToStr(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(date);
    }

    /**
     * String-Date
     * @param date
     * @return
     */
    public static Date getStrToDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ParsePosition pos = new ParsePosition(0);
        Date strtodate = df.parse(date, pos);
        return strtodate;
    }
}
