package com.pda.utils;

/**
 * @Classname LocalDateUtils
 * @Description TODO
 * @Date 2022-08-05 23:48
 * @Created by AlanZhang
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateUtils {

    /**
     * LocalDate转Date
     *
     * @param localDate
     * @return
     */
    public static Date localDate2Date(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date
     */
    public static LocalDate date2LocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * localDateTime转date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);

        return Date.from(zdt.toInstant());
    }

    /**
     * date转localDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime date2LocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 字符串转LocalDate yyyy-MM-dd
     * @param str
     * @return
     */
    public static LocalDate str2LocalDate(String str){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateParam = LocalDate.parse(str, df);
        return dateParam;
    }

    /**
     * @Description: LocalDateTime转String
     * @Date: 2021/3/16
     *
     * @param
     * @return
     * @author mj
     */
    public static String localDateTime2Str(LocalDateTime localDateTime){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String localTime = df.format(localDateTime);
        return localTime;
    }

    public static void main(String[] args) {
        System.out.println(LocalDateUtils.date2LocalDateTime(DateUtil.getTimeOfYestoday(new Date())));
    }

}
