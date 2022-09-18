package com.pda.utils;

import cn.hutool.core.util.ObjectUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 谭思涛
 * @Description:
 * @Date: 2020/12/1
 * @since JDK 1.8
 */
public class DateUtil {

    // ==格式到年==
    /**
     * 日期格式，年份，例如：2004，2008
     */
    public static final String DATE_FORMAT_YYYY = "yyyy";

    // ==格式到年月 ==
    /**
     * 日期格式，年份和月份，例如：200707，200808
     */
    public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

    /**
     * 日期格式，年份和月份，例如：200707，2008-08
     */
    public static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";

    // ==格式到年月日==
    /**
     * 日期格式，年月日，例如：050630，080808
     */
    public static final String DATE_FORMAT_YYMMDD = "yyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：06-12-25，08-08-08
     */
    public static final String DATE_FORMAT_YY_MM_DD = "yy-MM-dd";

    /**
     * 日期格式，年月日，例如：20050630，20080808
     */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
     */
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式，年月日，例如：2016.10.05
     */
    public static final String DATE_FORMAT_POINTYYYYMMDD = "yyyy.MM.dd";

    /**
     * 日期格式，年月日，例如：2016年10月05日
     */
    public static final String DATE_TIME_FORMAT_YYYY年MM月DD日 = "yyyy年MM月dd日";

    // ==格式到年月日 时分 ==

    /**
     * 日期格式，年月日时分，例如：200506301210，200808081210
     */
    public static final String DATE_FORMAT_YYYYMMDDHHmm = "yyyyMMddHHmm";

    /**
     * 日期格式，年月日时分，例如：20001230 12:00，20080808 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDD_HH_MI = "yyyyMMdd HH:mm";

    /**
     * 日期格式，年月日时分，例如：2000-12-30 12:00，2008-08-08 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";

    // ==格式到年月日 时分秒==
    /**
     * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";

    /**
     * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";

    // ==格式到年月日 时分秒 毫秒==
    /**
     * 日期格式，年月日时分秒毫秒，例如：20001230120000123，20080808200808456
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS";

    // ==特殊格式==
    /**
     * 日期格式，月日时分，例如：10-05 12:00
     */
    public static final String DATE_FORMAT_MMDDHHMI = "MM-dd HH:mm";

	/* ************工具方法*************** */

