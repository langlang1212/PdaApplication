package com.pda.utils;

import java.text.SimpleDateFormat;
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
}
