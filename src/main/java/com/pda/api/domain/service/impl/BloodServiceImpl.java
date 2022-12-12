package com.pda.api.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.pda.api.domain.entity.BloodInfo;
import com.pda.api.domain.service.BloodService;
import com.pda.api.mapper.primary.MobileCommonMapper;
import com.pda.api.mapper.slave.BloodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<BloodInfo> list(String patientId, Integer visitId) {
        // 查出所有输血送血
        List<BloodInfo> bloodInfos = mobileCommonMapper.selectBlood(patientId, visitId);
        if(CollectionUtil.isNotEmpty(bloodInfos)){
            // 查询状态
            List<BloodInfo> bloodStatus = bloodMapper.selectBloodStatus(patientId, visitId);
            Map<String, List<BloodInfo>> bloodStatusMap = bloodStatus.stream().collect(Collectors.groupingBy(b -> b.getPatientId() + "_" + b.getVisitId() + "_" + b.getBloodId()));
            // 袋数
            int size = bloodInfos.size();
            bloodInfos.forEach(b -> {
                b.setBloodQty(size);

                String key = b.getPatientId() + "_" + b.getVisitId() + "_" + b.getBloodId();
                if(CollectionUtil.isNotEmpty(bloodStatusMap) && bloodStatusMap.containsKey(key)){
                    BloodInfo status = bloodStatusMap.get(key).get(0);
                    b.setStatus(status.getStatus());
                }
            });
        }
        return bloodInfos;
    }
}
