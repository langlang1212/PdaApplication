package com.pda.api.service.impl;

import com.google.common.collect.Lists;
import com.pda.api.domain.entity.PatientInfo;
import com.pda.api.dto.PatientPatrolDto;
import com.pda.api.dto.PatrolLabelDto;
import com.pda.api.dto.PatrolSyncDto;
import com.pda.api.dto.PatrolSyncItemDto;
import com.pda.api.mapper.slave.PatrolMapper;
import com.pda.api.service.PatrolSyncService;
import com.pda.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname PatrolSyncServiceImpl
 * @Description TODO
 * @Date 2023-03-28 21:31
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class PatrolSyncServiceImpl implements PatrolSyncService {

    @Autowired
    private PatrolMapper patrolMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Async
    public void syncPatrol(PatientPatrolDto patientPatrolDto, PatientInfo patientInfo){
        log.info("进入同步巡查记录日志！");
        PatrolSyncDto result = new PatrolSyncDto();
        result.setZhuyuanId(String.format("%s%s",patientInfo.getPatientId(),patientInfo.getVisitId()));
        result.setXunchaTime(DateUtil.formatDateToStrNos(patientPatrolDto.getOperTime()));
        result.setXunchaHushiId(patientPatrolDto.getOperUserCode());
        result.setXunchaHushiName(patientPatrolDto.getOperUserName());
        result.setHisDisplayZhuyuanhao(patientInfo.getInpNo());
        result.setXingming(patientInfo.getName());
        result.setBingchuangHao(patientInfo.getBedLabel());
        result.setModifyTime(patientPatrolDto.getOperTime());
        result.setDataSource("PDA");
        result.setYiyuanId(0);
        result.setBingquId(patientInfo.getDeptCode());
        result.setBingquName(patientInfo.getDeptName());
        result.setKeshiId(patientInfo.getDeptCode());
        result.setKeshiName(patientInfo.getDeptName());
        result.setSaomiaoSource("1".equals(patientPatrolDto.getType()) ? "腕带" : "床头卡");
        Long nowLong = System.currentTimeMillis();
        Long operLong = patientPatrolDto.getOperTime().getTime();
        if( (nowLong.longValue() - operLong.longValue()) >= 30 * 60 * 1000){
            result.setIsBulu("是");
        }else{
            result.setIsBulu("否");
        }

        // 处理行项目
        List<PatrolLabelDto> patrolLabelDtos = patrolMapper.selectPatrolLabel();
        log.info("巡视列表数量:{}",patrolLabelDtos.size());
        Map<Integer, List<PatrolLabelDto>> patrolMap = patrolLabelDtos.stream().collect(Collectors.groupingBy(PatrolLabelDto::getPatrolId));
        log.info("巡视map的长度:{}",patrolMap.size());
        List<PatrolSyncItemDto> itemDtos = Lists.newArrayList();
        String[] split = patientPatrolDto.getPatrolId().split("/");
        log.info("巡查记录长度:{}",split.length);
        for(int i = 0 ;i < split.length ; i++){
            String str = split[i];
            log.info("巡查id:{}",str);
            log.info("=======拿到key的列表长度:{}=========",patrolMap.get(Integer.valueOf(str)).size());
            PatrolLabelDto patrolLabelDto = patrolMap.get(Integer.valueOf(str)).get(0);
            log.info("======id:{},name:{},code:{}=====",patrolLabelDto.getPatrolId(),patrolLabelDto.getPatrolName(),patrolLabelDto.getPatrolCode());

            PatrolSyncItemDto itemDto = new PatrolSyncItemDto();
            itemDto.setXunchaItemCode(patrolLabelDto.getPatrolCode());
            itemDto.setXunchaItemName(patrolLabelDto.getPatrolName());
            itemDto.setXunchaItemType("巡查事件");
            itemDto.setModifyTime(result.getModifyTime());
            itemDto.setDataSource(result.getDataSource());
            itemDto.setYiyuanId(result.getYiyuanId());

            itemDtos.add(itemDto);
        }

        result.setItems(itemDtos);

        // 发送请求
        PatrolSyncDto syncResult = restTemplate.postForObject("http://192.168.61.55:9093/syncPatrol", result, PatrolSyncDto.class);
        log.info("执行结果:{}",syncResult.toString());
    }
}
