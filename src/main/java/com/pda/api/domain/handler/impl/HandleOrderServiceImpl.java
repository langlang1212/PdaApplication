package com.pda.api.domain.handler.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.handler.HandleOrderService;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.dto.base.*;
import com.pda.common.Constant;
import com.pda.common.ExcuteStatusEnum;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import com.pda.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class HandleOrderServiceImpl implements HandleOrderService {

    @Autowired
    private IOrderTypeDictService iOrderTypeDictService;

    @Override
    public void countOrder(BaseCountDto baseCountDto, List<OrdersM> orders, Integer repeatRedicator,List<OrderExcuteLog> logs) {
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
    public List<BaseOrderDto> handleOrder(List<OrdersM> orders, List<OrderExcuteLog> logs,String type,Date queryTime) {
        List<BaseOrderDto> result = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(orders)){
            // 输液的用法
            Set<String> labelsByType3 = iOrderTypeDictService.findLabelsByType(Arrays.asList(ModuleTypeEnum.TYPE3.code()));
            Set<String> labelsByType7 = iOrderTypeDictService.findLabelsByType(Arrays.asList(ModuleTypeEnum.TYPE7.code()));
            Map<Integer, List<OrdersM>> orderGroup = orders.stream().collect(Collectors.groupingBy(OrdersM::getOrderNo));
            for(Integer orderNo : orderGroup.keySet()){
                List<OrdersM> ordersMS = orderGroup.get(orderNo);
                OrdersM firstSubOrder = ordersMS.get(0);
                BaseOrderDto baseOrderDto;
                if(Constant.EXCUTE_TYPE_ORDER.equals(type)){
                    baseOrderDto = new BaseExcuteResDto();
                    // 判断是否是输液的
                    if(labelsByType3.contains(firstSubOrder.getAdministration())){
                        if(labelsByType7.contains(firstSubOrder.getAdministration())){
                            ((BaseExcuteResDto)baseOrderDto).setType(ModuleTypeEnum.TYPE7.code());
                        }else{
                            ((BaseExcuteResDto)baseOrderDto).setType(ModuleTypeEnum.TYPE3.code());
                        }
                    }
                }else{
                    baseOrderDto = new BaseOrderDto();
                }
                if("3".equals(firstSubOrder.getOrderStatus())){
                    baseOrderDto.setIsStop("1");
                }else if("2".equals(firstSubOrder.getOrderStatus())){
                    baseOrderDto.setIsStop("0");
                }
                baseOrderDto.setPatientId(firstSubOrder.getPatientId());
                baseOrderDto.setVisitId(firstSubOrder.getVisitId());
                baseOrderDto.setOrderNo(orderNo);
                if(ObjectUtil.isNotEmpty(firstSubOrder.getFreqCounter())){
                    baseOrderDto.setFrequency(String.format("%s/%s",firstSubOrder.getFreqCounter(),firstSubOrder.getFreqIntervalUnit()));
                }
                baseOrderDto.setExcuteDate(DateUtil.getShortDate(queryTime));
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
                log.info("=======================orderNo:{}=================",baseOrderDto.getOrderNo());
                List<BaseSubOrderDto> subOrderDtoList = new ArrayList<>();
                ordersMS.forEach(ordersM -> {
                    BaseSubOrderDto baseSubOrderDto = new BaseSubOrderDto();
                    baseSubOrderDto.setOrderSubNo(ordersM.getOrderSubNo());
                    baseSubOrderDto.setOrderText(ordersM.getOrderText());
                    baseSubOrderDto.setAdministation(ordersM.getAdministration());
                    if(StringUtils.isNotBlank(ordersM.getDosage())){
                        baseSubOrderDto.setDosage(ordersM.getDosage()+ordersM.getDosageUnits());
                    }
                    baseSubOrderDto.setFreqDetail(ordersM.getFreqDetail());

                    subOrderDtoList.add(baseSubOrderDto);
                });
                baseOrderDto.setSubOrderDtoList(subOrderDtoList);

                List<OrderExcuteLog> checkedLog = new ArrayList<>();
                if(CollectionUtil.isNotEmpty(logs)){
                    if(Constant.EXCUTE_TYPE_ORDER.equals(type)){
                        log.info("处理执行状态");
                        setExcuteStatus((BaseExcuteResDto) baseOrderDto,logs,checkedLog);
                    }else{
                        logs.forEach(orderExcuteLog -> {
                            if(firstSubOrder.getPatientId().equals(orderExcuteLog.getPatientId()) && firstSubOrder.getOrderNo().intValue() == orderExcuteLog.getOrderNo().intValue()
                                    && firstSubOrder.getVisitId().intValue() == orderExcuteLog.getVisitId().intValue()){
                                if(ObjectUtil.isNotNull(baseOrderDto.getLatestOperTime())){
                                    if(orderExcuteLog.getCheckTime().isAfter(baseOrderDto.getLatestOperTime())){
                                        baseOrderDto.setLatestOperTime(orderExcuteLog.getCheckTime());
                                    }
                                }else{
                                    baseOrderDto.setLatestOperTime(orderExcuteLog.getCheckTime());
                                }
                                checkedLog.add(orderExcuteLog);
                            }
                        });
                    }
                }
                //log.info("=============step 2 医嘱编号:{},orderNo:{},状态:{}===============",checkedLog.get(0).getPatientId(),checkedLog.get(0).getOrderNo(),checkedLog.get(0).getExcuteStatus());
                baseOrderDto.setOrderExcuteLogs(checkedLog);
                result.add(baseOrderDto);
            }
            result.sort(((o1, o2) -> o2.getLatestOperTime().compareTo(o1.getLatestOperTime())));
        }
        return result;
    }

    private void setExcuteStatus(BaseExcuteResDto dto,List<OrderExcuteLog> orderExcuteLogs,List<OrderExcuteLog> checkedLog){
        for (OrderExcuteLog orderExcuteLog : orderExcuteLogs) {
            log.info("=============step 1 医嘱编号:{},orderNo:{},visitId:{}===============",dto.getPatientId(),dto.getOrderNo(),dto.getVisitId());
            log.info("=============step 2 医嘱编号:{},orderNo:{},visitId:{},状态:{}===============",orderExcuteLog.getPatientId(),orderExcuteLog.getOrderNo(),orderExcuteLog.getVisitId(),orderExcuteLog.getExcuteStatus());
            if(dto.getPatientId().equals(orderExcuteLog.getPatientId())
                    && dto.getOrderNo().intValue() == orderExcuteLog.getOrderNo().intValue() && dto.getVisitId().intValue() == orderExcuteLog.getVisitId().intValue()){
                if(ObjectUtil.isNotNull(dto.getLatestOperTime())){
                    if(orderExcuteLog.getCheckTime().isAfter(dto.getLatestOperTime())){
                        dto.setLatestOperTime(orderExcuteLog.getCheckTime());
                    }
                }else{
                    dto.setLatestOperTime(orderExcuteLog.getCheckTime());
                }
                log.info("=============step 3 医嘱编号:{},orderNo:{},状态:{}===============",orderExcuteLog.getPatientId(),orderExcuteLog.getOrderNo(),orderExcuteLog.getExcuteStatus());
                checkedLog.add(orderExcuteLog);
                if(Constant.EXCUTE_TYPE_ORDER.equals(orderExcuteLog.getType())){
                    dto.setExcuteStatus(orderExcuteLog.getExcuteStatus());
                }
            }
        }
    }

    private void setCount(BaseCountDto result, Integer repeatRedicator, List<OrderExcuteLog> logs, OrdersM order) {
        if (Constant.CHANG == repeatRedicator) {
            log.info("=========step 1 ：长期医嘱条数:{}，orderNo：{}========",result.getTotalBottles(),order.getOrderNo());
            result.setTotalBottles(result.getTotalBottles() + 1);
            log.info("=========step 2 ：长期医嘱条数:{}，orderNo：{}========",result.getTotalBottles(),order.getOrderNo());
        } else {
            result.setTempTotalBottles(result.getTempTotalBottles() + 1);
        }
        if (CollectionUtil.isNotEmpty(logs)) {
            logs.forEach(orderExcuteLog -> {
                if (order.getPatientId().equals(orderExcuteLog.getPatientId()) &&
                        order.getOrderNo().intValue() == orderExcuteLog.getOrderNo().intValue() && order.getVisitId().intValue() == orderExcuteLog.getVisitId().intValue() /*&& ExcuteStatusEnum.COMPLETED.code().equals(orderExcuteLog.getExcuteStatus())*/) {
                    if(Constant.EXCUTE_TYPE_ORDER.equals(orderExcuteLog.getType())){
                        if(!ExcuteStatusEnum.COMPLETED.code().equals(orderExcuteLog.getExcuteStatus())){
                            return;
                        }
                    }
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
