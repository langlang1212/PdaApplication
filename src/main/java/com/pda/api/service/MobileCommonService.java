package com.pda.api.service;

import com.pda.api.domain.entity.OrdersM;

import java.util.Date;
import java.util.List;

/**
 * @Classname MobileCommonService
 * @Description TODO
 * @Date 2022-10-15 14:53
 * @Created by AlanZhang
 */
public interface MobileCommonService {
    List<OrdersM> handleStopOrder(List<OrdersM> longOrders, Date queryTime);
}
