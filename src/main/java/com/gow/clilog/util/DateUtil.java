package com.gow.clilog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期时间帮助类
 * 
 * @author Adam
 * @date 2020/06/09
 */
@SuppressWarnings("all")
public class DateUtil {

    public static final String FMT_DATE_TIME_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final long TICKS_AT_EPOCH = 621355968000000000L;
    private static final long TICKS_PER_MILLISECOND = 10000;
    private static TimeZone timeZone = TimeZone.getDefault();
    public static final String FMT_DATE = "yyyy-MM-dd";
    public static final String FMT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_DATE_HOUR_H_M = "HH:mm";
    public static final String FMT_DATE_YM = "yyyyMM";
    public static final String FMT_DATE_YMD = "yyyyMMdd";
    public static final int UNREACHABLE_TIME = Integer.MAX_VALUE - 100000;

    public static String fromTimeStamp2StringDate(long second) {
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE);
        return sdf.format(new Date(second * 1000));
    }

    public static String fromTimeStamp2StringDateTime(long second) {
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
        return sdf.format(new Date(second * 1000));
    }

    public static String formatDate(long dateTime, String dateFmt) {
        return formatDate(new Date(dateTime), dateFmt);
    }

    public static String formatDate(Date date, String dateFmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
        return sdf.format(date);
    }

    /**
     * 根据Parttern获取一个DateFormat
     * 
     * @param parttern
     * @return
     */
    public static SimpleDateFormat getDateFormat(String parttern) {
        return new SimpleDateFormat(parttern);
    }

    /**
     * 获取下一个小时的指定分钟
     * 
     * @param nextMinute
     * @return
     */
    public static Date getNextMinuteHour(int nextMinute) {
        Calendar c = Calendar.getInstance();

        int currentMinute = c.get(Calendar.MINUTE);
        // 时间超过指定分钟，设置为小一个小时的指定分钟
        if (currentMinute >= nextMinute) {
            int currentHour = c.get(Calendar.HOUR);
            if (currentHour == 23) {
                c.add(Calendar.DATE, 1);
                c.set(Calendar.HOUR, 0);
            } else {
                c.add(Calendar.HOUR, 1);
            }
        }

        c.set(Calendar.MINUTE, nextMinute);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    /**
     * 获取为大于当前时间的整点或者半点时间，忽略秒
     * 
     * @return
     */
    public static Date get0And30Time() {
        Calendar c = Calendar.getInstance();

        int minute = c.get(Calendar.MINUTE);

        if (minute >= 0 && minute < 30) {
            c.add(Calendar.MINUTE, 30 - minute);
        } else {
            c.add(Calendar.MINUTE, 60 - minute);
        }

        return c.getTime();
    }

    /**
     * 获取为大于当前时间的整点时间
     * 
     * @return
     */
    public static Date get0Time() {
        Calendar c = Calendar.getInstance();

        int minute = c.get(Calendar.MINUTE);
        c.add(Calendar.MINUTE, 60 - minute);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取为大于当前时间的整点时间1秒
     * 
     * @return
     */
    public static Date get01Time() {
        Calendar c = Calendar.getInstance();

        int minute = c.get(Calendar.MINUTE);
        c.add(Calendar.MINUTE, 60 - minute);
        c.set(Calendar.SECOND, 1);
        return c.getTime();
    }

    public static int getDayOfYear() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 周日算第7天
     * 
     * @return
     */
    public static int weekOfYear() {
        Calendar c = Calendar.getInstance();
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        int weekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        if (weekDay == 1) {
            weekOfYear--;
        }
        return weekOfYear;
    }

    /**
     * 获取为大于当前时间的整点时间或半的1秒
     * 
     * @return
     */
    public static Date getHmTime() {
        Calendar c = Calendar.getInstance();

        int minute = c.get(Calendar.MINUTE);
        if (minute > 50) {
            c.add(Calendar.MINUTE, 60 - minute);
        } else {
            c.add(Calendar.MINUTE, 50 - minute);
        }
        c.set(Calendar.SECOND, 1);
        return c.getTime();
    }

    // 获取第2天0点30分的时间
    public static Date get30Past0Time() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    // 获取第2天0点10分的时间
    public static Date get10Past0Time() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 10);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    // 获取第2天0点2分的时间
    public static Date get2Past0Time() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 2);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    // 获取第2天0点1分的时间
    public static Date get1Past0Time() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    // 第二天 12:01的时间
    public static Date getNext1201Time() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    // 当前12:01的时间
    public static Date getCurrent1201Time() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    // 获取第2天1点0分的时间
    public static Date get0Past1Time() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    // 获取第2天1点30分的时间
    public static Date get30Past1Time() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    // 获取当前小时的秒
    public static Date getCurrentHourSeconds() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    // 获取下一个是n倍数的小时
    public static Date getNextModNHourTime(int n) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, n - c.get(Calendar.HOUR_OF_DAY) % n);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date get2SecondPast0Time() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 2);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    // 获取第2天0点1分的时间
    public static Date getNextDayPastMinute(int minute) {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    // 获取当天0点n分的时间
    public static Date getTodayPastMinute(int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取当前时间
     * 
     * @return 返回当前时间与协调世界时 1970 年 1 月 1 日午夜之间的时间差（以秒为单位测量）。
     */
    public static int getCurrentTime4Int() {
        return (int)System.currentTimeMillis() / 1000;
    }

    // 获取当前时间
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // 获取当前日期
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    // 获取两个以秒为单位的时间差(单位:天)
    public static int getDiffDay(int beforeDay, int afterDay) {
        int diffSeconds = afterDay - beforeDay;
        return diffSeconds / 60 / 60 / 24;
    }

    // 获取两个以毫秒为单位的时间差(单位:天)
    public static int getDiffDay(long beforeDay, long afterDay) {
        long diffSeconds = afterDay - beforeDay;
        return (int)(diffSeconds / 60 / 60 / 24 / 1000L);
    }

    public static String getTomorrowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date dt = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        return sdf.format(dt);

    }

    public static String getCurrentHourAndMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return sdf.format(new Date());
    }

    public static String getCurrentHourAndMinuteAndSecond() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        return sdf.format(new Date());
    }

    public static int getCurrentIntHourAndMinute() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        return Integer.parseInt(sdf.format(new Date()));
    }

    /**
     * 获得当前日期的总秒数
     * 
     * @return
     */
    public static int getCurrentDaySecond() {
        Date date = new Date();
        SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(hourSdf.format(date));
        SimpleDateFormat minuteSdf = new SimpleDateFormat("mm");
        int minute = Integer.parseInt(minuteSdf.format(date));
        SimpleDateFormat secondSdf = new SimpleDateFormat("ss");
        int second = Integer.parseInt(secondSdf.format(date));

        return hour * 60 * 60 + minute * 60 + second;
    }

    /**
     * 获得当前日
     * 
     * @return
     */
    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    /**
     * 今天是一年中的第几天
     * 
     * @return
     */
    public static int getCurrentDayOfYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 今天是当前周中的第几天
     * 
     * @return
     */
    public static int getCurrentDayOfWeek() {
        Calendar c = Calendar.getInstance();
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay--;
        }
        return weekDay;
    }

    public static String getCurrentday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    public static String getYestesday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    // 昨天 0点0分1秒
    public static String getYestesday0() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
        return sdf.format(c.getTime());
    }

    /**
     * 今天的0点0分0秒
     * 
     * @return 秒
     */
    public static int getToday0Second() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
        try {
            return (int)(sdf.parse(sdf.format(c.getTime())).getTime() / 1000);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 今天的0点0分0秒
     * 
     * @return 毫秒
     */
    public static long getToday0Millis() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
        try {
            return sdf.parse(sdf.format(c.getTime())).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    // 昨天 23点59分59秒
    public static String getYestesday23() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
        return sdf.format(c.getTime());
    }

    public static String getNextDate4Num(int n) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, n);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    public static String getNextDate4Num(String date, int n) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        cal.add(Calendar.DATE, n);
        return sdf.format(cal.getTime());
    }

    /**
     * 返回当前多少秒时间
     */
    public static int getTodaySecond(int second) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, second);
        return (int)(c.getTimeInMillis() / 1000);

    }

    /**
     * 通过格式，日期str获取秒
     * 
     * @param format
     * @param date
     * @return
     */
    public static int getSecondByDateStr(String format, String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return (int)(sdf.parse(date).getTime() / 1000);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getSecondByDateStr(String date) {
        return getSecondByDateStr(FMT_DATE_TIME, date);
    }

    public static int getPvPSecondByDateStr(String date) {
        return getSecondByDateStr(FMT_DATE_HOUR_H_M, date);
    }

    public static Date str2Data(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
        return sdf.parse(str);
    }

    public static long str2Long(String str) {
        try {
            if (str.length() == 10) {
                str = str + " 00:00:00";
            }
            return str2Data(str).getTime();
        } catch (ParseException e) {
            SystemLogger.error("DateUtil.str2Long >>> parse date error. str:{}", str);
        }
        return 0;
    }

    public static int str2Int(String str) {
        return (int)(str2Long(str) / 1000);
    }

    /**
     * 获取当年的最后一天的59分59秒Date类型，如果有需要则截取转换
     * 
     * @return
     */
    public static Date getLastDateOfYear() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.SECOND, -1);
        return c.getTime();
    }

    /**
     * 获取下一年第一天的00分00秒Date类型，如果有需要则截取转换
     * 
     * @return
     */
    public static Date getFirstDateOfNextYear() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    // 获取当前时间
    public static String getCurrentTimeOfStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    // 获取当前日期
    public static String getCurrentDateOfStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate
     *            较小的时间
     * @param bdate
     *            较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate
     *            较小的时间
     * @param bdate
     *            较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(int smdateSecond, int bdateSecond) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date smdate = sdf.parse(sdf.format(new Date(smdateSecond * 1000L)));
        Date bdate = sdf.parse(sdf.format(new Date(bdateSecond * 1000L)));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 当前时间和date日期的下一天的零点30分比较
     * 
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int betweenNextDayZeroHalf(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date));
        long time1 = cal.getTimeInMillis() / 1000 + (long)(24.5 * 60 * 60);
        long time2 = getCurrentTime4Int();
        long between_times = time2 - time1;
        return Integer.parseInt(String.valueOf(between_times));

    }

    public static long dayTimesBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FMT_DATE_TIME);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));

            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            return time2 - time1;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 获取当天剩余的时间(分钟)
     * 
     * @return
     */
    public static int getDayLeftMinute() {
        Calendar c = Calendar.getInstance();
        int leftMinute = 12 * 60 - (c.get(Calendar.HOUR) * 60 + c.get(Calendar.MINUTE));
        if (c.get(Calendar.AM_PM) == 0) {
            leftMinute += 12 * 60;
        }
        return leftMinute;
    }

    /**
     * 周一为1
     * 
     * @return
     */
    public static int getDayOfWeek() {
        Calendar c = Calendar.getInstance();
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay--;
        }
        return weekDay;
    }

    public static int getDayOfWeek(String fmt, String str) {
        Calendar c = Calendar.getInstance();
        c.setTime(getDateByStr(fmt, str));
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay--;
        }
        return weekDay;
    }

    /**
     * 周一为1
     * 
     * @return
     */
    public static int getDayOfWeek(int second) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(second * 1000L);
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay--;
        }
        return weekDay;
    }

    /**
     * 获取当前周周一的日期
     * 
     * @param fmt
     * @param str
     * @return
     */
    public static Date getWeekFirstDate(String fmt, String str) {
        Calendar c = Calendar.getInstance();
        c.setTime(getDateByStr(fmt, str));
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay--;
        }
        c.add(Calendar.DATE, -weekDay + 1);
        return c.getTime();
    }

    /**
     * 获取本周的开始时间戳
     */
    public static int getWeekStartTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 1);
        return parseTime("yyyy-MM-dd", simpleDateFormat.format(cal.getTime()));
    }

    /**
     * 本周结束时间戳
     * 
     * @return
     */
    public static int getWeekEndTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 7);
        return parseTime("yyyy-MM-dd HH:mm:ss", simpleDateFormat.format(cal.getTime()) + " 23:59:59");
    }

    public static String addHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    public static int addHour2Int(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        return (int)(c.getTimeInMillis() / 1000);
    }

    public static String addHourAndMinute(Date date, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        c.add(Calendar.MINUTE, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    /**
     * 一周168小时，获得当前时间
     * 
     * @return
     */
    public static int getHourOfWeek() {
        int weekDay = getDayOfWeek();
        return (weekDay - 1) * 24 + getCurrentHour();
    }

    /**
     * 时间是否超出指定时间
     * 
     * @param time
     *            比较时间
     * @param outimes
     *            超出时间（秒）
     * @return
     */
    public static boolean isTimeout(long time, int outimes) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, -outimes);

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // System.out.println( sdf.format(c.getTimeInMillis()));
        // System.out.println( sdf.format(time));

        return time < c.getTimeInMillis();
    }

    public static int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static int getCurrentSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    public static Date getDateByStr(String fmt, String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getNDayBefore4Int(int n) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DAY_OF_MONTH, -n);
        return (int)(c.getTimeInMillis() / 1000);
    }

    public static String long2Date(long mill) {
        String strs = "";
        try {
            Date date = new Date(mill);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strs = sdf.format(date);
        } catch (Exception e) {
            SystemLogger.error("DateUtil.long2Date >>> mill to date error. mill:{}", mill);
        }
        return strs;
    }

    public static String int2Date(int second) {
        Date date = new Date(second * 1000l);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            strs = sdf.format(date);
        } catch (Exception e) {
            SystemLogger.error("DateUtil.int2Date >>> second to date error. second:{}", second);
        }
        return strs;
    }

    public static int parseTime(String format, String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return (int)(sdf.parse(time).getTime() / 1000);
        } catch (Exception e) {
            SystemLogger.error("DateUtil.int2Date >>> parse time error. format:{}, time:{}", format, time);
        }
        return 0;
    }

    /**
     * 2个秒数的之间 精确小时差 注意 ！！！ 误差秒(既超过1秒 额外加1小时)
     * 
     * @return 一般情况下 返回最小值1
     */
    public static int getDiffAccurateHour(int beforeSeconds, int afterSeconds) {
        int diffSeconds = afterSeconds - beforeSeconds;
        int hour = 60 * 60;
        return diffSeconds / hour + (diffSeconds % hour == 0 ? 0 : 1);
    }

    /**
     * 2个秒数的之间 精确天数查 注意 ！！！ 误差秒(既超过1秒 额外加1天)
     * 
     * @return 一般情况下 返回最小值1
     */
    public static int getDiffAccurateDay(int beforeSeconds, int afterSeconds) {
        int diffSeconds = afterSeconds - beforeSeconds;
        int day = 24 * 60 * 60;
        return diffSeconds / day + (diffSeconds % day == 0 ? 0 : 1);
    }

    /**
     * 获取当前年月日的数字值 <br>
     * 格式:yyyyMMdd,如:20170303
     * 
     * @return
     */
    public static int yyyyMMdd4Now() {
        return yyyyMMdd4Date(System.currentTimeMillis());
    }

    /**
     * 获取目标日期的年月日的数字值 <br>
     * 格式:yyyyMMdd,如:20170303
     * 
     * @param date
     *            时间
     * @return
     */
    public static int yyyyMMdd4Date(Date date) {
        return yyyyMMdd4Date(date.getTime(), 0);
    }

    /**
     * 获取目标日期的年月日的数字值 <br>
     * 格式:yyyyMMdd,如:20170303
     * 
     * @param dateTime
     *            目标时间
     * @return
     */
    public static int yyyyMMdd4Date(long dateTime) {
        return yyyyMMdd4Date(dateTime, 0);
    }

    /**
     * 获取目标日期的年月日的数字值 <br>
     * 格式:yyyyMMdd,如:20170303
     * 
     * @param dateTime
     *            目标时间
     * @param addDay
     *            操作天数,可增加天数,也可减天数
     * @return
     */
    public static int yyyyMMdd4Date(long dateTime, int addDay) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(dateTime);
        if (addDay != 0) {
            rightNow.add(Calendar.DATE, addDay);
        }
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH) + 1;
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        return Integer
            .parseInt(String.valueOf(year) + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day));
    }

    /**
     * 获取目标日期的年月日时的数字值 <br>
     * 格式:yyyyMMddHH,如:2017030301
     * 
     * @param dateTime
     * @return
     */
    public static int yyyyMMddHH4Date(long dateTime) {
        if (dateTime <= 0) {
            return 0;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(dateTime);
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH) + 1;
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        return Integer.parseInt(String.valueOf(year) + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day)
            + (hour < 10 ? "0" + hour : hour + ""));
    }

    /**
     * 获取目标日期的年月日时分秒数字值 <br>
     * 格式:yyyyMMddHHmmss,如:20171018104011
     * 
     * @param dateTime
     * @return
     */
    public static long yMdHms4Date(long dateTime) {
        if (dateTime <= 0) {
            return 0;
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeInMillis(dateTime);
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH) + 1;
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        int second = rightNow.get(Calendar.SECOND);
        StringBuilder sbVal = new StringBuilder();
        sbVal.append(year).append(month < 10 ? "0" + month : month).append(day < 10 ? "0" + day : day)
            .append(hour < 10 ? "0" + hour : hour + "").append(minute < 10 ? "0" + minute : minute + "")
            .append(second < 10 ? "0" + second : second + "");
        return Long.parseLong(sbVal.toString());
    }

    // 获取第2天0点1分的时间
    public static Date getNextDayPastSecond(int second) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, second);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * 根据纳秒时间计算消耗时间,返回毫秒
     * 
     * @param startNano
     *            纳秒
     * @return
     */
    public static double getCostTime(long startNano) {
        double costTime = System.nanoTime() - startNano;
        double costMillis = costTime / 1000000;
        return costMillis;
    }

    /**
     * 年的第几周
     * 
     * @param dateTime
     *            秒
     * @return
     */
    public static int getWeekOfYear(Integer dateTime) {
        Calendar rightNow = Calendar.getInstance();
        if (dateTime != null && dateTime > -1) {
            rightNow.setTimeInMillis(dateTime * 1000L);
        }
        int week = rightNow.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    /**
     * 年的第几周
     * 
     * @return
     */
    public static int getWeekOfYear() {
        return getWeekOfYear(null);
    }

    /**
     * 获取当前时间的前一天日期
     * 
     * @return
     */
    public static Date getPreviousDay() {
        return getPreviousDay(System.currentTimeMillis());
    }

    /**
     * 根据指定时间获取前一天日期
     * 
     * @param time
     * @return
     */
    public static Date getPreviousDay(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * 根据指定时间获取前 x 天日期
     * 
     * @param time
     *            指定时间
     * @param prevDay
     *            前 x 天
     * @return
     */
    public static Date getPreviousDay(long time, int prevDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        cal.add(Calendar.DATE, -Math.abs(prevDay));
        return cal.getTime();
    }

    /**
     * 获取某个日期的 00:00:00
     * 
     * @param date
     * @return
     */
    public static Date getStartTimeOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }

    /**
     * 获取某个日期的 23:59:59
     * 
     * @param date
     * @return
     */
    public static Date getEndTimeOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        return cal.getTime();
    }

    /**
     * 获取当前日期 在之后最近的周几
     * 
     * @param fmt
     *            格式化
     * @param dateTime
     *            日期时间
     * @param dayOfWeek
     *            周几 周一为1
     * @return
     */
    public static int getNearDayOfWeek(String fmt, String dateTime, int dayOfWeek) {
        Calendar c = Calendar.getInstance();
        c.setTime(getDateByStr(fmt, dateTime));
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay--;
        }
        if (weekDay < dayOfWeek) {
            c.add(Calendar.DATE, dayOfWeek - weekDay);
        } else if (weekDay == dayOfWeek) {
            // 什么也不用做
        } else {
            c.add(Calendar.DATE, (7 - weekDay) + dayOfWeek);
        }
        return (int)(c.getTimeInMillis() / 1000);
    }

    public static int getSecondTimestamp(Date date) {
        if (date == null) {
            return 0;
        }
        return (int)(date.getTime() / 1000);
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate
     *            较小的时间
     * @param bdate
     *            较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static String addDaysForStr(String formate, String date, int days) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        Date addDate = addDaysForDate(formate, date, days);
        return sdf.format(addDate);
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate
     *            较小的时间
     * @param bdate
     *            较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static Date addDaysForDate(String formate, String date, int days) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        Date pDate = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * 获取当前时间下个小时目标分钟的表示
     * 
     * @param minute
     *            目标分钟
     * @return
     */
    public static Date getNextHourTimeByMinute(int minute) {
        return getNextHourTimeByMinute(System.currentTimeMillis(), minute);
    }

    /**
     * 获取当前时间下个小时目标分钟的表示
     * 
     * @param nowTime
     *            当前时间
     * @param minute
     *            目标分钟
     * @return
     */
    public static Date getNextHourTimeByMinute(long nowTime, int minute) {
        return getNextHourTimeByMinute(new Date(nowTime), minute);
    }

    /**
     * 获取当前时间下个小时目标分钟的表示
     * 
     * @param nowDate
     *            当前时间
     * @param minute
     *            目标分钟
     * @return
     */
    public static Date getNextHourTimeByMinute(Date nowDate, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(nowDate);
        // 设置目标时间
        c.add(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, Math.abs(minute));
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取当前时间向前日期当前小时的开始时间表示
     * 
     * @param nowTime
     *            当前时间
     * @param prevDay
     *            向前日期
     * @return
     */
    public static Date getPreviousDayHourBeginTime(long nowTime, int prevDay) {
        return getPreviousDayHourBeginTime(new Date(nowTime), prevDay);
    }

    /**
     * 获取当前时间向前日期当前小时的开始时间表示
     * 
     * @param nowDate
     *            当前时间
     * @param prevDay
     *            向前日期
     * @return
     */
    public static Date getPreviousDayHourBeginTime(Date nowDate, int prevDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(nowDate);
        // 向前日期
        c.add(Calendar.DATE, -Math.abs(prevDay));
        // c.add(Calendar.HOUR_OF_DAY, -1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取当前时间向前日期当前小时的结束时间表示
     * 
     * @param nowTime
     *            当前时间
     * @param prevDay
     *            向前日期
     * @return
     */
    public static Date getPreviousDayHourEndTime(long nowTime, int prevDay) {
        return getPreviousDayHourEndTime(new Date(nowTime), prevDay);
    }

    /**
     * 获取当前时间向前日期当前小时的结束时间表示
     * 
     * @param nowDate
     *            当前时间
     * @param prevDay
     *            向前日期
     * @return
     */
    public static Date getPreviousDayHourEndTime(Date nowDate, int prevDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(nowDate);
        // 向前日期
        c.add(Calendar.DATE, -Math.abs(prevDay));
        // c.add(Calendar.HOUR_OF_DAY, -1);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 获取目标时间当前小时的开始时间表示
     * 
     * @param destDate
     *            目标时间
     * @return
     */
    public static Date getDestDayHourBeginTime(Date destDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(destDate);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取目标时间当前小时的结束时间表示
     * 
     * @param destDate
     *            目标时间
     * @return
     */
    public static Date getDestDayHourEndTime(Date destDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(destDate);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 返回当前日期的+几天
     * 
     * @param day
     *            增加或减少几天
     * @return 返回单位 秒
     */
    public static int getDateAddDay(int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, day);
        return (int)(c.getTimeInMillis() / 1000);
    }

    /**
     * 是否是同一天
     * 
     * @param time1
     *            时间1（秒）
     * @param time2
     *            时间2（秒）
     * @return
     */
    public static boolean isSameDay(long time1, long time2) {
        long t1 = time1 * 1000;
        long t2 = time2 * 1000;
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(t1);
        Calendar cb = Calendar.getInstance();
        cb.setTimeInMillis(t2);
        return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR) && ca.get(Calendar.MONTH) == cb.get(Calendar.MONTH)
            && ca.get(Calendar.DAY_OF_MONTH) == cb.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取本周的开始时间戳
     */
    public static int getWeekStartTimeBySec() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int)(cal.getTime().getTime() / 1000);
    }

    /**
     * 本周结束时间戳
     * 
     * @return
     */
    public static int getWeekEndTimeBySec() {
        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 7);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return (int)(cal.getTime().getTime() / 1000);
    }

    /**
     * 获取startTimeStr参数days天之后的String
     */
    public static String getAfterDays(String startTimeStr, int days) {

        String endTimeStr = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(startTimeStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        date = calendar.getTime();
        endTimeStr = sdf.format(date);
        return endTimeStr;
    }

    /**
     * 获取目标时间下一周第X天的整点时间, 周一为第一天
     * 
     * @param date
     *            目标时间
     * @param nextWeekDay
     *            下一周第X天
     * @return
     */
    public static Date getNextWeekDay(Date date, int nextWeekDay) {
        nextWeekDay = nextWeekDay < 1 ? 1 : (nextWeekDay > 7 ? 7 : nextWeekDay);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek = 7;
        } else {
            dayOfWeek--;
        }
        int addDay = 7 - dayOfWeek + nextWeekDay;
        c.add(Calendar.DATE, addDay);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取目标时间下一周第X天的整点时间秒, 周一为第一天
     * 
     * @param date
     *            目标时间
     * @param nextWeekDay
     *            下一周第X天
     * @return
     */
    public static int getNextWeekDaySec(Date date, int nextWeekDay) {
        Date destDate = getNextWeekDay(date, nextWeekDay);
        return (int)(destDate.getTime() / 1000);
    }

    /**
     * 打印日志日期
     */
    // public static final int[] INIT_LOG_DAY = new int[] {1,8,15,22,29};

    /**
     * 打印日志日期
     */
    public static final int[] INIT_LOG_DAY_OF_WEEK = new int[] {2, 3};

    /**
     * 打印日志的时间
     */
    public static final int[] INIT_LOG_HOUR = new int[] {10};

    /**
     * 判断当前时间是否打印initLog
     * 
     * @return
     */
    public static boolean sendLog() {
        if (INIT_LOG_DAY_OF_WEEK.length < 1 || INIT_LOG_HOUR.length < 1) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        // if(c.get(Calendar.DAY_OF_MONTH) != INIT_LOG_DAY || c.get(Calendar.HOUR_OF_DAY) != INIT_LOG_HOUR){
        // return false;
        // }
        // int day = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = getDayOfWeek();
        // 为了方便理解 所以单独定义一个boolean值
        boolean isDay = false;
        for (int di = 0; di < INIT_LOG_DAY_OF_WEEK.length; di++) {
            if (dayOfWeek == INIT_LOG_DAY_OF_WEEK[di]) {
                // day数组中 如果是当天
                isDay = true;
                break;
            }
        }
        if (isDay) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            for (int hi = 0; hi < INIT_LOG_HOUR.length; hi++) {
                if (hour == INIT_LOG_HOUR[hi]) {
                    // day数组中 如果是当天 且 满足时间则返回真 否则假
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * CSharp时间转java时间
     * 
     * @param csharpTime
     * @return
     */
    public static Date csharpTime2JavaTime(long csharpTime) {
        Calendar c = Calendar.getInstance(timeZone);
        c.setTimeInMillis((csharpTime - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND);
        c.setTimeInMillis(c.getTimeInMillis() - c.getTimeZone().getRawOffset());
        return c.getTime();
    }

    /**
     * 计算消耗时间, 单位:毫秒
     * 
     * @param startNano
     *            开始时间, 单位:纳秒
     * @param endNano
     *            结束时间, 单位:纳秒
     * @return
     */
    public static float costMillis(long startNano, long endNano) {
        return (float)((endNano - startNano) * 1.0f) / 1000000.0f;
    }

    /**
     * 获取指定时间当天零点
     * 
     * @param seconds
     *            秒
     * @return 返回单位:秒
     */
    public static int getZeroBySeconds(int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(seconds * 1000L);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int)(cal.getTimeInMillis() / 1000L);
    }

    /**
     * 获取指定时间当天零点
     *
     * @param millisecond
     *            毫秒
     * @return 返回单位:毫秒
     */
    public static long getZeroByMillSeconds(long millisecond) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millisecond);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

}
