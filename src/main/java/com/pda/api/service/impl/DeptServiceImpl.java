package com.pda.api.service.impl;

import com.pda.api.dto.base.BaseKeyValueDto;
import com.pda.api.mapper.primary.MobileCommonMapper;
import com.pda.api.service.DeptService;
import com.pda.common.PdaBaseService;
import com.pda.common.config.WsProperties;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Classname DeptServiceImpl
 * @Description TODO
 * @Date 2022-07-22 14:22
 * @Created by AlanZhang
 */
@Slf4j
@Service
public class DeptServiceImpl extends PdaBaseService implements DeptService {

    @Autowired
    private MobileCommonMapper mobileCommonMapper;

    @Override
    public String list(Integer pageNum) {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>MS025</msgType>\n" +
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
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }

    @Override
    public List<BaseKeyValueDto> findAll() {
        return mobileCommonMapper.selectAll();
    }
}
