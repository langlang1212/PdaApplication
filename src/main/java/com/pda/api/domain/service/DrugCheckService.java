package com.pda.api.domain.service;

import com.pda.api.dto.CheckReqDto;
import com.pda.api.dto.CheckCountResDto;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.api.dto.DrugOrderResDto;
import com.pda.api.dto.base.BaseOrderDto;

import java.util.List;
import java.util.Map;

/**
 * @Classname DrugCheckService
 * @Description TODO
 * @Date 2022-08-03 21:14
 * @Created by AlanZhang
 */
public interface DrugCheckService {
    CheckCountResDto drugDispensionCount(DrugDispensionReqDto dto);

    Map<String,List<BaseOrderDto>> drugOrders(DrugDispensionReqDto dto);

    CheckCountResDto distributionCount(DrugDispensionReqDto dto);

    Map<String,List<BaseOrderDto>>  distributionOrders(DrugDispensionReqDto dto);

    void check(List<CheckReqDto> checkReqDtoList,String type);
}
