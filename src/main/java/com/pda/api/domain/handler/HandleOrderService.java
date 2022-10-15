package com.pda.api.domain.handler;

import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.dto.base.BaseReqDto;

import java.util.Date;
import java.util.List;

/**
 * @Classname HandleOrderService
 * @Description TODO
 * @Date 2022-09-18 17:58
 * @Created by AlanZhang
 */
public interface HandleOrderService {

    void countOrder(BaseCountDto baseCountDto, List<OrdersM> orders, Integer repeatRedicator,List<OrderExcuteLog> logs);

    List<BaseOrderDto> handleOrder(List<OrdersM> orders, List<OrderExcuteLog> logs, String type, Date queryTime);
}
