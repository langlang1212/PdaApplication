package com.pda.utils;

import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pda.common.dto.PdaRequest;

import java.util.Map;

/**
 * @Classname PdaParamUtil
 * @Description TODO
 * @Date 2022-07-22 13:24
 * @Created by AlanZhang
 */
public class PdaParamUtil {

    public static String getParam(PdaRequest pdaRequest){
        Map objMap = JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(pdaRequest)),Map.class);
        String objXml = XmlUtil.mapToXmlStr(objMap,"root");
        String requestXml = objXml.replace("standalone=\"no\"","").replace("<t>","").replace("</t>","");
        return requestXml;
    }
}