    /**
     * 获取某日期的年份
     *
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取某日期的月份
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某日期的日数
     *
     * @param date
     * @return
     */
    public static Integer getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DATE);// 获取日
        return day;
    }

    /**
     * 格式化Date时间位yyyy-MM-dd HH:mm:ss
     *
     * @param date Date类型时间
     * @return 格式化后的字符串
     */
    public static String formatDateToStr(Date date) {
        return parseDateToStr(date, DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
    }

    /**
     * 解析yyyy-MM-dd HH:mm:ss为时间
     *
     * @param dateStr 时间字符串
     * @return 格式化后的字符串
     */
    public static Date praseStrToDate(String dateStr) {
        return parseStrToDate(dateStr, DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
    }

    /**
     * 格式化Date时间
     *
     * @param time
     *            Date类型时间
     * @param timeFromat
     *            String类型格式
     * @return 格式化后的字符串
     */
    public static String parseDateToStr(Date time, String timeFromat) {
        DateFormat dateFormat = new SimpleDateFormat(timeFromat);
        return dateFormat.format(time);
    }

    /**
     * 格式化Timestamp时间
     *
     * @param timestamp
     *            Timestamp类型时间
     * @param timeFromat
     * @return 格式化后的字符串
     */
    public static String parseTimestampToStr(Timestamp timestamp, String timeFromat) {
        SimpleDateFormat df = new SimpleDateFormat(timeFromat);
        return df.format(timestamp);
    }

    /**
     * 格式化Date时间
     *
     * @param time
     *            Date类型时间
     * @param timeFromat
     *            String类型格式
     * @param defaultValue
     *            默认值为当前时间Date
     * @return 格式化后的字符串
     */
    public static String parseDateToStr(Date time, String timeFromat, final Date defaultValue) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFromat);
            return dateFormat.format(time);
        } catch (Exception e) {
            if (defaultValue != null)
                return parseDateToStr(defaultValue, timeFromat);
            else
                return parseDateToStr(new Date(), timeFromat);
        }
    }

    /**
     * 格式化Date时间
     *
     * @param time
     *            Date类型时间
     * @param timeFromat
     *            String类型格式
     * @param defaultValue
     *            默认时间值String类型
     * @return 格式化后的字符串
     */
    public static String parseDateToStr(Date time, String timeFromat, final String defaultValue) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFromat);
            return dateFormat.format(time);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 格式化String时间
     *
     * @param time
     *            String类型时间
     * @param timeFromat
     *            String类型格式
     * @return 格式化后的Date日期
     */
    public static Date parseStrToDate(String time, String timeFromat) {
        if (time == null || time.equals("")) {
            return null;
        }

        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFromat);
            date = dateFormat.parse(time);
        } catch (Exception e) {

        }
        return date;
    }

    /**
     * 格式化String时间
     *
     * @param strTime
     *            String类型时间
     * @param timeFromat
     *            String类型格式
     * @param defaultValue
     *            异常时返回的默认值
     * @return
     */
    public static Date parseStrToDate(String strTime, String timeFromat, Date defaultValue) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(timeFromat);
            return dateFormat.parse(strTime);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 当strTime为2008-9时返回为2008-9-1 00:00格式日期时间，无法转换返回null.
     *
     * @param strTime
     * @return
     */
    public static Date strToDate(String strTime) {
        if (strTime == null || strTime.trim().length() <= 0)
            return null;

        Date date = null;
        List<String> list = new ArrayList<String>(0);

        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS);
        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI);
        list.add(DATE_TIME_FORMAT_YYYYMMDD_HH_MI);
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISS);
        list.add(DATE_FORMAT_YYYY_MM_DD);
        // list.add(DATE_FORMAT_YY_MM_DD);
        list.add(DATE_FORMAT_YYYYMMDD);
        list.add(DATE_FORMAT_YYYY_MM);
        list.add(DATE_FORMAT_YYYYMM);
        list.add(DATE_FORMAT_YYYY);

        for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
            String format = (String) iter.next();
            if (strTime.indexOf("-") > 0 && format.indexOf("-") < 0)
                continue;
            if (strTime.indexOf("-") < 0 && format.indexOf("-") > 0)
                continue;
            if (strTime.length() > format.length())
                continue;
            date = parseStrToDate(strTime, format);
            if (date != null)
                break;
        }

        return date;
    }

    /**
     * 解析两个日期之间的所有月份
     *
     * @param beginDateStr
     *            开始日期，至少精确到yyyy-MM
     * @param endDateStr
     *            结束日期，至少精确到yyyy-MM
     * @return yyyy-MM日期集合
     */
    public static List<String> getMonthListOfDate(String beginDateStr, String endDateStr) {
        // 指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        // 返回的月份列表
        String sRet = "";

        // 定义一些变量
        Date beginDate = null;
        Date endDate = null;

        GregorianCalendar beginGC = null;
        GregorianCalendar endGC = null;
        List<String> list = new ArrayList<String>();

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = new GregorianCalendar();
            beginGC.setTime(beginDate);

            endGC = new GregorianCalendar();
            endGC.setTime(endDate);

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
                sRet = beginGC.get(Calendar.YEAR) + "-" + (beginGC.get(Calendar.MONTH) + 1);
                list.add(sRet);
                // 以月为单位，增加时间
                beginGC.add(Calendar.MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析两个日期段之间的所有日期
     *
     * @param beginDateStr
     *            开始日期 ，至少精确到yyyy-MM-dd
     * @param endDateStr
     *            结束日期 ，至少精确到yyyy-MM-dd
     * @return yyyy-MM-dd日期集合
     */
    public static List<String> getDayListOfDate(String beginDateStr, String endDateStr) {
        // 指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        // 定义一些变量
        Date beginDate = null;
        Date endDate = null;

        Calendar beginGC = null;
        Calendar endGC = null;
        List<String> list = new ArrayList<String>();

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = Calendar.getInstance();
            beginGC.setTime(beginDate);

            endGC = Calendar.getInstance();
            endGC.setTime(endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

                list.add(sdf.format(beginGC.getTime()));
                // 以日为单位，增加时间
                beginGC.add(Calendar.DAY_OF_MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当下年份指定前后数量的年份集合
     *
     * @param before
     *            当下年份前年数
     * @param behind
     *            当下年份后年数
     * @return 集合
     */
    public static List<Integer> getYearListOfYears(int before, int behind) {
        if (before < 0 || behind < 0) {
            return null;
        }
        List<Integer> list = new ArrayList<Integer>();
        Calendar c = null;
        c = Calendar.getInstance();
        c.setTime(new Date());
        int currYear = Calendar.getInstance().get(Calendar.YEAR);

        int startYear = currYear - before;
        int endYear = currYear + behind;
        for (int i = startYear; i < endYear; i++) {
            list.add(Integer.valueOf(i));
        }
        return list;
    }

    /**
     * 获取当前日期是一年中第几周
     *
     * @param date
     * @return
     */
    public static Integer getWeekthOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取某一年各星期的始终时间 实例：getWeekList(2016)，第52周(从2016-12-26至2017-01-01)
     *
     * @param 年份
     * @return
     */
    public static HashMap<Integer, String> getWeekTimeOfYear(int year) {
        HashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekthOfYear(c.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dayOfWeekStart = "";
        String dayOfWeekEnd = "";
        for (int i = 1; i <= count; i++) {
            dayOfWeekStart = sdf.format(getFirstDayOfWeek(year, i));
            dayOfWeekEnd = sdf.format(getLastDayOfWeek(year, i));
            map.put(Integer.valueOf(i), "第" + i + "周(从" + dayOfWeekStart + "至" + dayOfWeekEnd + ")");
        }
        return map;

    }

    /**
     * 获取某一年的总周数
     *
     * @param year
     * @return
     */
    public static Integer getWeekCountOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekthOfYear(c.getTime());
        return count;
    }

    /**
     * 获取指定日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 获取指定日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 获取某年某周的第一天
     *
     * @param year
     *            目标年份
     * @param week
     *            目标周数
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 获取某年某周的最后一天
     *
     * @param year
     *            目标年份
     * @param week
     *            目标周数
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year
     *            目标年份
     * @param month
     *            目标月份
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        int day = c.getActualMinimum(Calendar.DAY_OF_MONTH);

        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year
     *            目标年份
     * @param month
     *            目标月份
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 获取某个日期为星期几
     *
     * @param date
     * @return String "星期*"
     */
    public static String getDayWeekOfDate1(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 获得指定日期的星期几数
     *
     * @param date
     * @return int
     */
    public static Integer getDayWeekOfDate2(Date date) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        int weekDay = aCalendar.get(Calendar.DAY_OF_WEEK);
        return weekDay;
    }

    /**
     * 验证字符串是否为日期
     * 验证格式:YYYYMMDD、YYYY_MM_DD、YYYYMMDDHHMISS、YYYYMMDD_HH_MI、YYYY_MM_DD_HH_MI、
     * YYYYMMDDHHMISSSSS、YYYY_MM_DD_HH_MI_SS
     *
     * @param strTime
     * @return null时返回false;true为日期，false不为日期
     */
    public static boolean validateIsDate(String strTime) {
        if (strTime == null || strTime.trim().length() <= 0)
            return false;

        Date date = null;
        List<String> list = new ArrayList<String>(0);

        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS);
        list.add(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI);
        list.add(DATE_TIME_FORMAT_YYYYMMDD_HH_MI);
        list.add(DATE_TIME_FORMAT_YYYYMMDDHHMISS);
        list.add(DATE_FORMAT_YYYY_MM_DD);
        // list.add(DATE_FORMAT_YY_MM_DD);
        list.add(DATE_FORMAT_YYYYMMDD);
        // list.add(DATE_FORMAT_YYYY_MM);
        // list.add(DATE_FORMAT_YYYYMM);
        // list.add(DATE_FORMAT_YYYY);

        for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
            String format = (String) iter.next();
            if (strTime.indexOf("-") > 0 && format.indexOf("-") < 0)
                continue;
            if (strTime.indexOf("-") < 0 && format.indexOf("-") > 0)
                continue;
            if (strTime.length() > format.length())
                continue;
            date = parseStrToDate(strTime.trim(), format);
            if (date != null)
                break;
        }

        if (date != null) {
            System.out.println("生成的日期:" + DateUtil.parseDateToStr(date, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS, "--null--"));
            return true;
        }
        return false;
    }

    /**
     * 将指定日期的时分秒格式为零
     *
     * @param date
     * @return
     */
    public static Date formatHhMmSsOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得指定时间加减参数后的日期(不计算则输入0)
     *
     * @param date
     *            指定日期
     * @param year
     *            年数，可正可负
     * @param month
     *            月数，可正可负
     * @param day
     *            天数，可正可负
     * @param hour
     *            小时数，可正可负
     * @param minute
     *            分钟数，可正可负
     * @param second
     *            秒数，可正可负
     * @param millisecond
     *            毫秒数，可正可负
     * @return 计算后的日期
     */
    public static Date addDate(Date date, int year, int month, int week,int day, int hour, int minute, int second, int millisecond) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);// 加减年数
        c.add(Calendar.MONTH, month);// 加减月数
        c.add(Calendar.WEDNESDAY,week);
        c.add(Calendar.DATE, day);// 加减天数
        c.add(Calendar.HOUR, hour);// 加减小时数
        c.add(Calendar.MINUTE, minute);// 加减分钟数
        c.add(Calendar.SECOND, second);// 加减秒
        c.add(Calendar.MILLISECOND, millisecond);// 加减毫秒数

        return c.getTime();
    }

    /**
     * 获得两个日期的时间戳之差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDistanceTimestamp(Date startDate, Date endDate) {
        long daysBetween = (endDate.getTime() - startDate.getTime() + 1000000) / (3600 * 24 * 1000);
        return daysBetween;
    }

    /**
     * 判断二个时间是否为同年同月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean compareIsSameMonth(Date date1, Date date2) {
        boolean flag = false;
        int year1 = getYear(date1);
        int year2 = getYear(date2);
        if (year1 == year2) {
            int month1 = getMonth(date1);
            int month2 = getMonth(date2);
            if (month1 == month2)
                flag = true;
        }
        return flag;
    }

    /**
     * 获得两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1
     *            时间参数 1 格式：1990-01-01 12:00:00
     * @param str2
     *            时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTime(Date one, Date two) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {

            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long[] times = { day, hour, min, sec };
        return times;
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1
     *            时间参数 1 格式：1990-01-01 12:00:00
     * @param str2
     *            时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = { day, hour, min, sec };
        return times;
    }

    /**
     * 两个时间相差距离多少小时
     *
     * @param str1
     *            时间参数 1
     * @param str2
     *            时间参数 2
     * @return String 返回值为小时
     */
    public static long getDistanceHour(Date one, Date two) {
        long hour = 0;
        long time1 = one.getTime();
        long time2 = two.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        hour = (diff / (60 * 60 * 1000));
        return hour;
    }

    /**
     * 两个时间相差距离多少分钟
     *
     * @param str1
     *            时间参数 1
     * @param str2
     *            时间参数 2
     * @return String 返回值为分钟
     */
    public static long getDistanceMin(Date one, Date two) {
        long min = 0;
        long time1 = one.getTime();
        long time2 = two.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        min = (diff / (60 * 1000));
        return min;
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param one
     *            时间参数 1：
     * @param two
     *            时间参数 2：
     * @return 相差天数
     */
    public static Long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date
     * @return
     */
    public static Date getDayBeginTime(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date getDayEndTime(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 获取微信指定格式的时间字符串
     *
     * @param minutes 分钟数
     * @return
     */
    public static Date addMinutes(int minutes) {
        Calendar calendar = Calendar.getInstance();
        if (minutes > 0) {
            calendar.add(Calendar.MINUTE, minutes);
        }
        return calendar.getTime();
    }



    private final static String patternDef = "yyyy-MM-dd";

    /** 存放不同的日期模板格式的sdf的Map */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new ConcurrentHashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> sdf = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (sdf == null) {
            // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
            sdf = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(pattern);
                }
            };
            sdfMap.put(pattern, sdf);
        }

        return sdf.get();
    }


    /**
     * 得到指定日期的周一
     * @param time
     * @return
     */
    public static Date paresMonday(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return cal.getTime();
    }
    /**
     * 得到指定日期的周日
     * @param time
     * @return
     */
    public static Date paresSunday(Date time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }


    /**
     * 按照指定的格式，将日期类型对象转换成字符串，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss
     * @param date
     * @param pattern 格式
     * @return
     */
    public static String formatDate(Date date,String pattern){
        SimpleDateFormat formater=getSdf(pattern);
        return formater.format(date);
    }

    /**
     * 将日期转换为yyyy-MM-dd日期字符串
     * @param date
     * @return
     */
    public static String formatDate(Date date){
        return getSdf(patternDef).format(date);
    }

    /**
     * 根据毫秒数得到日期
     * @param timeMillis
     * @return
     */
    public static Date newDate(long timeMillis){
        Calendar logDate = Calendar.getInstance();
        logDate.setTimeInMillis(timeMillis);
        return logDate.getTime();
    }

    /**
     * 按照指定的格式，将字符串转换成日期类型对象，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parseDate(String dateStr,String pattern){
        SimpleDateFormat formater=getSdf(pattern);
        try {
            return formater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串（yyyy-MM-dd）解析成日期
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr){
        return parseDate(dateStr,"yyyy-MM-dd");
    }

    /**
     * 将字符串（yyyy-MM-dd HH:mm:ss）解析成日期
     * @param dateStr
     * @return
     */
    public static Date parseDate2(String dateStr){
        return parseDate(dateStr,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * @return
     */
    public static int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }

/*	*//**
     * 比较两个日期相差的天数
     * @param d1
     * @param d2
     * @param hour 相差的小时
     * @return
     *//*
	public static long getDayDiffer(Date d1,Date d2,int hour){
	    long rel =d2.getTime()-d1.getTime();
	    return Math.abs(rel/(1000*60*60*hour));
	}*/

    /**
     * 比较两个日期相差的秒
     * @param d1
     * @param d2
     * @param hour 相差的秒
     * @return
     */
    public static long getSencondDiffer(Date d1,Date d2){
        long rel =d2.getTime()-d1.getTime();
        return Math.abs(rel/(1000));
    }

    /**
     * 时间比较, 比较到日
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 0表示相同, 正数是date1 > date2, 负数是date1 < date2
     */
    public static int compareToDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = getSdf("yyyyMMdd");
        return sdf.format(date1).compareTo(sdf.format(date2));
    }

    /**
     * 时间比较, 比较到时
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 0表示相同, 正数是date1 > date2, 负数是date1 < date2
     */
    public static int compareToHour(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = getSdf("yyyyMMddHH");
        return sdf.format(date1).compareTo(sdf.format(date2));
    }

    /**
     * 时间比较, 比较到秒
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 0表示相同, 正数是date1 > date2, 负数是date1 < date2
     */
    public static int compareToSecond(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = getSdf("yyyyMMddHHmmss");
        return sdf.format(date1).compareTo(sdf.format(date2));
    }

    /**
     * 取得所给的日期的日开始时间(00:00:00,000)
     *
     * @param day 要转换的日期
     * @return 日开始时间
     */

    public static Date getStartDateOfDay(){
        return getStartDateOfDay(null);
    }

    public static Date getStartDateOfDay(Date day) {
        if (ObjectUtil.isEmpty(day)) {
            day = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }

    public static Date getStartDateOfTomorrow() {
        return getStartDateOfTomorrow(null);
    }

    public static Date getStartDateOfTomorrow(Date day) {
        if (ObjectUtil.isEmpty(day)) {
            day = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_MONTH,1);
        return cal.getTime();
    }

    /**
     * 取得所给的日期的日结束时间(23:59:59,000)
     *
     * @param day 要转换的日期
     * @return 日结束时间
     */

    public static Date getEndDateOfDay(){
        return getEndDateOfDay(null);
    }

    public static Date getEndDateOfDay(Date day) {
        if (ObjectUtil.isEmpty(day)) {
            day = new Date();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        return cal.getTime();
    }

    public static Date getEndDateOfTomorrow(Date day) {
        if (day == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.add(Calendar.DAY_OF_MONTH,1);
        return cal.getTime();
    }

    /**
     * 取得所给的日期的月开始时间(00:00:00,000)
     *
     * @param day 要转换的日期
     * @return 月开始时间
     */
    public static Date getStartDateOfMonth(Date day) {
        if (day == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 取得所给的日期的月结束时间(23:59:59,000)
     *
     * @param day 要转换的日期
     * @return 月结束时间
     */
    public static Date getEndDateOfMonth(Date day) {
        if (day == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 取得所给的日期的年开始时间(00:00:00,000)
     *
     * @param day 要转换的日期
     * @return 年开始时间
     */
    public static Date getStartDateOfYear(Date day) {
        if (day == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        return cal.getTime();
    }

    /**
     * 取得所给的日期的年结束时间(23:59:59,000)
     *
     * @param day 要转换的日期
     * @return 年结束时间
     */
    public static Date getEndDateOfYear(Date day) {
        if (day == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }

    /**
     * 根据年月日创建的日期
     *
     * @param year  年 如2006年为2006
     * @param month 月 如12月为 11
     * @param day   日 如15日为15
     * @return 日期
     */
    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 根据年月日创建的日期
     *
     * @param year   年 如2006年为2006
     * @param month  月 如12月为 11
     * @param day    日 如15日为15
     * @param hour   小时 24时格式 如下午2点为 14
     * @param minute 分钟 如25分为25
     * @param second 秒 如30秒位30
     * @return 日期
     */
    public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 计算开始日期和结束日期中间相差几个月
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 相差几个月
     */
    public static int calculateMonthDistance(Date start, Date end) {
        int year1 = getYear(start);
        int year2 = getYear(end);
        int month1 = getMonth(start);
        int month2 = getMonth(end);
        return 12 * (year1 - year2) + (month1 - month2);
    }

    /**
     * 计算开始日期和结束日期中间相差多少天
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 相差多少天
     */
    public static int calculateDayDistance(Date start, Date end) {
        long startTimeInMillis = start.getTime();
        long endTimeInMillis = end.getTime();
        return (int) ((startTimeInMillis - endTimeInMillis) / (1000 * 60 * 60 * 24));
    }

    /**
     * 得到现在的时间
     *
     * @return Date
     */
    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 得到现在的时间, 以秒的形式
     *
     * @return long
     */
    public static long nowInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 当前时间以指定格式传换成文字.
     *
     * @param pattern 格式
     * @return 文字
     */
    public static String nowInFormat(String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * 获取传入日期的后一个月的日期
     * @param dateStr 日期
     * @param pattern 格式 例yyyy-MM-dd yyyy-MM
     */
    @SuppressWarnings("deprecation")
    public static String nowAfterMouth(String dateStr, String pattern){
        Date date = parseDate(dateStr,pattern);
        date.setMonth(date.getMonth() + 1);
        String datestr2 = formatDate(date, pattern);
        return datestr2;
    }

    /**
     * 取当前日期的前一个月的日期
     */
    public static String nowBeforeMouth(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());   //设置当前日期
        c.add(Calendar.MONTH, -1); //日期加1
        Date date2 = c.getTime(); //结果
        String datestr2=formatDate(date2, "yyyy-MM-dd");
        return datestr2;
    }

    /**
     * 传入一个日期和间隔天数，取到想要的日期
     */
    public static Date changeDays(Date date,int count)
    {
        Date date1=(Date)date;
        //String dateStr = nowInFormat("yyyy-MM-dd");
        //Date now =parseDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date1);   //设置当前日期
        c.add(Calendar.DATE, count); //日期加1
        Date date2 = c.getTime(); //结果
        return date2;
    }
    /**
     * 传入一个日期和间隔天数，取到想要的日期
     */
    public static Date changeDays(String date,int count)
    {
        Date date1=(Date)parseDate(date);
        //String dateStr = nowInFormat("yyyy-MM-dd");
        //Date now =parseDate(dateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date1);   //设置当前日期
        c.add(Calendar.DATE, count); //日期加1
        Date date2 = c.getTime(); //结果
        return date2;
    }

    /**
     * 传入一个日期和间隔小时，取到想要的日期
     */
    public static Date changeHours(Date date,int count)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.HOUR, count); //小时加1
        Date date2 = c.getTime(); //结果
        return date2;
    }

    /**
     * 传入一个日期和间隔分钟数，取到想要的日期时间
     */
    public static Date changeMinute(Date date,int count)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.MINUTE, count); //加上间隔时间的分钟数
        Date date2 = c.getTime(); //结果
        return date2;
    }
    /**
     * 传入一个日期和间隔月数，取到想要的日期
     */
    public static Date changeMonth(Date date,int count)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.MONTH, count); //月加1
        Date date2 = c.getTime(); //结果
        return date2;
    }

    /**
     * 传入一个日期和间隔年数，取到想要的日期
     */
    public static Date changeYear(Date date,int count)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.YEAR, count); //年加1
        Date date2 = c.getTime(); //结果
        return date2;
    }

    /**
     * 传入一个日期和  秒数,取到想要的日期,如果是负数是减，正数是增加
     * @param date
     * @return
     */
    public static Date changeSecond(Date date, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, count);
        return calendar.getTime();
    }
    /**
     * 传入一个日期和  秒数,取到想要的日期,如果是负数是减，正数是增加
     * @param date
     * @return
     */
    public static Date changeSecond(String date, int count) {
        Date date1=(Date)parseDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.SECOND, count);
        return calendar.getTime();
    }

    /**
     * 检查当前时间是否在配置时间之内
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean checkDate(Date startDate, Date endDate) {
        boolean result = false;
        if(startDate != null && endDate != null) {
            Date nowDate = now();
            if(compareToSecond(nowDate, startDate) > 0
                    && compareToSecond(endDate, nowDate) > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 检查当前时间是否在配置时间之内(2个时间可以都为null)
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean checkDate2(Date startDate, Date endDate) {
        boolean result = false;
        if(startDate != null && endDate != null) {
            Date nowDate = now();
            if(compareToSecond(nowDate, startDate) > 0
                    && compareToSecond(endDate, nowDate) > 0) {
                result = true;
            }
        } else if(startDate == null && endDate != null) {
            Date nowDate = now();
            if(compareToSecond(endDate, nowDate) > 0) {
                result = true;
            }
        } else if(startDate != null && endDate == null) {
            Date nowDate = now();
            if(compareToSecond(nowDate, startDate) > 0) {
                result = true;
            }
        } else {
            result = true;
        }
        return result;
    }


    /**
     * Description: 将yyyyMMddHHmmss格式转date</br>
     * Date: 2019/1/10 16:18</br>
     *
     * @param
     * @return
     * @author yisonglin
     * @since JDK 1.8
     */
    public static Date parseFormatStrToDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDDHHMISS);
        formatter.setLenient(false);
        Date newDate = null;
        try {
            newDate = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return newDate;
    }


    /**
     * yyyy-MM-dd
     * @param date
     * @return
     */
    public static String getShortDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}