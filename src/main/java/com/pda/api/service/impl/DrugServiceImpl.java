package com.pda.api.service.impl;

import com.pda.api.service.DrugService;
import com.pda.common.PdaBaseService;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Classname DrugServiceImpl
 * @Description TODO
 * @Date 2022-07-22 18:04
 * @Created by AlanZhang
 */
@Slf4j
@Service
public class DrugServiceImpl extends PdaBaseService implements DrugService {
    @Override
    public String fintDrugmerchineInfo(Integer pageNum) {
        Date now = new Date();
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>TJ610</msgType>\n" +
                "\t\t<msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "\t\t<createTime>"+ PdaTimeUtil.getCreateTime(now) +"</createTime>\n" +
                "\t\t<sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "\t\t<targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "\t\t<sysPassword/>\n" +
                "\t</AuthHeader>\n" +
                "\t<ControlActProcess>\n" +
                "\t\t<PageNum>"+pageNum+"</PageNum>\n" +
                "\t\t<UpTime>"+PdaTimeUtil.getLongTime(now)+"</UpTime>\n" +
                "\t</ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }

    @Override
    public String findDistributionInfo(Integer pageNum) {
        Date now = new Date();
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>TJ611</msgType>\n" +
                "\t\t<msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "\t\t<createTime>"+ PdaTimeUtil.getCreateTime(now) +"</createTime>\n" +
                "\t\t<sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "\t\t<targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "\t\t<sysPassword/>\n" +
                "\t</AuthHeader>\n" +
                "\t<ControlActProcess>\n" +
                "\t\t<PageNum>"+pageNum+"</PageNum>\n" +
                "\t\t<UpTime>"+PdaTimeUtil.getLongTime(now)+"</UpTime>\n" +
                "\t</ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }
}
