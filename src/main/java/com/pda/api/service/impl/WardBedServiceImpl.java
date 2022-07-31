package com.pda.api.service.impl;

import com.pda.api.service.WardBedService;
import com.pda.common.PdaBaseService;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Classname WardBedServiceImpl
 * @Description TODO
 * @Date 2022-07-22 14:33
 * @Created by AlanZhang
 */
@Slf4j
@Service
public class WardBedServiceImpl extends PdaBaseService implements WardBedService {

    @Override
    public String findWardBed(Integer pageNum) {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "    <AuthHeader>\n" +
                "        <msgType>TJ620</msgType>\n" +
                "        <msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "        <createTime>"+PdaTimeUtil.getCreateTime(new Date())+"</createTime>\n" +
                "        <sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "        <targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "        <sysPassword/>\n" +
                "    </AuthHeader>\n" +
                "    <ControlActProcess>\n" +
                "        <PageNum>"+pageNum+"</PageNum>\n" +
                "    </ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }
}
