package com.pda.api.domain.factory;

import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.api.dto.OrderGroupDto;
import com.pda.api.dto.base.BaseReqDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Classname OrderFactory
 * @Description TODO
 * @Date 2023-03-18 20:11
 * @Created by AlanZhang
 */
public interface OrderFactory {
    /**
     * 区分长期医嘱和临时医嘱
     * @param groupDto
     * @return
     */
    Map<String, List<OrdersM>> group(OrderGroupDto groupDto);

    OrderGroupDto processGroupDto(BaseReqDto dto,List<OrdersM> orders,Set<String> labels,List<String> status,String excuteType);
}
