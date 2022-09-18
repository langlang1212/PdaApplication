package com.pda.api.domain.handler;

import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.dto.base.BaseReqDto;

import java.util.List;
import java.util.Set;

/**
 * @Classname HandleOrderService
 * @Description TODO
 * @Date 2022-09-18 17:58
 * @Created by AlanZhang
 */
public interface HandleOrderService {

    void countOrder(BaseReqDto baseReqDto, BaseCountDto baseCountDto, List<OrdersM> orders, Integer repeatRedicator, String type, List<OrderExcuteLog> logs);

    List<BaseOrderDto> handleOrder(BaseReqDto baseReqDto,List<OrdersM> orders,String type,List<OrderExcuteLog> logs);
}
