package com.pda.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.pda.common.config.JaxWsDynamicClientFactory;
import com.pda.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Classname CxfClientUtil
 * @Description TODO
 * @Date 2022-07-22 11:52
 * @Created by AlanZhang
 */
@Slf4j
public class CxfClient {

    /**
     * 执行 webservice
     * @param wsdlUrl
     * @param methodName
     * @param args
     * @return
     */
    public static String excute(String wsdlUrl,String methodName,String... args){
        String result = null;
        try {
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient(wsdlUrl);
            client = dcf.createClient(wsdlUrl);
            HTTPConduit conduit = (HTTPConduit) client.getConduit();
            HTTPClientPolicy policy = new HTTPClientPolicy();
            policy.setAllowChunking(false);
            policy.setConnectionTimeout(10000);
            policy.setReceiveTimeout(20000);
            conduit.setClient(policy);
            // 调用
            Object[] objects = client.invoke(methodName,args);
            if(objects.length > 0){
                result = (String) objects[0];
            }
        }catch (Exception e){
            log.error("调用webservice失败!",e);
            throw new BusinessException("调用webservice失败!");
        }
        return result;
    }
    private static final OkHttpClient client = new OkHttpClient();
    public static String exuteHttp(String url, String method, Map<String,String> parames) {
        String result = null;
        try {
            String jsonBody = JSON.toJSONString(parames);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonBody);
            Request request = new Request.Builder()
                    .url(url)
                    .method(method,body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                result = response.body().string();
                System.out.println(result);
            }
        }catch (Exception e){
            log.error("调用http失败!",e);
            throw new BusinessException("调用http失败!");
        }
        return result;

    }
}
