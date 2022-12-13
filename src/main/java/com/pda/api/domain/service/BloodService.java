package com.pda.api.domain.service;

import com.pda.api.domain.entity.BloodInfo;
import com.pda.api.dto.query.BloodExcuteReq;

import java.util.List;

/**
 * @Classname BloodService
 * @Description TODO
 * @Date 2022-12-12 21:08
 * @Created by AlanZhang
 */
public interface BloodService {

    List<BloodInfo> list(String patientId, Integer visitId);

    void excute(BloodExcuteReq excuteReq);
}
