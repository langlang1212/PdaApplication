package com.pda.api.domain.handler.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.handler.HandleOrderService;
import com.pda.api.dto.CheckCountResDto;
import com.pda.api.dto.DrugOrderResDto;
import com.pda.api.dto.DrugSubOrderDto;
import com.pda.api.dto.base.*;
import com.pda.common.Constant;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname HandleOrderServiceImpl
 * @Description TODO
 * @Date 2022-09-18 17:58
 * @Created by AlanZhang
 */
@Component
public class HandleOrderServiceImpl implements HandleOrderService {

    @Override
    public void countOrder(BaseReqDto baseReqDto, BaseCountDto baseCountDto, List<OrdersM> orders, Integer repeatRedicator,List<OrderExcuteLog> logs) {
        if(CollectionUtil.isNotEmpty(orders)){
            Map<Integer,List<OrdersM>> orderGroup = orders.stream().collect(Collectors.groupingBy(OrdersM::getOrderNo));
            if(CollectionUtil.isNotEmpty(orderGroup)){
                for(Integer orderNo : orderGroup.keySet()){
                    OrdersM order = orderGroup.get(orderNo).get(0);
                    setCount(baseCountDto, repeatRedicator, logs, order);
                }
            }
        }
    }

    @Override
    public List<BaseOrderDto> handleOrder(BaseReqDto baseReqDto, List<OrdersM> orders, List<OrderExcuteLog> logs,String type) {
        List<BaseOrderDto> result = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(orders)){
            Map<Integer, List<OrdersM>> orderGroup = orders.stream().collect(Collectors.groupingBy(OrdersM::getOrderNo));
            for(Integer orderNo : orderGroup.keySet()){
                List<OrdersM> ordersMS = orderGroup.get(orderNo);
                OrdersM firstSubOrder = ordersMS.get(0);
                BaseOrderDto baseOrderDto;
                if(Constant.EXCUTE_TYPE_ORDER.equals(type)){
                    baseOrderDto = new BaseExcuteResDto();
                }else{
                    baseOrderDto = new BaseOrderDto();
                }
                baseOrderDto.setPatientId(firstSubOrder.getPatientId());
                baseOrderDto.setVisitId(firstSubOrder.getVisitId());
                baseOrderDto.setOrderNo(orderNo);
                baseOrderDto.setFrequency(String.format("%s/%s",firstSubOrder.getFreqCounter(),firstSubOrder.getFreqIntervalUnit()));
                baseOrderDto.setExcuteDate(DateUtil.getShortDate(LocalDateUtils.localDate2Date(logs.get(0).getExcuteDate())));
                baseOrderDto.setStartDateTime(firstSubOrder.getStartDateTime());
                baseOrderDto.setRepeatIndicator(firstSubOrder.getRepeatIndicator());
                if(StringUtils.isNotBlank(firstSubOrder.getPerformSchedule())){
                    String[] split = firstSubOrder.getPerformSchedule().split("-");
                    if(split.length == 1){
                        List<String> schedule = new ArrayList<>();
                        schedule.add(split[0]);
                        baseOrderDto.setSchedule(schedule);
                    }else{
                        baseOrderDto.setSchedule(Arrays.asList(split));
                    }
                }
                baseOrderDto.setStopDateTime(firstSubOrder.getStopDateTime());

                List<BaseSubOrderDto> subOrderDtoList = new ArrayList<>();
                ordersMS.forEach(ordersM -> {
                    BaseSubOrderDto baseSubOrderDto = new BaseSubOrderDto();
                    baseSubOrderDto.setOrderSubNo(ordersM.getOrderSubNo());
                    baseSubOrderDto.setOrderText(ordersM.getOrderText());
                    baseSubOrderDto.setAdministation(ordersM.getAdministration());
                    baseSubOrderDto.setDosage(ordersM.getDosage()+ordersM.getDosageUnits());
                    baseSubOrderDto.setFreqDetail(ordersM.getFreqDetail());

                    subOrderDtoList.add(baseSubOrderDto);
                });
                baseOrderDto.setSubOrderDtoList(subOrderDtoList);

                List<OrderExcuteLog> checkedLog = new ArrayList<>();
                if(CollectionUtil.isNotEmpty(logs)){
                    if(Constant.EXCUTE_TYPE_ORDER.equals(type)){
                        setExcuteStatus((BaseExcuteResDto) baseOrderDto,logs,checkedLog);
                    }else{
                        logs.forEach(orderExcuteLog -> {
                            if(firstSubOrder.getPatientId().equals(orderExcuteLog.getPatientId()) && firstSubOrder.getOrderNo() == orderExcuteLog.getOrderNo() && firstSubOrder.getVisitId() == orderExcuteLog.getVisitId()){
                                checkedLog.add(orderExcuteLog);
                            }
                        });
                    }
                }
                baseOrderDto.setOrderExcuteLogs(checkedLog);
                result.add(baseOrderDto);
            }
            Collections.sort(result);
        }
        return null;
    }

    private void setExcuteStatus(BaseExcuteResDto dto,List<OrderExcuteLog> orderExcuteLogs,List<OrderExcuteLog> checkedLog){
        for (OrderExcuteLog orderExcuteLog : orderExcuteLogs) {
            if(dto.getPatientId().equals(orderExcuteLog.getPatientId())
                    && dto.getOrderNo() == orderExcuteLog.getOrderNo() && dto.getVisitId() == orderExcuteLog.getVisitId()){
                checkedLog.add(orderExcuteLog);
                if("3".equals(orderExcuteLog.getType())){
                    dto.setExcuteStatus(orderExcuteLog.getExcuteStatus());
                }
            }
        }
    }

    private void setCount(BaseCountDto result, Integer repeatRedicator, List<OrderExcuteLog> logs, OrdersM order) {
        if (Constant.CHANG == repeatRedicator) {
            result.setTotalBottles(result.getTotalBottles() + 1);
        } else {
            result.setTempTotalBottles(result.getTempTotalBottles() + 1);
        }
        if (CollectionUtil.isNotEmpty(logs)) {
            logs.forEach(orderExcuteLog -> {
                if (order.getPatientId().equals(orderExcuteLog.getPatientId()) &&
                        order.getOrderNo() == orderExcuteLog.getOrderNo() && order.getVisitId() == orderExcuteLog.getVisitId()) {
                    if (Constant.CHANG == repeatRedicator) {
                        result.setCheckedBottles(result.getCheckedBottles() + 1);
                    } else {
                        result.setTempCheckedBottles(result.getTempCheckedBottles() + 1);
                    }
                }
            });
        }
    }
}
