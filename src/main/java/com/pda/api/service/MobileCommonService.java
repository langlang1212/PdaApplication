package com.pda.api.service;

import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Classname MobileCommonService
 * @Description TODO
 * @Date 2022-10-15 14:53
 * @Created by AlanZhang
 */
public interface MobileCommonService {
    List<OrdersM> handleStopOrder(List<OrdersM> longOrders, Date queryTime);

    void countOtherOrder(BaseCountDto result, Date queryTime,String patientId,Integer visitId);

    void countHisOrder(BaseCountDto result, Date queryTime, String patientId, Integer visitId, Set<String> labels);

    void listOtherOrder(List<BaseOrderDto> result, Date queryTime, String patientId, Integer visitId);

    void listHisOrder(List<BaseOrderDto> result, Date queryTime, String patientId, Integer visitId, Set<String> labels);
}
