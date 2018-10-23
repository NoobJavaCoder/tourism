package com.keendo.biz.service.utils;

import com.keendo.architecture.utils.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {


    /**
     * 通过Date获取当天星期几
     * @param date
     * @return [0, 1, 2, 3, 4, 5, 6] 对应 [周日,周一,周二,周三,周四,周五,周六]
     */
    public static int weekFromDate(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int w = calendar.get(Calendar.DAY_OF_WEEK);

        return w - 1;
    }

    /**
     * 通过Date获取当天星期几
     * @param date
     * @return
     */
    public static String weekdayFromDate(Date date){

        String[] weekdays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

        int i = weekFromDate(date);

        return weekdays[i];
    }

    /**
     * 时间推移days天，days为正往后推，days为负往前推
     * @param date
     * @param days 为正，往后推。
     * @return
     */
    public static Date dateOffset(Date date ,int days){

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);

        Date mewDate = calendar.getTime();

        return mewDate;
    }

    /**
     * 时间推移hours小时，hours为正往后推，hours为负往前推
     * @param date
     * @param hours 为正，往后推。
     * @return
     */
    public static Date hourOffset(Date date, int hours){

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);

        Date mewDate = calendar.getTime();

        return mewDate;
    }

    /**
     * 时间推移minutes分钟，minutes为正往后推，minutes为负往前推
     * @param date
     * @param minutes 为正，往后推。
     * @return
     */
    public static Date minuteOffset(Date date,int minutes){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,minutes);

        Date mewDate = calendar.getTime();

        return mewDate;
    }

    /**
     * 取date当周的开始时间
     * @param date yyyy-MM-dd 00:00:00
     * @return
     */
    public static Date weekStartTime(Date date){

        int w = weekFromDate(date);

        Date startDate = dateOffset(date, -w);

        return dateFloor(startDate);

    }

    /**
     * 取date当周结束时间
     * @param date
     * @return
     */
    public static Date weekEndTime(Date date){

        int w = weekFromDate(date);

        int offset = 6 - w;
        Date endDate = dateOffset(date, offset);

        return dateCeil(endDate);
    }

    /**
     * 当天开始时间
     * @param date
     * @return
     */
    public static Date dateStartTime(Date date){

        String dateStr = dateFormat(date, "yyyy-MM-dd");

        Date dateStartTime = string2Date(dateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");

        return dateStartTime;
    }

    /**
     * 当天结束时间
     * @param date
     * @return
     */
    public static Date dateEndTime(Date date){

        String dateStr = dateFormat(date, "yyyy-MM-dd");

        Date dateEndTime = string2Date(dateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");

        return dateEndTime;
    }

    /**
     * 日期取整 yyyy-MM-dd HH:mm:ss 转为 yyyy-MM-dd 00:00:00
     * @param date
     * @return
     */
    public static Date dateFloor(Date date){

        String dateFormat = dateFormat(date, "yyyy-MM-dd");

        Date newDate = string2Date(dateFormat, "yyyy-MM-dd");

        return newDate;
    }

    /**
     * 日期ceil，yyyy-MM-dd HH:mm:ss 转为 yyyy-MM-dd 23:59:59
     * @param date
     * @return
     */
    public static Date dateCeil(Date date){

        String dateFormat = dateFormat(date, "yyyy-MM-dd 23:59:59");

        Date newDate = string2Date(dateFormat, "yyyy-MM-dd HH:mm:ss");

        return newDate;
    }

    /**
     * 格式化时间。
     * 距当前时间1小时间，返回 x分钟前
     * 距当前时间1小时以上，24小时内，返回 x小时前
     * 距当前时间1天以上，显示 X月X日
     * @param timeSec 秒数
     * @returns {*}
     */
    public final static String formatPubTime(long timeSec)
    {
        long curTime =  System.currentTimeMillis()/1000;
        long tmp = curTime - timeSec;

        if (tmp < 3600)
        {
            return (long)Math.floor(tmp/60) + "分钟前";
        }

        if (tmp < 24*3600)
        {
            return (long)Math.floor(tmp/3600) + "小时前";
        }

        Date date = new Date(timeSec*1000);

        if (date.getYear() == new Date().getYear())
        {
            return formatTimeSecs(timeSec, "M月d日");
        }

        return formatTimeSecs(timeSec,"yyyy年M月d日");
    }

    /**
     * 格式化时间2。
     * 距当前时间1小时间，返回 x分钟前
     * 距当前时间1小时以上，24小时内，返回 x小时前
     * 距当前时间1天以上，显示 X月X日
     * @param timeSec 秒数
     * @returns {*}
     */
    public final static String formatPubTime2(long timeSec)
    {
        long curTime =  System.currentTimeMillis()/1000;
        long tmp = curTime - timeSec;

        if (tmp < 3600)
        {
            return (long)Math.floor(tmp/60) + "分钟前";
        }

        if (tmp < 24*3600)
        {
            Long h = (long)Math.floor(tmp/3600) ;

            tmp = h - (tmp%3600);

            Long m = (long)Math.floor(tmp/60);

            return h + "小时" + m + "分钟前";
        }

        if ( tmp < 7*24*3600 ){
            Long d = (long)Math.floor(tmp/(3600*24));

            tmp =  tmp - (d*24*3600);
            Long h = (long)Math.floor(tmp/3600);

            tmp = tmp - (h*3600);
            Long m = (long)Math.floor(tmp/60);

            return d + "天" + h + "小时" + m + "分钟前";
        }

        Date date = new Date(timeSec*1000);

        if (date.getYear() == new Date().getYear())
        {
            return formatTimeSecs(timeSec, "M月d日HH时mm分");
        }

        return formatTimeSecs(timeSec,"yyyy年M月d日HH时mm分");
    }

    /**
     * 格式化时间秒
     * @param secs 秒数
     * @return
     */
    public final static String formatTimeSecs(long secs, String format)
    {
        Date d = timeSecs2Date(secs);

        return dateFormat(d, format);
    }

    /**
     * 秒转Date对像
     * @param timeSecs
     * @return
     */
    public static Date timeSecs2Date(long timeSecs)
    {
        return new Date(timeSecs * 1000);
    }


    /**
     * 日期字符串转为Date对象
     * @param dateStr
     * @param format
     * @return
     */
    public static Date string2Date(String dateStr, String format){

        if ( format == null ){
            format = "yyyy-MM-dd";
        }

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateStr);
            return date;
        }
        catch (ParseException e)
        {
            Log.w(e.getMessage());
            return null;
        }
    }


    /**
     * 日期格式化
     * @param :format(yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static String dateFormat(Date date, String sFormat)
    {
        if ( sFormat == null)
        {
            sFormat = "yyyy-MM-dd HH:mm:ss";
        }

        java.text.DateFormat format = new SimpleDateFormat(sFormat);
        return format.format(date);
    }

}
