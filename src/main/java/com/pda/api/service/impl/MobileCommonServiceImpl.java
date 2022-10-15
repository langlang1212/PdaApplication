package com.pda.api.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.service.MobileCommonService;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname MobileCommonServiceImpl
 * @Description TODO
 * @Date 2022-10-15 14:53
 * @Created by AlanZhang
 */
@Service
public class MobileCommonServiceImpl implements MobileCommonService {

    @Override
    public List<OrdersM> handleStopOrder(List<OrdersM> longOrders, Date queryTime) {
        longOrders = longOrders.stream().filter(o -> {
            if("2".equals(o.getOrderStatus())){
                return true;
            }else{
                if("3".equals(o.getOrderStatus()) && ObjectUtil.isNotEmpty(o.getStopDateTime())
                        && o.getStopDateTime().isAfter(LocalDateUtils.date2LocalDateTime(DateUtil.getTimeOfYestoday(queryTime)))
                        && o.getStopDateTime().isBefore(LocalDateUtils.date2LocalDateTime(queryTime))){
                    return true;
                }else{
                    return false;
                }
            }
        }).collect(Collectors.toList());
        return longOrders;
    }
}
