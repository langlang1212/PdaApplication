package com.pda.api.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.pda.api.domain.entity.BloodBaseInfo;
import com.pda.api.domain.entity.BloodExcute;
import com.pda.api.domain.entity.BloodInfo;
import com.pda.api.domain.entity.BloodOperLog;
import com.pda.api.domain.service.BloodService;
import com.pda.api.dto.UserResDto;
import com.pda.api.dto.query.BloodExcuteReq;
import com.pda.api.mapper.primary.MobileCommonMapper;
import com.pda.api.mapper.slave.BloodExcuteMapper;
import com.pda.api.mapper.slave.BloodMapper;
import com.pda.api.mapper.slave.BloodOperLogMapper;
import com.pda.api.service.PdaService;
import com.pda.exception.BusinessException;
import com.pda.utils.SecurityUtil;
import com.pda.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Classname BloodServiceImpl
 * @Description TODO
 * @Date 2022-12-12 21:08
 * @Created by AlanZhang
 */
@Service
public class BloodServiceImpl implements BloodService {

    @Autowired
    private MobileCommonMapper mobileCommonMapper;
    @Autowired
    private BloodMapper bloodMapper;
    @Autowired
    private BloodExcuteMapper bloodExcuteMapper;
    @Autowired
    private BloodOperLogMapper bloodOperLogMapper;
    @Autowired
    private PdaService pdaService;

