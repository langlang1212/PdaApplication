package com.pda.api.service.impl;

import com.pda.api.dto.PatientAllergyReqDto;
import com.pda.api.dto.PatientReqDto;
import com.pda.api.service.PatientService;
import com.pda.common.PdaBaseService;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Classname PatientServiceImpl
 * @Description TODO
 * @Date 2022-07-22 14:47
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class PatientServiceImpl extends PdaBaseService implements PatientService {

    @Override
    public String fintPatientInfo(PatientReqDto patientReqDto) {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "    <AuthHeader>\n" +
                "        <msgType>TJ602</msgType>\n" +
                "        <msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "        <createTime>"+ PdaTimeUtil.getCreateTime(new Date())+"</createTime>\n" +
                "        <sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "        <targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "        <sysPassword/>\n" +
                "    </AuthHeader>\n" +
                "    <ControlActProcess>\n" +
                "        <PageNum>"+patientReqDto.getPageNum()+"</PageNum>\n" +
                "        <PatientNo>"+patientReqDto.getPatientNo()+"</PatientNo>\n" +
                "        <WardCode>"+patientReqDto.getWardCode()+"</WardCode>\n" +
                "        <Status>"+patientReqDto.getStatus()+"</Status>\n" +
                "        <UpTime>"+PdaTimeUtil.getLongTime(new Date())+"</UpTime>\n" +
                "    </ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }

    @Override
    public String fintAllergyInfo(Integer pageNum,String patientNo) {
        Date now = new Date();
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "    <AuthHeader>\n" +
                "        <msgType>TJ604</msgType>\n" +
                "        <msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "        <createTime>"+PdaTimeUtil.getCreateTime(now)+"</createTime>\n" +
                "        <sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "        <targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "        <sysPassword/>\n" +
                "    </AuthHeader>\n" +
                "    <ControlActProcess>\n" +
                "        <PageNum>"+pageNum+"</PageNum>\n" +
                "        <PatientNo>"+patientNo+"</PatientNo>\n" +
                "        <UpTime>"+PdaTimeUtil.getLongTime(now)+"</UpTime>\n" +
                "    </ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }

    @Override
    public String fintTzInfo(Integer pageNum, String patientNo, Integer upTime) {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>TJ605</msgType>\n" +
                "\t\t<msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "\t\t<createTime>"+PdaTimeUtil.getCreateTime(new Date())+"</createTime>\n" +
                "\t\t<sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "\t\t<targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "\t\t<sysPassword/>\n" +
                "\t</AuthHeader>\n" +
                "\t<ControlActProcess>\n" +
                "\t\t<PageNum>"+pageNum+"</PageNum>\n" +
                "\t\t<UpTime>"+upTime+"</UpTime>\n" +
                "<PatientNo>"+patientNo+"</PatientNo>\n" +
                "\t</ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }
}
