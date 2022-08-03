package com.pda.api.domain.service;

import com.pda.api.dto.DrugDispensingCountResDto;
import com.pda.api.dto.DrugDispensionReqDto;

/**
 * @Classname DrugCheckService
 * @Description TODO
 * @Date 2022-08-03 21:14
 * @Created by AlanZhang
 */
public interface DrugCheckService {
    DrugDispensingCountResDto drugDispensionCount(DrugDispensionReqDto dto);
}
