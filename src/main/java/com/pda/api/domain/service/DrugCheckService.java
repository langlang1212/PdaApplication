package com.pda.api.domain.service;

import com.pda.api.dto.DrugDispensingCountResDto;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.api.dto.DrugOrderResDto;

import java.util.List;

/**
 * @Classname DrugCheckService
 * @Description TODO
 * @Date 2022-08-03 21:14
 * @Created by AlanZhang
 */
public interface DrugCheckService {
    DrugDispensingCountResDto drugDispensionCount(DrugDispensionReqDto dto);

    List<DrugOrderResDto> drugOrders(DrugDispensionReqDto dto);
}
