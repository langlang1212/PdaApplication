package com.pda.api.domain.factory.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.enums.OrderTypeEnum;
import com.pda.api.domain.factory.OrderFactory;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.api.dto.OrderGroupDto;
import com.pda.api.dto.base.BaseReqDto;
import com.pda.common.Constant;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import com.pda.utils.PdaTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Classname OrderFactoryImpl
 * @Description TODO
 * @Date 2023-03-18 20:11
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class OrderFactoryImpl implements OrderFactory {

    @Override
    public Map<String, List<OrdersM>> group(OrderGroupDto groupDto) {
        // 需要返回的结果
        Map<String,List<OrdersM>> group = Maps.newHashMap();
        // 参数处理
        //
        Date startDateOfDay = DateUtil.getTimeOfYestoday();
        Date endDateOfDay = DateUtil.getEndDateOfDay(groupDto.getQueryTime());
        log.info("====================时间1:{},时间2:{},时间3:{}=================",groupDto.getQueryTime(),startDateOfDay,endDateOfDay);
        // 处理订单
        if(CollectionUtil.isNotEmpty(groupDto.getOrders())){
            for(OrdersM o : groupDto.getOrders()){
                // 过滤用法和状态
                if(groupDto.getLabels().contains(o.getAdministration())
                        && groupDto.getStatus().contains(o.getOrderStatus())){
                    if((o.getStartDateTime().isBefore(LocalDateUtils.date2LocalDateTime(groupDto.getQueryTime())) || o.getStartDateTime().isEqual(LocalDateUtils.date2LocalDateTime(groupDto.getQueryTime())))
                            && o.getRepeatIndicator().intValue() == OrderTypeEnum.LONG.value()){
                        // 长期医嘱
                        if(group.containsKey(OrderTypeEnum.LONG.code())){
                            List<OrdersM> longOrders = group.get(OrderTypeEnum.LONG.code());
                            longOrders.add(o);
                        }else {
                            List<OrdersM> longOrders = Lists.newArrayList();
                            longOrders.add(o);
                            group.put(OrderTypeEnum.LONG.code(),longOrders);
                        }
                    }else if ((o.getStartDateTime().isAfter(LocalDateUtils.date2LocalDateTime(startDateOfDay)) || o.getStartDateTime().isEqual(LocalDateUtils.date2LocalDateTime(startDateOfDay)))
                                && (o.getStartDateTime().isBefore(LocalDateUtils.date2LocalDateTime(endDateOfDay)) || o.getStartDateTime().isEqual(LocalDateUtils.date2LocalDateTime(endDateOfDay)))
                                && o.getRepeatIndicator().intValue() == OrderTypeEnum.SHORT.value()){
                        // 临时医嘱
                        if(group.containsKey(OrderTypeEnum.SHORT.code())){
                            List<OrdersM> shortOrders = group.get(OrderTypeEnum.SHORT.code());
                            shortOrders.add(o);
                        }else {
                            List<OrdersM> shortOrders = Lists.newArrayList();
                            shortOrders.add(o);
                            group.put(OrderTypeEnum.SHORT.code(),shortOrders);
                        }
                    }
                }
            }
        }
        return group;
    }

    @Override
    public OrderGroupDto processGroupDto(BaseReqDto dto,List<OrdersM> orders, Set<String> labels, List<String> status,String excuteType) {
        OrderGroupDto groupDto = new OrderGroupDto();
        groupDto.setReq(dto);
        groupDto.setOrders(orders);
        groupDto.setLabels(labels);
        groupDto.setStatus(status);
        groupDto.setExcuteType(excuteType);
        if(Constant.EXCUTE_TYPE_DRUG.equals(excuteType)){
            groupDto.setQueryTime(PdaTimeUtil.getTodayOrTomorrow(((DrugDispensionReqDto) dto).getTodayOrTomorrow()));
        }else {
            groupDto.setQueryTime(PdaTimeUtil.getTodayOrTomorrow());
        }
        log.info("封装好的参数:{}",groupDto.toString());
        return groupDto;
    }

}
