package com.pda.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        return String.format("%s岁%s月",age,(thisMonth - birthMonth));
    }

    public static Integer getDurationDays(Date startDate,Date endDate) {
        Long result = null;
        try {
            Long starTime=startDate.getTime();
            Long endTime=endDate.getTime();
            Long num=endTime-starTime;//时间戳相差的毫秒数
            result = num/24/60/60/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.intValue();
    }


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "1992-01-15 01:00:00";
        Date date = sdf.parse(str);
        System.out.println(getAgeStr(date));

        String startStr = "2022-07-29 14:16:38";
        System.out.println(getDurationDays(sdf.parse(startStr),new Date()));
    }

}
