package com.pda.api.service.impl;

import com.pda.api.service.UserService;
import com.pda.common.PdaBaseService;
import com.pda.common.config.WsProperties;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Date 2022-07-22 12:46
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class UserServiceImpl extends PdaBaseService implements UserService {

    @Override
    public String list(Integer pageNum) {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>MS032</msgType>\n" +
                "\t\t<msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "\t\t<createTime>"+ PdaTimeUtil.getCreateTime(new Date()) +"</createTime>\n" +
                "\t\t<sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "\t\t<targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "\t\t<sysPassword/>\n" +
                "\t</AuthHeader>\n" +
                "\t<ControlActProcess>\n" +
                "\t\t<PageNum>"+pageNum+"</PageNum>\n" +
                "\t</ControlActProcess>\n" +
                "</root>";
        /*AuthHeader authHeader = new AuthHeader();
        authHeader.setMsgType("MS032");
        authHeader.setCreateTime(PdaTimeUtil.getCreateTime(new Date()));

        ControlActProcess<String> controlActProcess = new ControlActProcess<>();
        controlActProcess.setT("<PageNum>1</PageNum>");

        PdaRequest pdaRequest = new PdaRequest();
        pdaRequest.setAuthHeader(authHeader);
        pdaRequest.setControlActProcess(controlActProcess);

        String param = PdaParamUtil.getParam(pdaRequest);*/

        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }
}
