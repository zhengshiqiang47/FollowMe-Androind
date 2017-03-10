package com.example.coderqiang.followme.Util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CoderQiang on 2017/2/16.
 */

public class DateUtil {
    private static SimpleDateFormat sf = null;
    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy-MM-dd-");
        return sf.format(d);
    }

    public static String getDateToStringYear(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy");
        return sf.format(d);
    }

    public static String getDateToStringMon(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("MM");
        return sf.format(d);
    }
    public static String getDateToStringDay(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("dd");
        return sf.format(d);
    }

    public static String getDateToStringMD(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("M月d H:m");
        return sf.format(d);
    }

    public static String getDateToStringNormal(long time) {
        int curTime=Integer.parseInt(new String(time+"").substring(0,10));
        if(curTime>=getTimesmorning()){
            Date d = new Date(time);
            sf = new SimpleDateFormat("今天 H:m");
            return sf.format(d);
        }else if(curTime>=getTimesmorning()-24*60*60){
            Date d = new Date(time);
            sf = new SimpleDateFormat("昨天 H:m");
            return sf.format(d);
        }
        Date d = new Date(time);
        sf = new SimpleDateFormat("M.d H:m");;
        return sf.format(d);
    }

    public static int getTimesmorning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis()/1000);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}
