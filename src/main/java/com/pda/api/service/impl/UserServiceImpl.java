package com.pda.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.UserResDto;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.service.UserService;
import com.pda.common.PdaBaseService;
import com.pda.common.config.WsProperties;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import com.pda.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }

    @Override
    public String test() {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "    <AuthHeader>\n" +
                "        <msgType>TJ618</msgType>\n" +
                "        <msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "        <createTime>20181229154012</createTime>\n" +
                "        <sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "        <targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "        <sysPassword/>\n" +
                "    </AuthHeader>\n" +
                "    <ControlActProcess>\n" +
                "        <PageNum>1</PageNum>\n" +
                "        <inp_id>TJ00003791_1</inp_id>\n" +
                "    </ControlActProcess>\n" +
                "</root>";

        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }
}
