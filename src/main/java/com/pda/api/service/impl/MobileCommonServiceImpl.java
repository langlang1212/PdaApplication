package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.handler.HandleOrderService;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.domain.service.IOrderTypeDictService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
        LogQuery logQueryShort = LogQuery.create(patientId,visitId,shortOrders, Arrays.asList(Constant.EXCUTE_TYPE_ORDER),queryTime);
        List<OrderExcuteLog> shortDistinctLogs = iOrderExcuteLogService.findDistinctLog(logQueryShort);
        handleOrderService.countOrder(result,shortOrders, Constant.LINSHI,shortDistinctLogs);
        //2、拿到其他长期医嘱
        List<OrdersM> longOrders = ordersMMapper.listLongOtherOrderByPatient(patientId,visitId, queryTime);
        longOrders = handleStopOrder(longOrders,queryTime);
        LogQuery logQuerylong = LogQuery.create(patientId,visitId,longOrders, Arrays.asList(Constant.EXCUTE_TYPE_ORDER),queryTime);
        List<OrderExcuteLog> longDistinctLogs = iOrderExcuteLogService.findDistinctLog(logQuerylong);
        handleOrderService.countOrder(result,longOrders, Constant.CHANG,longDistinctLogs);
    }

    @Override
    public void countHisOrder(BaseCountDto result, Date queryTime, String patientId, Integer visitId,Set<String> labels) {
        BaseReqDto baseReqDto = new BaseReqDto();
        baseReqDto.setPatientId(patientId);
        baseReqDto.setVisitId(visitId);
        // 临时
        Date startDateOfDay = DateUtil.getTimeOfYestoday();
        Date endDateOfDay = DateUtil.getEndDateOfDay(queryTime);
        baseReqDto.setRepeatIndicator(0);

        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId,visitId,startDateOfDay,endDateOfDay,labels,Constant.STATUS_LIST);
        LogQuery logQueryShort = LogQuery.create(baseReqDto,shortOrders,Arrays.asList(Constant.EXCUTE_TYPE_ORDER),queryTime);
        List<OrderExcuteLog> shortDistinctLogs = iOrderExcuteLogService.findDistinctLog(logQueryShort);
        handleOrderService.countOrder(result,shortOrders,Constant.LINSHI,shortDistinctLogs);

        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId,visitId, queryTime,labels,Constant.STATUS_LIST);
        // 获取操作日志
        baseReqDto.setRepeatIndicator(1);
        LogQuery logQuery = LogQuery.create(baseReqDto,longOrders,Arrays.asList(Constant.EXCUTE_TYPE_ORDER),queryTime);
        List<OrderExcuteLog> distinctLogs = iOrderExcuteLogService.findDistinctLog(logQuery);
        // 处理医嘱
        handleOrderService.countOrder(result,longOrders,Constant.CHANG,distinctLogs);
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
    public void listHisOrder(List<BaseOrderDto> result, Date queryTime, String patientId, Integer visitId, Set<String> labels) {
        BaseReqDto baseReqDto = new BaseReqDto();
        baseReqDto.setPatientId(patientId);
        baseReqDto.setVisitId(visitId);
        // 临时
        Date startDateOfDay = DateUtil.getTimeOfYestoday();
        Date endDateOfDay = DateUtil.getEndDateOfDay(queryTime);
        baseReqDto.setRepeatIndicator(0);

        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId,visitId,startDateOfDay,endDateOfDay,labels,Constant.STATUS_LIST);
        LogQuery logQueryShort = LogQuery.create(baseReqDto,shortOrders,Arrays.asList(Constant.EXCUTE_TYPE_ORDER,Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID),queryTime);
        List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(logQueryShort);
        List<BaseOrderDto> shortResOrders = handleOrderService.handleOrder(shortOrders, shortCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);

        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId,visitId, queryTime,labels,Constant.STATUS_LIST);
        // 获取操作日志
        baseReqDto.setRepeatIndicator(1);
        LogQuery logQuery = LogQuery.create(baseReqDto,longOrders,Arrays.asList(Constant.EXCUTE_TYPE_ORDER,Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID),queryTime);
        List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findOperLog(logQuery);
        List<BaseOrderDto> longResOrders = handleOrderService.handleOrder(longOrders, longCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
        if (CollectionUtil.isNotEmpty(longCheckedLogs)) {
            result.addAll(longResOrders);
        }
        if (CollectionUtil.isNotEmpty(shortResOrders)) {
            result.addAll(shortResOrders);
        }
    }
}
