package com.pda.api.domain.excuteBus;

import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;

import java.util.Date;
import java.util.List;

/**
 * @Classname ExcuteStrategy
 * @Description TODO
 * @Date 2022-10-15 22:03
 * @Created by AlanZhang
 */
public interface ExcuteStrategy {

    public void count(BaseCountDto result, Date queryTime,String patientId,Integer visitId);

    public void list(List<BaseOrderDto> result,Date queryTime,String patientId,Integer visitId);
}
