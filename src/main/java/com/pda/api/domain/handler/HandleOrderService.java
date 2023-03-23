package com.pda.api.domain.handler;

import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.dto.base.BaseReqDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname HandleOrderService
 * @Description TODO
 * @Date 2022-09-18 17:58
 * @Created by AlanZhang
 */
public interface HandleOrderService {

    void countOrder(BaseCountDto baseCountDto, List<OrdersM> orders, Integer repeatRedicator,List<OrderExcuteLog> logs,String type);

    void countOrder(BaseCountDto baseCountDto, List<OrdersM> orders, Integer repeatRedicator,Map<String, List<OrderExcuteLog>> excuteLogGroup,String type);

    List<BaseOrderDto> handleOrder(List<OrdersM> orders, List<OrderExcuteLog> logs, String type, Date queryTime);

    List<BaseOrderDto> handleOrder(List<OrdersM> orders, Map<String, List<OrderExcuteLog>> excuteLogGroup, String type, Date queryTime);
}
