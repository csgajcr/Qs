package com.jcrspace.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.jcrspace.common.Qs;
import com.jcrspace.common.R;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间处理相关工具类
 */
public class DateUtils {


    public final static String yyyyMMddHHmmss = "yyyyMMddhhmmss";



    /**
     * 时间处理<br/>
     * 英文时间<br/>
     * 今天：Hour:Minute                                  9:43<br/>
     * 昨天：yesterday Hour:Minute                 yesterday 15:50<br/>
     * 今年更早：Day/Month/Year Hour:Minute        4/3/2017 13:10<br/>
     * 去年：Day/Month/Year                            4/9/2016<br/>
     * <br/>
     * 中文时间<br/>
     * 今天：时:分                                 9:43<br/>
     * 昨天：昨天 时:分                         昨天 15:50 <br/>
     * 今年更早：*年*月*日 时:分              2017/3/4 13:10<br/>
     * 去年：*年*月*日                         2016/9/4
     *
     * @param longTime
     * @return
     */
    public static String getDateTime(long longTime) {
        String date = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(longTime);

        Calendar nowTime = Calendar.getInstance();
        nowTime.setTimeInMillis(System.currentTimeMillis());

        int year = calendar.get(Calendar.YEAR);
        if (year == nowTime.get(Calendar.YEAR)) {//今年
            int dayX = nowTime.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
            if (dayX == 0) {//今天
                date = getTimeString(longTime, Qs.app.getResources().getString(R.string.date_time_format_day));
            } else if (dayX == 1) {//yes
                date = Qs.app.getResources().getString(R.string.date_time_format_yesterday)
                        + " "
                        + getTimeString(longTime, Qs.app.getResources().getString(R.string.date_time_format_day));
            } else {
                date = getMMddHHmmss(longTime);
            }
        } else { // 非今年的时候，加上年份
            date = getYyyyMMddHHmmss(longTime);
        }
        return date;
    }


    private static String getYyyyMMddHHmmss(long tms) {
        return getTimeString(tms, Qs.app.getResources().getString(R.string.date_time_format1));
    }


    private static String getMMddHHmmss(long tms) {
        return getTimeString(tms,Qs.app.getResources().getString(R.string.date_time_format2));
    }

    private static String getTimeString(long tms, String timeFormat) {
        String sDateTime = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
            Date dt = new Date(tms);
            sDateTime = sdf.format(dt); // 得到精确到秒的表示：08/31/2006 21:08
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sDateTime;
    }
}
