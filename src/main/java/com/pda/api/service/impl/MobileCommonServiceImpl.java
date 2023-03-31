package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.enums.OrderTypeEnum;
import com.pda.api.domain.factory.OrderFactory;
import com.pda.api.domain.handler.HandleOrderService;
import com.pda.api.domain.repository.OrderRepository;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.dto.OrderGroupDto;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.dto.base.BaseReqDto;
import com.pda.api.dto.query.LogQuery;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.service.MobileCommonService;
import com.pda.common.Constant;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname MobileCommonServiceImpl
 * @Description TODO
 * @Date 2022-10-15 14:53
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class MobileCommonServiceImpl implements MobileCommonService {

    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private IOrderExcuteLogService iOrderExcuteLogService;
    @Autowired
    private HandleOrderService handleOrderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderFactory orderFactory;

    @Override
    public List<OrdersM> handleStopOrder(List<OrdersM> longOrders, Date queryTime) {
        log.info("===过滤停止的医嘱，start=====");
        Date now = new Date();
        longOrders = longOrders.stream().filter(o -> {
            if("2".equals(o.getOrderStatus())){
                return true;
            }else{
                log.info("====stopTime:{},time1:{},time2:{}=====",o.getStopDateTime(),LocalDateUtils.date2LocalDateTime(DateUtil.getTimeOfYestoday(now)),LocalDateUtils.date2LocalDateTime(queryTime));
                if("3".equals(o.getOrderStatus()) && ObjectUtil.isNotEmpty(o.getStopDateTime())
                        && o.getStopDateTime().isAfter(LocalDateUtils.date2LocalDateTime(DateUtil.getTimeOfYestoday(now)))
                        && o.getStopDateTime().isBefore(LocalDateUtils.date2LocalDateTime(queryTime))){
                    return true;
                }else{
                    log.info("=====过滤的orderNo：{}====",o.getOrderNo());
                    return false;
                }
            }
        }).collect(Collectors.toList());
        return longOrders;
    }

    @Override
    public void countOtherOrder(BaseCountDto result, Date queryTime,String patientId,Integer visitId) {
        // 1、拿到其他临时医嘱
        Date startDateOfDay = DateUtil.getTimeOfYestoday();
        Date endDateOfDay = DateUtil.getEndDateOfDay(queryTime);
        List<OrdersM> shortOrders = ordersMMapper.listShortOtherOrderByPatient(patientId,visitId,startDateOfDay,endDateOfDay);
        shortOrders = handleStopOrder(shortOrders,queryTime);
        // 1.2、处理日志
        LogQuery logQueryShort = LogQuery.create(patientId,visitId,shortOrders, Arrays.asList(Constant.EXCUTE_TYPE_ORDER,Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID),queryTime);
        List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(logQueryShort);
        handleOrderService.countOrder(result,shortOrders, Constant.LINSHI,shortCheckedLogs,Constant.EXCUTE_TYPE_ORDER);
        //2、拿到其他长期医嘱
        List<OrdersM> longOrders = ordersMMapper.listLongOtherOrderByPatient(patientId,visitId, queryTime);
        longOrders = handleStopOrder(longOrders,queryTime);
        LogQuery logQuerylong = LogQuery.create(patientId,visitId,longOrders, Arrays.asList(Constant.EXCUTE_TYPE_ORDER,Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID),queryTime);
        List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findOperLog(logQuerylong);
        handleOrderService.countOrder(result,longOrders, Constant.CHANG,longCheckedLogs,Constant.EXCUTE_TYPE_ORDER);
    }

    @Override
    public void countHisOrder(BaseCountDto result, Date queryTime, String patientId, Integer visitId,Set<String> labels) {
        BaseReqDto baseReqDto = new BaseReqDto();
        baseReqDto.setPatientId(patientId);
        baseReqDto.setVisitId(visitId);
        // 根据传入参数查询所有医嘱
        List<OrdersM> orders = orderRepository.listOrder(baseReqDto);
        if(CollectionUtil.isEmpty(orders)){
            return;
        }
        // 装入参数
        OrderGroupDto groupDto = orderFactory.processGroupDto(baseReqDto,orders,labels,Constant.STATUS_LIST,Constant.EXCUTE_TYPE_ORDER);
        // 查询所有日志
        LogQuery logQuery = LogQuery.create(baseReqDto,orders,Arrays.asList(Constant.EXCUTE_TYPE_ORDER,Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID),groupDto.getQueryTime());
        List<OrderExcuteLog> orderExcuteLogs = iOrderExcuteLogService.findOperLog(logQuery);
        Map<String, List<OrderExcuteLog>> excuteLogGroup = orderExcuteLogs.stream().collect(Collectors.groupingBy(o -> o.getPatientId() + "-" + o.getVisitId() + "-" + o.getOrderNo()));
        // 区分长期医嘱和临时医嘱
        Map<String,List<OrdersM>> orderGroup = orderFactory.group(groupDto);
        //
        for(String key : orderGroup.keySet()){
            if(OrderTypeEnum.LONG.code().equals(key)){
                List<OrdersM> longOrders = orderGroup.get(key);
                handleOrderService.countOrder(result,longOrders,Constant.CHANG,excuteLogGroup,Constant.EXCUTE_TYPE_ORDER);
            }else {
                List<OrdersM> shortOrders = orderGroup.get(key);
                handleOrderService.countOrder(result,shortOrders,Constant.LINSHI,excuteLogGroup,Constant.EXCUTE_TYPE_ORDER);
            }
        }
    }

    @Override
    public void listOtherOrder(List<BaseOrderDto> result, Date queryTime, String patientId, Integer visitId) {
        // 1、拿到其他临时医嘱
        Date startDateOfDay = DateUtil.getTimeOfYestoday();
        Date endDateOfDay = DateUtil.getEndDateOfDay(queryTime);
        List<OrdersM> shortOrders = ordersMMapper.listShortOtherOrderByPatient(patientId,visitId,startDateOfDay,endDateOfDay);
        shortOrders = handleStopOrder(shortOrders,queryTime);
        // 1.2、处理日志
        LogQuery logQueryShort = LogQuery.create(patientId,visitId,shortOrders, Arrays.asList(Constant.EXCUTE_TYPE_ORDER,Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID),queryTime);
        List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(logQueryShort);
        log.info("处理其他临时医嘱");
        List<BaseOrderDto> shortResOrders = handleOrderService.handleOrder(shortOrders, shortCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
        // 2、长期
        List<OrdersM> longOrders = ordersMMapper.listLongOtherOrderByPatient(patientId,visitId, queryTime);
        longOrders = handleStopOrder(longOrders,queryTime);
        LogQuery logQuerylong = LogQuery.create(patientId,visitId,longOrders, Arrays.asList(Constant.EXCUTE_TYPE_ORDER,Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID),queryTime);
        List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findOperLog(logQuerylong);
        List<BaseOrderDto> longResOrders = handleOrderService.handleOrder(longOrders, longCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
        if (CollectionUtil.isNotEmpty(longResOrders)) {
            result.addAll(longResOrders);
        }
        if (CollectionUtil.isNotEmpty(shortResOrders)) {
            result.addAll(shortResOrders);
        }
    }

    @Override
    public void listHisOrder(List<BaseOrderDto> result, String patientId, Integer visitId, Set<String> labels) {
        BaseReqDto baseReqDto = new BaseReqDto();
        baseReqDto.setPatientId(patientId);
        baseReqDto.setVisitId(visitId);
        // 根据传入参数查询所有医嘱
        Long start1Time = System.currentTimeMillis();
        List<OrdersM> orders = orderRepository.listOrder(baseReqDto);
        Long end1Time = System.currentTimeMillis();
        log.info("查询医嘱单用时:{}",end1Time - start1Time);
        if(CollectionUtil.isEmpty(orders)){
            return;
        }
        // 装入参数
        OrderGroupDto groupDto = orderFactory.processGroupDto(baseReqDto,orders,labels,Constant.STATUS_LIST,Constant.EXCUTE_TYPE_ORDER);
        // 查询所有日志
        Long start2Time = System.currentTimeMillis();
        LogQuery logQuery = LogQuery.create(baseReqDto,orders,Arrays.asList(Constant.EXCUTE_TYPE_ORDER,Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID),groupDto.getQueryTime());
        List<OrderExcuteLog> orderExcuteLogs = iOrderExcuteLogService.findOperLog(logQuery);
        Map<String, List<OrderExcuteLog>> excuteLogGroup = orderExcuteLogs.stream().collect(Collectors.groupingBy(o -> o.getPatientId() + "-" + o.getVisitId() + "-" + o.getOrderNo()));
        Long end2Time = System.currentTimeMillis();
        log.info("查询操作日志用时:{}",end2Time - start2Time);
        // 区分长期医嘱和临时医嘱
        Map<String,List<OrdersM>> orderGroup = orderFactory.group(groupDto);
        //
        Long start3Time = System.currentTimeMillis();
        for(String key : orderGroup.keySet()){
            if(OrderTypeEnum.LONG.code().equals(key)){
                Long longStartTime = System.currentTimeMillis();
                List<OrdersM> longOrders = orderGroup.get(key);
                List<BaseOrderDto> longResOrders = handleOrderService.handleOrder(longOrders, excuteLogGroup, Constant.EXCUTE_TYPE_ORDER, groupDto.getQueryTime());
                if (CollectionUtil.isNotEmpty(longResOrders)) {
                    result.addAll(longResOrders);
                }
                Long longEndTime = System.currentTimeMillis();
                log.info("==============处理长期医嘱时长:{}=================",longEndTime - longStartTime);
            }else {
                Long shortStartTime = System.currentTimeMillis();
                List<OrdersM> shortOrders = orderGroup.get(key);
                List<BaseOrderDto> shortResOrders = handleOrderService.handleOrder(shortOrders, excuteLogGroup, Constant.EXCUTE_TYPE_ORDER, groupDto.getQueryTime());
                if (CollectionUtil.isNotEmpty(shortResOrders)) {
                    result.addAll(shortResOrders);
                }
                Long shortEndTime = System.currentTimeMillis();
                log.info("==============处理临时医嘱时长:{}=================",shortEndTime - shortStartTime);
            }
        }
        Long end3Time = System.currentTimeMillis();
        log.info("handleOrder用时:{}",end3Time - start3Time);
    }
}
