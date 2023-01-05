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
import com.pda.api.dto.base.BaseKeyValueDto;
import com.pda.api.mapper.lis.SpecimenApplyMapper;
import com.pda.api.mapper.primary.MobileCommonMapper;
import com.pda.api.mapper.slave.OrderExcuteLogMapper;
import com.pda.api.service.CheckService;
import com.pda.api.service.DeptService;
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
import java.util.stream.Collectors;

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
    @Autowired
    private SpecimenApplyMapper specimenApplyMapper;
    @Autowired
    private DeptService deptService;


    /**
     * 标本送检
     * @param patientId
     * @param visitId
     * @return
     */
    @Override
    public List<SpecimenCheckResDto> specimenCheck(String patientId, Integer visitId) {
        // 1、查询用户所有标本送检
        String patId = String.format("%s%s",patientId,visitId);
        log.info("====================标本送检，查询满足条件的标本=========================");
        long start1Time = System.currentTimeMillis();
        List<SpecimenCheckResDto> results = specimenApplyMapper.selectSubjectCheck(patId);
        long end1Time = System.currentTimeMillis();
        log.info("============查询标本所用时间:{}====================",(end1Time - start1Time));
        /*List<SpecimenCheckResDto> results = mobileCommonMapper.selectSubjectCheck(patientId,visitId);*/
        log.info("====================标本送检，查询所有部门=========================");
        long start2Time = System.currentTimeMillis();
        List<BaseKeyValueDto> depts = deptService.findAll();
        long end2Time = System.currentTimeMillis();
        log.info("============查询所有部门所用时间:{}====================",(end2Time - start2Time));
        Map<Object, List<BaseKeyValueDto>> deptMap = depts.stream().collect(Collectors.groupingBy(BaseKeyValueDto::getKey));
        // 拿到所有科室的信息
        log.info("====================标本送检，查询所有标本日志=========================");
        long start3Time = System.currentTimeMillis();
        if(CollectionUtil.isNotEmpty(results)){
            results.forEach(result -> {
                result.setPatientId(patientId);
                result.setVisitId(visitId);
                // 执行科室
                if(CollectionUtil.isNotEmpty(deptMap) && CollectionUtil.isNotEmpty(deptMap.get(result.getPerformedBy()))){
                    BaseKeyValueDto dept = deptMap.get(result.getPerformedBy()).get(0);
                    result.setPerformedDeptName(String.valueOf(dept.getValue()));
                }
                // 状态
                List<OrderExcuteLog> logs = iOrderExcuteLogService.findSpecimenLog(patientId,visitId,result.getTestNo());
                if(CollectionUtil.isNotEmpty(logs)){
                    setSpecimenStatus(result,logs);
                    result.setOrderExcuteLogList(logs);
                }
            });
         }
        long end3Time = System.currentTimeMillis();
        log.info("============查询所有部门所用时间:{}====================",(end3Time - start3Time));
        return results;
    }

    @Override
    public SpecimenCheckCountDto specimenCheckCount(String patientId, Integer visitId) {
        SpecimenCheckCountDto result = new SpecimenCheckCountDto();
        String patId = String.format("%s%s",patientId,visitId);
        List<SpecimenCheckResDto> results = specimenApplyMapper.selectSubjectCheck(patId);
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
        orderExcuteLog.setDeviceNo(specimenCheckOperDto.getDeviceNo());
        orderExcuteLog.setCheckTime(now);
        orderExcuteLog.setCheckStatus("1");
        orderExcuteLog.setExcuteUserCode(currentUser.getUserName());
        orderExcuteLog.setExcuteUserName(currentUser.getName());
        orderExcuteLog.setExcuteStatus("1");
        orderExcuteLog.setExcuteTime(now);
        if("1".equals(specimenCheckOperDto.getStatus())){
            logs.forEach(log -> {
                if("6".equals(log.getType())){
                    throw new BusinessException("该标本已核对!");
                }
            });
            orderExcuteLog.setType("6");
        }else{
            logs.forEach(log -> {
                if("7".equals(log.getType())){
                    throw new BusinessException("该标本已送检!");
                }
                /*if(specimenCheckOperDto.getSpecimen().contains("血")){
                    if("6".equals(log.getType()) && !log.getDeviceNo().equals(specimenCheckOperDto.getDeviceNo())){
                        throw new BusinessException("当前操作使用设备需和之前操作的设备相同!");
                    }
                }*/

            });
            orderExcuteLog.setType("7");
            // 反写lis系统 req_master的req_stat字段
            String patId = String.format("%s%s",specimenCheckOperDto.getPatientId(),specimenCheckOperDto.getVisitId());
            specimenApplyMapper.sendSpecimen(specimenCheckOperDto.getTestNo(),patId);
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
