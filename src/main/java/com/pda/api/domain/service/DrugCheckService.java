package com.pda.api.domain.service;

import com.pda.api.dto.CheckReqDto;
import com.pda.api.dto.CheckCountResDto;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.api.dto.DrugOrderResDto;

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

    Map<String,List<DrugOrderResDto>> drugOrders(DrugDispensionReqDto dto);

    CheckCountResDto distributionCount(DrugDispensionReqDto dto);

    List<DrugOrderResDto> distributionOrders(DrugDispensionReqDto dto);

    void check(List<CheckReqDto> checkReqDtoList,String type);
}
