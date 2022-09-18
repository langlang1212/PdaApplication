package com.pda.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

/**
 * @Classname PdaTimeUtil
 * @Description TODO
 * @Date 2022-07-22 13:36
 * @Created by AlanZhang
 */
public class PdaTimeUtil {

    public static String getCreateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }


    public static String getLongTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static int getAge(Date birth) {
        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        int thisMonth = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birth);
        int birthYear = cal.get(Calendar.YEAR);
        int birthMonth = cal.get(Calendar.MONTH);
        int birthdayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        int age = thisYear - birthYear;

        // 未足月
        if (thisMonth <= birthMonth) {
            // 当月
            if (thisMonth == birthMonth) {
                // 未足日
                if (dayOfMonth < birthdayOfMonth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    public static String getAgeStr(Date birth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String birthStr = sdf.format(birth);
        String now = sdf.format(new Date());
        Temporal temporal1 = LocalDate.parse(birthStr);
        Temporal temporal2 = LocalDate.parse(now);
        // 方法返回为相差月份
        long l = ChronoUnit.MONTHS.between(temporal1, temporal2);
        long years = l/12;
        long monthes = l%12;
        return String.format("%s岁%s月",years,monthes);
    }

    public static Integer getDurationDays(Date startDate,Date endDate) {
        Long result = null;
        try {
            Long starTime=DateUtil.getStartDateOfDay(startDate).getTime();
            Long endTime=DateUtil.getStartDateOfTomorrow(endDate).getTime();
            Long num=endTime-starTime;//时间戳相差的毫秒数
            result = num/24/60/60/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.intValue();
    }


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = "1951-12-01 16:00:37";
        Date date = sdf.parse(str);
        System.out.println(getAgeStr(date));
    }

}
