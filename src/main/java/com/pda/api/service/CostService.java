package com.pda.api.service;

import com.pda.api.dto.CostReqDto;

/**
 * @Classname CostService
 * @Description TODO
 * @Date 2022-07-22 15:20
 * @Created by AlanZhang
 */
public interface CostService {

    String fintCostInfo(Integer pageNum, String patientNo);
}
