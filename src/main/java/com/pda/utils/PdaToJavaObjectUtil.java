package com.pda.utils;

import cn.hutool.core.util.XmlUtil;

import java.util.List;
import java.util.Map;

/**
 * @Classname PdaToJavaObjectUtil
 * @Description TODO
 * @Date 2022-07-31 14:04
 * @Created by AlanZhang
 */
public class PdaToJavaObjectUtil {

    public static List convertList(String str){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(str);
        Map<String,Object> controlActMap = (Map<String, Object>) stringObjectMap.get("ControlActProcess");
        List list = (List) controlActMap.get("List");
        return list;
    }
}
