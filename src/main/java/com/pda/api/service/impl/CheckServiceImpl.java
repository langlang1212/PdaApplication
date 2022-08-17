package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pda.api.domain.entity.SpecimenCheck;
import com.pda.api.domain.service.ISpecimenCheckService;
import com.pda.api.dto.SpecimenCheckCountDto;
import com.pda.api.dto.SpecimenCheckOperDto;
import com.pda.api.dto.SpecimenCheckResDto;
import com.pda.api.dto.UserResDto;
import com.pda.api.service.CheckService;
import com.pda.common.Constant;
import com.pda.common.PdaBaseService;
import com.pda.exception.BusinessException;
import com.pda.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname CheckServiceImpl
 * @Description TODO
 * @Date 2022-07-22 18:12
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class CheckServiceImpl extends PdaBaseService implements CheckService {

    @Autowired
    private ISpecimenCheckService iSpecimenCheckService;

    @Override
    public String findCheckoutInfo(Integer pageNum) {
        Date now = new Date();
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>TJ612</msgType>\n" +
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
    public String findCheckInfo(Integer pageNum) {
        Date now = new Date();
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>TJ613</msgType>\n" +
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
    public String findCheckApplyInfo(Integer pageNum) {
        Date now = new Date();
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>TJ618</msgType>\n" +
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

    /**
     * 标本送检
     * @param patientId
     * @param visitId
     * @return
     */
    @Override
    public List<SpecimenCheckResDto> specimenCheck(String patientId, Integer visitId) {
        Date now = new Date();
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "    <AuthHeader>\n" +
                "        <msgType>TJ618</msgType>\n" +
                "        <msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "        <createTime>"+ PdaTimeUtil.getCreateTime(now) +"</createTime>\n" +
                "        <sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "        <targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "        <sysPassword/>\n" +
                "    </AuthHeader>\n" +
                "    <ControlActProcess>\n" +
                "        <PageNum>1</PageNum>\n" +
                "        <inp_id>"+patientId+"_"+visitId+"</inp_id>\n" +
                "    </ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        if (StringUtils.isNotBlank(result)){
            Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(result);
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(stringObjectMap));
            String responseCode = jsonObject.getJSONObject("ControlActProcess").getJSONObject("Response").getString("TypeCode");
            String responseText = jsonObject.getJSONObject("ControlActProcess").getJSONObject("Response").getString("Text");
            if(Constant.YDYH_RESPONSE_CODE.equals(responseCode) && Constant.YDYH_RESPONSE_TEXT.equals(responseText)){
                JSONArray jsonArray = jsonObject.getJSONObject("ControlActProcess").getJSONObject("ListInfo").getJSONArray("List");
                if(CollectionUtil.isNotEmpty(jsonArray)){
                    List<SpecimenCheckResDto> resultList = new ArrayList<>();
                    for(Object obj : jsonArray){
                        JSONObject jsonObj = (JSONObject) obj;
                        if("2".equals(jsonObj.getString("result_status"))){
                            SpecimenCheckResDto specimenCheckResDto = initSpecimenCheckInfo(jsonObj);
                            resultList.add(specimenCheckResDto);
                        }
                    }
                    return resultList;
                }
            }
        }
        return null;
    }

    private SpecimenCheckResDto initSpecimenCheckInfo(JSONObject jsonObj) {
        SpecimenCheckResDto specimenCheckResDto = new SpecimenCheckResDto();
        specimenCheckResDto.setTestNo(jsonObj.getString("test_no"));
        specimenCheckResDto.setPatientId(jsonObj.getString("patient_id"));
        specimenCheckResDto.setVisitId(jsonObj.getInteger("visit_id"));
        specimenCheckResDto.setSubject(jsonObj.getString("subject"));
        specimenCheckResDto.setSpecimen(jsonObj.getString("specimen"));
        specimenCheckResDto.setRequestedDateTime(jsonObj.getDate("requested_date_time"));
        specimenCheckResDto.setPerformedBy(jsonObj.getString("performed_by"));
        specimenCheckResDto.setPerformedDeptName(jsonObj.getString("performed_dept_name"));
        // 核对信息
        List<SpecimenCheck> checkInfos = getCheckInfo(specimenCheckResDto);
        if(CollectionUtil.isNotEmpty(checkInfos)){
            for(SpecimenCheck specimenCheck : checkInfos){
                if("1".equals(specimenCheck.getStatus())){
                    specimenCheckResDto.setCollartor(specimenCheck.getOperUser());
                    specimenCheckResDto.setCollarDate(LocalDateUtils.localDateTime2Date(specimenCheck.getOperTime()));
                }else if("2".equals(specimenCheck.getStatus())){
                    specimenCheckResDto.setSendUser(specimenCheck.getOperUser());
                    specimenCheckResDto.setSendTime(LocalDateUtils.localDateTime2Date(specimenCheck.getOperTime()));
                }
                specimenCheckResDto.setStatus(specimenCheck.getStatus());
            }
        }
        return specimenCheckResDto;
    }

    @Override
    public SpecimenCheckCountDto specimenCheckCount(String patientId, Integer visitId) {
        SpecimenCheckCountDto result = new SpecimenCheckCountDto();
        // 查询已有的
        List<SpecimenCheckResDto> specimenCheckResDtos = this.specimenCheck(patientId, visitId);
        if(CollectionUtil.isNotEmpty(specimenCheckResDtos)){
            result.setTotal(specimenCheckResDtos.size());
            for(SpecimenCheckResDto resDto : specimenCheckResDtos){
                if("1".equals(resDto.getStatus())){
                    result.setCollaredCount(result.getCollaredCount() + 1);
                }else if("2".equals(resDto.getStatus())){
                    result.setSentCount(result.getSentCount() + 1);
                }
            }
        }
        result.setRemainingCount(result.getTotal() - result.getCollaredCount() - result.getSentCount());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doSpecimenCheck(SpecimenCheckOperDto specimenCheckOperDto) {
        LambdaQueryWrapper<SpecimenCheck> checkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        checkLambdaQueryWrapper.eq(SpecimenCheck::getPatientId,specimenCheckOperDto.getPatientId())
                .eq(SpecimenCheck::getVisitId,specimenCheckOperDto.getVisitId())
                .eq(SpecimenCheck::getTestNo,specimenCheckOperDto.getTestNo())
                .eq(SpecimenCheck::getStatus,specimenCheckOperDto.getStatus());
        SpecimenCheck checkInfo = iSpecimenCheckService.getOne(checkLambdaQueryWrapper);
        if(ObjectUtil.isNotNull(checkInfo)){
            throw new BusinessException("当前标本送检已经执行过当前操作!");
        }else{
            UserResDto currentUser = SecurityUtil.getCurrentUser();
            checkInfo = new SpecimenCheck();
            BeanUtils.copyProperties(specimenCheckOperDto,checkInfo);
            checkInfo.setOperUserCode(currentUser.getUserName());
            checkInfo.setOperUser(currentUser.getName());
            checkInfo.setOperTime(LocalDateTime.now());

            iSpecimenCheckService.save(checkInfo);
        }
    }

    private List<SpecimenCheck> getCheckInfo(SpecimenCheckResDto specimenCheckResDto) {
        LambdaQueryWrapper<SpecimenCheck> checkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        checkLambdaQueryWrapper.eq(SpecimenCheck::getPatientId,specimenCheckResDto.getPatientId())
                .eq(SpecimenCheck::getVisitId,specimenCheckResDto.getVisitId())
                .eq(SpecimenCheck::getTestNo,specimenCheckResDto.getTestNo())
                .orderByAsc(SpecimenCheck::getStatus);
        return iSpecimenCheckService.list(checkLambdaQueryWrapper);
    }
}