    @Override
    public List<BloodInfo> list(String patientId, Integer visitId) {
        // 查出所有输血送血
        List<BloodInfo> bloodInfos = mobileCommonMapper.selectBlood(patientId, visitId);
        if(CollectionUtil.isNotEmpty(bloodInfos)){
            List<String> ids = bloodInfos.stream().map(BloodInfo::getBloodId).collect(Collectors.toList());
            // 查询状态
            List<BloodExcute> bloodStatus = bloodExcuteMapper.selectBloodStatus(patientId, visitId);
            Map<String, List<BloodExcute>> bloodStatusMap = bloodStatus.stream().collect(Collectors.groupingBy(b -> b.getPatientId() + "_" + b.getVisitId() + "_" + b.getBloodId()));
            // 查询日志
            List<BloodOperLog> bloodOperLogs = bloodMapper.selectLogs(patientId, visitId, ids);
            Map<String,List<BloodOperLog>> operLogMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(bloodOperLogs)){
                operLogMap = bloodOperLogs.stream().collect(Collectors.groupingBy(BloodOperLog::getBloodId));
            }
            // 袋数
            int size = bloodInfos.size();
            for(BloodInfo b : bloodInfos){
                b.setBloodQty(size);
                // 设置状态
                String key = b.getPatientId() + "_" + b.getVisitId() + "_" + b.getBloodId();
                if(CollectionUtil.isNotEmpty(bloodStatusMap) && bloodStatusMap.containsKey(key)){
                    BloodExcute status = bloodStatusMap.get(key).get(0);
                    b.setStatus(status.getStatus());
                }
                // 日志
                if(operLogMap.containsKey(b.getBloodId())){
                    List<BloodOperLog> logs = operLogMap.get(b.getBloodId());
                    logs = logs.stream().filter(l -> 6 != l.getStatus()).sorted(Comparator.comparing(BloodOperLog::getCreateTime))
                            .collect(Collectors.toList());
                    b.setLogs(logs);
                }
            }
        }
        return bloodInfos;
    }

    @Override
    public BloodBaseInfo bloodBaseInfo(String patientId, Integer visitId) {
        // 查出所有输血送血
        List<BloodInfo> bloodInfos = list(patientId, visitId);
        if(CollectionUtil.isNotEmpty(bloodInfos)){
            BloodInfo firstBloodInfo = bloodInfos.get(0);
            BloodBaseInfo bloodBaseInfo = new BloodBaseInfo();
            BeanUtils.copyProperties(firstBloodInfo,bloodBaseInfo);
            if(CollectionUtil.isNotEmpty(firstBloodInfo.getLogs())){
                List<BloodOperLog> commonLogs = Lists.newArrayList();
                for(BloodOperLog log : firstBloodInfo.getLogs()){
                    if(log.getStatus().intValue() == 0 || log.getStatus().intValue() == 1){
                        commonLogs.add(log);
                    }
                }
            }
            return bloodBaseInfo;
        }else {
            throw new BusinessException("该患者没有血袋！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void excute(BloodExcuteReq excuteReq) {
        excuteReq.setBloodId(excuteReq.getBloodId().substring(0,excuteReq.getBloodId().length() - 2));
        // 1、获取当前用户
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        // 2、执行逻辑
        List<BloodOperLog> logs = bloodMapper.selectLogs(excuteReq.getPatientId(),excuteReq.getVisitId(), Arrays.asList(excuteReq.getBloodId()));
        if(CollectionUtil.isNotEmpty(logs)){
            checkNextStep(excuteReq.getPreStatus(),excuteReq.getStatus());
        }
        // 当前时间
        Date now = new Date();
        // 如果是取血、接血阶段，则所有血袋插入取血接血日志
        if(0 == excuteReq.getStatus().intValue() || 1 == excuteReq.getStatus().intValue()){
            List<BloodInfo> bloodInfos = mobileCommonMapper.selectBlood(excuteReq.getPatientId(), excuteReq.getVisitId());
            if(CollectionUtil.isEmpty(bloodInfos)){
                throw new BusinessException("当前患者没有血袋!");
            }
            List<BloodOperLog> operLogs = Lists.newArrayList();
            for(BloodInfo info : bloodInfos){
                BloodOperLog bloodOperLog  = new BloodOperLog();
                bloodOperLog.setPatientId(excuteReq.getPatientId());
                bloodOperLog.setVisitId(excuteReq.getVisitId());
                bloodOperLog.setBloodId(info.getBloodId());
                bloodOperLog.setStatus(excuteReq.getStatus());
                bloodOperLog.setOperUserCode(currentUser.getUserName());
                bloodOperLog.setOperUserName(currentUser.getName());
                bloodOperLog.setOperTime(now);
                bloodOperLog.setCreateUserCode(currentUser.getUserName());
                bloodOperLog.setCreateUserName(currentUser.getName());
                bloodOperLog.setCreateTime(now);
                operLogs.add(bloodOperLog);
            }
            bloodOperLogMapper.insertBatch(operLogs);
        }else {
            UserResDto operUser = null;
            if(StringUtil.isNotBlank(excuteReq.getCheckUserCode())){
                operUser = pdaService.getUserByCode(excuteReq.getCheckUserCode());
            }
            // 操作步骤，如果不是取血和接血
            BloodOperLog bloodOperLog  = new BloodOperLog();
            bloodOperLog.setPatientId(excuteReq.getPatientId());
            bloodOperLog.setVisitId(excuteReq.getVisitId());
            bloodOperLog.setBloodId(excuteReq.getBloodId());
            bloodOperLog.setStatus(excuteReq.getStatus());
            if(ObjectUtil.isNotNull(operUser)){
                bloodOperLog.setOperUserCode(operUser.getUserName());
                bloodOperLog.setOperUserName(operUser.getName());
            }else {
                bloodOperLog.setOperUserCode(currentUser.getUserName());
                bloodOperLog.setOperUserName(currentUser.getName());
            }
            bloodOperLog.setOperTime(now);
            bloodOperLog.setCreateUserCode(currentUser.getUserName());
            bloodOperLog.setCreateUserName(currentUser.getName());
            bloodOperLog.setCreateTime(now);

            bloodOperLogMapper.insert(bloodOperLog);
        }


        // 执行状态
        List<BloodExcute> existExcutes = bloodExcuteMapper.selectBloodStatus(excuteReq.getPatientId(),excuteReq.getVisitId());
        Map<String, List<BloodExcute>> excuteMap = existExcutes.stream().collect(Collectors.groupingBy(BloodExcute::getBloodId));
        if(excuteMap.containsKey(excuteReq.getBloodId())){
            BloodExcute excute = excuteMap.get(excuteReq.getBloodId()).get(0);
            if(3 == excuteReq.getStatus()){
                excute.setStatus(0);
            }else if(4 == excuteReq.getStatus()){
                excute.setStatus(1);
            }else if(5 == excuteReq.getStatus()){
                excute.setStatus(3);
            }else if(6 == excuteReq.getStatus()){
                excute.setStatus(2);
            }
            excute.setExcuteUserCode(currentUser.getUserName());
            excute.setExcuteUserName(currentUser.getName());
            excute.setExcuteTime(now);

            bloodExcuteMapper.updateExcute(excute);
        }else{
            if(3 == excuteReq.getStatus()){
                BloodExcute bloodExcute = new BloodExcute();
                bloodExcute.setPatientId(excuteReq.getPatientId());
                bloodExcute.setVisitId(excuteReq.getVisitId());
                bloodExcute.setBloodId(excuteReq.getBloodId());
                bloodExcute.setStatus(0);
                bloodExcute.setExcuteUserCode(currentUser.getUserName());
                bloodExcute.setExcuteUserName(currentUser.getName());
                bloodExcute.setExcuteTime(now);

                bloodExcuteMapper.insert(bloodExcute);
            }
        }
    }

    private void checkNextStep(Integer cur, Integer next) {
        if(0 == cur.intValue() && 1 != next.intValue()){
            throw new BusinessException("请执行接血!");
        }
        if(1 == cur.intValue() && 2 != next.intValue()){
            throw new BusinessException("请执行核对!");
        }
        if(2 == cur.intValue() && 3 != next.intValue()){
            throw new BusinessException("请执行复核!");
        }
        if(3 == cur.intValue() && 4 != next.intValue()){
            throw new BusinessException("请执行开始执行操作!");
        }
        if(4 == cur.intValue() && 6 != next.intValue()){
            throw new BusinessException("请执行开始执行第二次复核操作!");
        }
        if(6 == cur.intValue() && 5 != next.intValue()){
            throw new BusinessException("请执行下一步操作!");
        }
        if(5 == cur.intValue()){
            throw new BusinessException("已经执行完毕!");
        }
        /*if(6 == cur.intValue() && 4 != next.intValue()){
            throw new BusinessException("请传入执行中状态!");
        }
        if(6 != cur.intValue() && cur.intValue() >= next.intValue()){
            throw new BusinessException("已经执行过该步骤!");
        }*/
    }

    public static void main(String[] args) {
        BloodExcuteReq req = new BloodExcuteReq();
        req.setBloodId("3245235346534599");
        req.setBloodId(req.getBloodId().substring(0,req.getBloodId().length() - 2));
        System.out.println(req.getBloodId());

    }
}
