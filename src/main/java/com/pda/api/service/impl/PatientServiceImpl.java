package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.base.Joiner;
import com.pda.api.domain.entity.UserInfo;
import com.pda.api.domain.service.IUserInfoService;
import com.pda.api.dto.*;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.mapper.primary.PatientInfoMapper;
import com.pda.api.mapper.slave.PatrolMapper;
import com.pda.api.service.PatientService;
import com.pda.api.service.PdaService;
import com.pda.common.Constant;
import com.pda.common.PdaBaseService;
import com.pda.exception.BusinessException;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import com.pda.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname PatientServiceImpl
 * @Description TODO
 * @Date 2022-07-22 14:47
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class PatientServiceImpl extends PdaBaseService implements PatientService {

    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private IUserInfoService iUserInfoService;
    @Autowired
    private PatrolMapper patrolMapper;

    @Override
    public String fintPatientInhInfo(PatientReqDto patientReqDto) {
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
                "        <start_time>"+patientReqDto.getStartTime()+"</start_time>\n" +
                "        <end_time>"+patientReqDto.getEndTime()+"</end_time>\n" +
                "        <Status>"+patientReqDto.getStatus()+"</Status>\n" +
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

    @Override
    public String findPatientInfo(PatientReqDto patientReqDto) {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "    <AuthHeader>\n" +
                "        <msgType>TJ601</msgType>\n" +
                "        <msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "        <createTime>"+ PdaTimeUtil.getCreateTime(new Date())+"</createTime>\n" +
                "        <sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "        <targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "        <sysPassword/>\n" +
                "    </AuthHeader>\n" +
                "    <ControlActProcess>\n" +
                "        <PageNum>"+patientReqDto.getPageNum()+"</PageNum>\n" +
                "        <start_time>"+patientReqDto.getStartTime()+"</start_time>\n" +
                "        <end_time>"+patientReqDto.getEndTime()+"</end_time>\n" +
                "    </ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }

    @Override
    public List<PatientInfoDto> findMyPatient(String keyword,String wardCode) {
        List<PatientInfoDto> result = new ArrayList<>();
        // 1、拿到当前用户
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        List<UserInfo> list = iUserInfoService.list();
        Map<String, String> userMap = list.stream().collect(Collectors.toMap(UserInfo::getUserName, UserInfo::getName));
        if(Constant.DOCTOR.equals(currentUser.getJob())){ //医生
            result = ordersMMapper.findMyPatient(keyword,wardCode,null,currentUser.getUserName());
        }else if(Constant.NURSE.equals(currentUser.getJob())){ // 护士
            result = ordersMMapper.findMyPatient(keyword,null,wardCode,currentUser.getUserName());
        }
        // 2、拿到饮食提醒
        List<FoodNoticeDto> noticeDtos = ordersMMapper.selectNotice();
        Map<String, List<FoodNoticeDto>> noticeMap = noticeDtos.stream().collect(Collectors.groupingBy(FoodNoticeDto::getPatientId));
        if(CollectionUtil.isNotEmpty(result)){
            result.forEach(patientInfo -> {
                List<FoodNoticeDto> notices = noticeMap.get(patientInfo.getPatientId());
                if(CollectionUtil.isNotEmpty(notices)){
                    log.info(patientInfo.getPatientId() + "============================长度:"+notices.size()+"==================================");
                    String notice = notices.stream().map(FoodNoticeDto::getOrderText).collect(Collectors.joining(";"));
                    log.info("============================"+notice+"==================================");
                    patientInfo.setNotice(notice);
                }
                patientInfo.setAge(PdaTimeUtil.getAgeStr(patientInfo.getBirthDay()));
                patientInfo.setInpDays(PdaTimeUtil.getDurationDays(patientInfo.getAdmissionDate(),new Date()));
                patientInfo.setDoctorName(userMap.get(patientInfo.getDoctorCode()));
                patientInfo.setNurseCode(currentUser.getUserName());
                patientInfo.setNurseName(currentUser.getName());
                patientInfo.setBedDesc(patientInfo.getBedNo()+"床");
            });
        }
        return result;
    }

    @Override
    public PatrolDto findPatrol(String patientId, Integer visitId) {
        // 返回结果
        PatrolDto result = new PatrolDto();
        result.setLabels(patrolMapper.selectPatrolLabel());

        List<PatientPatrolDto> patientPatrolDtos = patrolMapper.selectPatientPatrol(patientId,visitId);
        if(CollectionUtil.isNotEmpty(patientPatrolDtos)){
            result.setPatientPatrolDtos(patientPatrolDtos);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void oper(PatrolOperDto patrolOperDto) {
        // 获取当前用户
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        List<PatrolLabelDto> patrolLabelList = patrolOperDto.getPatrolLabelDtos();
        if(CollectionUtil.isEmpty(patrolLabelList)){
            throw new BusinessException("请填入巡视选项!");
        }
        PatientPatrolDto patientPatrolDto = new PatientPatrolDto();
        patientPatrolDto.setPatientId(patrolOperDto.getPatientId());
        patientPatrolDto.setVisitId(patrolOperDto.getVisitId());
        patientPatrolDto.setOperUserCode(currentUser.getUserName());
        patientPatrolDto.setOperUserName(currentUser.getName());
        patientPatrolDto.setType(patrolOperDto.getType());
        //
        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        patrolLabelList.forEach(vo -> {
            ids.add(vo.getPatrolId());
            names.add(vo.getPatrolName());
        });

        patientPatrolDto.setPatrolId(Joiner.on("/").join(ids));
        patientPatrolDto.setPatrolName(Joiner.on("/").join(names));

        patrolMapper.inserPatientPatrol(patientPatrolDto);
    }
}
