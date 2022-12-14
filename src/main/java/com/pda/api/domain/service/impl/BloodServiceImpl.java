package com.pda.api.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
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
import com.pda.exception.BusinessException;
import com.pda.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Override
    public List<BloodInfo> list(String patientId, Integer visitId) {
        // 查出所有输血送血
        List<BloodInfo> bloodInfos = mobileCommonMapper.selectBlood(patientId, visitId);
        if(CollectionUtil.isNotEmpty(bloodInfos)){
            // 查询状态
            List<BloodExcute> bloodStatus = bloodMapper.selectBloodStatus(patientId, visitId);
            Map<String, List<BloodExcute>> bloodStatusMap = bloodStatus.stream().collect(Collectors.groupingBy(b -> b.getPatientId() + "_" + b.getVisitId() + "_" + b.getBloodId()));
            // 袋数
            int size = bloodInfos.size();
            bloodInfos.forEach(b -> {
                b.setBloodQty(size);
                // 设置状态
                String key = b.getPatientId() + "_" + b.getVisitId() + "_" + b.getBloodId();
                if(CollectionUtil.isNotEmpty(bloodStatusMap) && bloodStatusMap.containsKey(key)){
                    BloodExcute status = bloodStatusMap.get(key).get(0);
                    b.setStatus(status.getStatus());
                }
            });
        }
        return bloodInfos;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void excute(BloodExcuteReq excuteReq) {
        // 1、获取当前用户
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        // 2、执行逻辑
        List<BloodOperLog> logs = bloodMapper.selectLogs(excuteReq.getPatientId(),excuteReq.getVisitId(),excuteReq.getBloodId());
        List<Integer> status = logs.stream().map(BloodOperLog::getStatus).collect(Collectors.toList());
        // 3、
        if(!checkStatus(status,excuteReq.getStatus())){
            throw new BusinessException("已执行过该步骤!");
        }
        // 4、
        Date now = new Date();
        // 操作步骤
        if( 6 != excuteReq.getStatus()){
            BloodOperLog bloodOperLog  = new BloodOperLog();
            bloodOperLog.setPatientId(excuteReq.getPatientId());
            bloodOperLog.setVisitId(excuteReq.getVisitId());
            bloodOperLog.setBloodId(excuteReq.getBloodId());
            bloodOperLog.setStatus(excuteReq.getStatus());
            bloodOperLog.setOperUserCode(currentUser.getUserName());
            bloodOperLog.setOperUserName(currentUser.getName());
            bloodOperLog.setOperTime(now);
            bloodOperLog.setCreateUserCode(currentUser.getUserName());
            bloodOperLog.setCreateUserName(currentUser.getName());
            bloodOperLog.setCreateTime(now);

            bloodOperLogMapper.insert(bloodOperLog);
        }

        // 执行状态
        List<BloodExcute> existExcutes = bloodMapper.selectBloodStatus(excuteReq.getPatientId(),excuteReq.getVisitId());
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

    private boolean checkStatus(List<Integer> status, Integer param) {
        for(Integer statu : status){
            if(statu.intValue() == param.intValue()){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        /*BloodExcuteReq req = new BloodExcuteReq();
        req.setStatus(0);
        BloodOperLog e1 = new BloodOperLog();
        e1.setStatus(0);
        BloodOperLog e2 = new BloodOperLog();
        e2.setStatus(1);
        BloodOperLog e3 = new BloodOperLog();
        e3.setStatus(2);
        BloodOperLog e4 = new BloodOperLog();
        e4.setStatus(3);
        List<BloodOperLog> logs = new ArrayList<>();
        logs.add(e1);
        logs.add(e2);
        logs.add(e3);
        logs.add(e4);
        List<Integer> status = logs.stream().map(BloodOperLog::getStatus).collect(Collectors.toList());
        if(!checkStatus(status,req.getStatus())){
            System.out.println("验证不通过");
        }
        System.out.println(checkStatus(status,req.getStatus()));*/
    }
}
