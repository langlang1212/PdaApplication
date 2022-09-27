package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.SpecimenCheck;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.domain.service.ISpecimenCheckService;
import com.pda.api.dto.SpecimenCheckCountDto;
import com.pda.api.dto.SpecimenCheckOperDto;
import com.pda.api.dto.SpecimenCheckResDto;
import com.pda.api.dto.UserResDto;
import com.pda.api.mapper.primary.MobileCommonMapper;
import com.pda.api.mapper.slave.OrderExcuteLogMapper;
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
    private MobileCommonMapper mobileCommonMapper;

    @Autowired
    private IOrderExcuteLogService iOrderExcuteLogService;

    @Autowired
    private OrderExcuteLogMapper orderExcuteLogMapper;


    /**
     * 标本送检
     * @param patientId
     * @param visitId
     * @return
     */
    @Override
    public List<SpecimenCheckResDto> specimenCheck(String patientId, Integer visitId) {
        // 1、查询用户所有标本送检
        List<SpecimenCheckResDto> results = mobileCommonMapper.selectSubjectCheck(patientId,visitId);
        if(CollectionUtil.isEmpty(results)){
            results.forEach(result -> {
                List<OrderExcuteLog> logs = iOrderExcuteLogService.findSpecimenLog(patientId,visitId,result.getTestNo());
                if(CollectionUtil.isNotEmpty(logs)){
                    setSpecimenStatus(result,logs);
                    result.setOrderExcuteLogList(logs);
                }
            });
         }
        return results;
    }

    @Override
    public SpecimenCheckCountDto specimenCheckCount(String patientId, Integer visitId) {
        SpecimenCheckCountDto result = new SpecimenCheckCountDto();
        List<SpecimenCheckResDto> results = mobileCommonMapper.selectSubjectCheck(patientId,visitId);
        if(CollectionUtil.isNotEmpty(results)){
            result.setTotal(results.size());
            results.forEach(resDto -> {
                List<OrderExcuteLog> logs = iOrderExcuteLogService.findSpecimenLog(patientId,visitId,resDto.getTestNo());
                if(CollectionUtil.isNotEmpty(logs)){
                    OrderExcuteLog orderExcuteLog = logs.get(logs.size() - 1);
                    if("6".equals(orderExcuteLog.getType())){
                        result.setCollaredCount(result.getCollaredCount() + 1);
                    }else if("7".equals(orderExcuteLog.getType())){
                        result.setSentCount(result.getSentCount() + 1);
                    }
                }
            });
        }
        result.setRemainingCount(result.getTotal() - result.getCollaredCount() - result.getSentCount());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doSpecimenCheck(SpecimenCheckOperDto specimenCheckOperDto) {
        //
        UserResDto currentUser = SecurityUtil.getCurrentUser();

        List<OrderExcuteLog> logs = iOrderExcuteLogService.findSpecimenLog(specimenCheckOperDto.getPatientId(),specimenCheckOperDto.getVisitId(),specimenCheckOperDto.getTestNo());

        OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
        LocalDateTime now = LocalDateTime.now();
        orderExcuteLog.setPatientId(specimenCheckOperDto.getPatientId());
        orderExcuteLog.setVisitId(specimenCheckOperDto.getVisitId());
        orderExcuteLog.setTestNo(specimenCheckOperDto.getTestNo());
        orderExcuteLog.setCheckTime(now);
        orderExcuteLog.setCheckStatus("1");
        orderExcuteLog.setExcuteUserCode(currentUser.getUserName());
        orderExcuteLog.setExcuteUserName(currentUser.getName());
        orderExcuteLog.setExcuteStatus("1");
        orderExcuteLog.setExcuteTime(now);
        if("1".equals(specimenCheckOperDto.getStatus())){
            logs.forEach(log -> {
                if("6".equals(log.getType())){
                    throw new BusinessException("请勿重复核对!");
                }
            });
            orderExcuteLog.setType("6");
        }else{
            logs.forEach(log -> {
                if("7".equals(log.getType())){
                    throw new BusinessException("已送检!");
                }
            });
            orderExcuteLog.setType("7");
        }
        // 插入
        orderExcuteLogMapper.insert(orderExcuteLog);
    }

    private void setSpecimenStatus(SpecimenCheckResDto result, List<OrderExcuteLog> logs) {
        // null：未核对 1:已核对 2:已送检
        OrderExcuteLog orderExcuteLog = logs.get(logs.size() - 1);
        if("6".equals(orderExcuteLog.getType())){
            result.setStatus("1");
        }else if("7".equals(orderExcuteLog.getType())){
            result.setStatus("2");
        }
    }
}
