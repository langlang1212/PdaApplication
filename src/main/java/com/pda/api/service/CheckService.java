package com.pda.api.service;

import com.pda.api.dto.SpecimenCheckCountDto;
import com.pda.api.dto.SpecimenCheckResDto;

import java.util.List;

/**
 * @Classname CheckService
 * @Description TODO
 * @Date 2022-07-22 18:12
 * @Created by AlanZhang
 */
public interface CheckService {
    String findCheckoutInfo(Integer pageNum);

    String findCheckInfo(Integer pageNum);

    String findCheckApplyInfo(Integer pageNum);

    List<SpecimenCheckResDto> specimenCheck(String patientId, Integer visitId);

    SpecimenCheckCountDto specimenCheckCount(String patientId, Integer visitId);
}
