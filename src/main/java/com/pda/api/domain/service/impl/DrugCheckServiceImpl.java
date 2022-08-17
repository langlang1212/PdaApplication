package com.pda.api.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrderLabelParam;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.service.DrugCheckService;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.domain.service.IOrderLabelParamService;
import com.pda.api.dto.*;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.mapper.slave.OrderExcuteLogMapper;
import com.pda.common.Constant;
import com.pda.common.ExcuteStatusEnum;
import com.pda.exception.BusinessException;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import com.pda.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname DrugCheckServiceImpl
 * @Description TODO
 * @Date 2022-08-03 21:14
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class DrugCheckServiceImpl implements DrugCheckService {

    private static final String BAIYAO = "m0001";

    private static final Integer CHANG = 1;

    private static final Integer LINSHI = 0;
    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private OrderExcuteLogMapper orderExcuteLogMapper;
    @Autowired
    private IOrderExcuteLogService iOrderExcuteLogService;
    @Autowired
    private IOrderLabelParamService iOrderLabelParamService;

    /**
     * 摆药统计
     * @param dto
     * @return
     */
    @Override
    public CheckCountResDto drugDispensionCount(DrugDispensionReqDto dto) {
        // 1、结果
        CheckCountResDto result = new CheckCountResDto();
        Date queryTime;
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        //Date today = new Date();
        Date today = getTestTime();
        if(Constant.TODAY.equals(dto.getTodayOrTomorrow())){
            queryTime = DateUtil.getStartDateOfDay(today);
        }else{
            queryTime = DateUtil.getStartDateOfTomorrow(today);
        }
        // TODO: 2022-08-09 暂时只查找了输液的数据
        Set<String> labels = iOrderLabelParamService.getLaeblsByModule(BAIYAO);
        // 查询病人所有药
        List<OrdersM> longOrders = ordersMMapper.listByPatientId(dto.getPatientId(),dto.getVisitId(),queryTime);
        handleOrder(dto, result, longOrders,CHANG,Constant.EXCUTE_TYPE_DRUG,DateUtil.getShortDate(today),labels);
        // 处理临时医嘱
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        List<OrdersM> shortOrders = ordersMMapper.listByPatientIdShort(dto.getPatientId(),dto.getVisitId(),startDateOfDay,endDateOfDay);
        handleOrder(dto, result, shortOrders,LINSHI,Constant.EXCUTE_TYPE_DRUG,DateUtil.getShortDate(today),labels);
        // 设置剩余的
        result.setSurplusBottles(result.getTotalBottles() - result.getCheckedBottles());
        result.setTempSurplusBottles(result.getTempTotalBottles() - result.getTempCheckedBottles());
        return result;
    }

    private void handleOrder(DrugDispensionReqDto dto, CheckCountResDto result, List<OrdersM> orders, Integer repeatRedicator,String type,String excuteDate,Set<String> labels) {
        if(CollectionUtil.isNotEmpty(orders)){
            // 已核查条数
            List<Integer> orderNos = orders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            // 查出已经核查过该病人的医嘱
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectCheckedExcuteLog(dto.getPatientId(),dto.getVisitId(),orderNos, type,excuteDate);
            if(CollectionUtil.isNotEmpty(orders)){
                orders.forEach(order -> {
                    if(CollectionUtil.isNotEmpty(labels) && labels.contains(order.getAdministration())){
                        if(CHANG == repeatRedicator){
                            result.setTotalBottles(result.getTotalBottles()+1);
                        }else{
                            result.setTempTotalBottles(result.getTempTotalBottles()+1);
                        }
                        if(CollectionUtil.isNotEmpty(orderExcuteLogs)){
                            orderExcuteLogs.forEach(orderExcuteLog -> {
                                if(order.getPatientId().equals(orderExcuteLog.getPatientId()) &&
                                        order.getOrderNo() == orderExcuteLog.getOrderNo() &&
                                        order.getOrderSubNo() == orderExcuteLog.getOrderSubNo()){
                                    if(CHANG == repeatRedicator){
                                        result.setCheckedBottles(result.getCheckedBottles() + 1);
                                    }else{
                                        result.setTempCheckedBottles(result.getTempCheckedBottles() + 1);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    /**
     * 摆药明细
     * @param dto
     * @return
     */
    @Override
    public Map<String,List<DrugOrderResDto>> drugOrders(DrugDispensionReqDto dto) {
        Date queryTime;
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        //Date today = new Date();
        Date today = getTestTime();
        if(Constant.TODAY.equals(dto.getTodayOrTomorrow())){
            queryTime = DateUtil.getStartDateOfDay(today);
        }else{
            queryTime = DateUtil.getStartDateOfTomorrow(today);
        }
        List<DrugOrderResDto> longTimeOrder = new ArrayList<>();
        /**
         * 1、取今天的医嘱，今天早上0点之前开的医嘱，并且结束时间为null的，都有效
         * 2、取明天的医嘱，明天早上0点之前开的医嘱，并且结束时间为null的，都有效
         * 所以日期应该取明天早上0点
         */
        Set<String> labels = iOrderLabelParamService.getLaeblsByModule(BAIYAO);
        // 长期
        List<OrdersM> orders = ordersMMapper.listByPatientId(dto.getPatientId(),dto.getVisitId(),queryTime);
        handleOrderInfos(dto, queryTime, longTimeOrder, orders,Constant.EXCUTE_TYPE_DRUG,DateUtil.getShortDate(today),labels);
        // 临时
        // 获取临时医嘱的时间范围
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        // 临时医嘱返回值
        List<DrugOrderResDto> shortTimeOrder = new ArrayList<>();
        // 查询临时医嘱
        List<OrdersM> shortOrders = ordersMMapper.listByPatientIdShort(dto.getPatientId(),dto.getVisitId(),startDateOfDay,endDateOfDay);
        handleOrderInfos(dto, today, shortTimeOrder, shortOrders,Constant.EXCUTE_TYPE_DRUG,DateUtil.getShortDate(today),labels);

        Map<String,List<DrugOrderResDto>> map = new HashMap<>();
        if(CollectionUtil.isNotEmpty(longTimeOrder)){
            map.put("longOrder",longTimeOrder);
        }
        if(CollectionUtil.isNotEmpty(shortTimeOrder)){
            map.put("shortOrder",shortTimeOrder);
        }
        return map;
    }

    private void handleOrderInfos(DrugDispensionReqDto dto, Date queryTime, List<DrugOrderResDto> resOrders, List<OrdersM> orders,String type,String excuteDate,Set<String> labels) {
        if(CollectionUtil.isNotEmpty(orders)){
            List<Integer> orderNos = orders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            // 3、查出已经核查过该病人的医嘱
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectCheckedExcuteLog(dto.getPatientId(),dto.getVisitId(),orderNos, type,excuteDate);

            Map<Integer, List<OrdersM>> orderGroup = orders.stream().collect(Collectors.groupingBy(OrdersM::getOrderNo));
            for(Integer orderNo : orderGroup.keySet()){
                List<OrdersM> ordersMS = orderGroup.get(orderNo);
                OrdersM firstSubOrder = ordersMS.get(0);
                if(CollectionUtil.isNotEmpty(labels) && labels.contains(firstSubOrder.getAdministration())){
                    // 第一步初始化
                    DrugOrderResDto drugOrderResDto = new DrugOrderResDto();
                    drugOrderResDto.setPatientId(firstSubOrder.getPatientId());
                    drugOrderResDto.setVisitId(firstSubOrder.getVisitId());
                    drugOrderResDto.setOrderNo(orderNo);
                    drugOrderResDto.setFrequency(String.format("%s/%s",firstSubOrder.getFreqCounter(),firstSubOrder.getFreqIntervalUnit()));
                    drugOrderResDto.setExcuteDate(DateUtil.getShortDate(queryTime));
                    drugOrderResDto.setStartDateTime(firstSubOrder.getStartDateTime());
                    drugOrderResDto.setRepeatIndicator(firstSubOrder.getRepeatIndicator());
                    if(StringUtils.isNotBlank(firstSubOrder.getPerformSchedule())){
                        String[] split = firstSubOrder.getPerformSchedule().split("-");
                        if(split.length == 1){
                            List<String> schedule = new ArrayList<>();
                            schedule.add(split[0]);
                            drugOrderResDto.setSchedule(schedule);
                        }else{
                            drugOrderResDto.setSchedule(Arrays.asList(split));
                        }
                    }
                    drugOrderResDto.setStopDateTime(firstSubOrder.getStopDateTime());

                    List<DrugSubOrderDto> subOrderDtoList = new ArrayList<>();
                    ordersMS.forEach(ordersM -> {
                        DrugSubOrderDto drugSubOrderDto = new DrugSubOrderDto();
                        drugSubOrderDto.setOrderSubNo(ordersM.getOrderSubNo());
                        drugSubOrderDto.setOrderText(ordersM.getOrderText());
                        drugSubOrderDto.setAdministation(ordersM.getAdministration());
                        drugSubOrderDto.setDosage(String.format("%s%s",ordersM.getDosage(),ordersM.getDosageUnits()));
                        drugSubOrderDto.setFreqDetail(ordersM.getFreqDetail());

                        subOrderDtoList.add(drugSubOrderDto);
                    });
                    drugOrderResDto.setSubOrderDtoList(subOrderDtoList);

                    List<OrderExcuteLog> checkedLog = new ArrayList<>();
                    if(CollectionUtil.isNotEmpty(orderExcuteLogs)){
                        orderExcuteLogs.forEach(orderExcuteLog -> {
                            if(firstSubOrder.getOrderNo() == orderExcuteLog.getOrderNo()){
                                checkedLog.add(orderExcuteLog);
                            }
                        });
                    }
                    drugOrderResDto.setOrderExcuteLogs(checkedLog);
                    resOrders.add(drugOrderResDto);
                }
            }
        }
    }

    @Transactional(transactionManager = "ds2TransactionManager",rollbackFor = Exception.class)
    @Override
    public void check(List<CheckReqDto> drugCheckReqDtoList,String type) {
        if(CollectionUtil.isEmpty(drugCheckReqDtoList)){
            throw new BusinessException("需要核查的摆药不能为空!");
        }
        // 登陆人
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 插入核查日志
        List<OrderExcuteLog> addLog = initAddLog(drugCheckReqDtoList, currentUser, now,type);
        // 插入
        /*if(CollectionUtil.isNotEmpty(addLog)){
            iOrderExcuteLogService.saveBatch(addLog);
        }*/
    }
    /**
     * 配液核对统计
     * @param dto
     * @return
     */
    @Override
    public CheckCountResDto distributionCount(DrugDispensionReqDto dto) {
        // 1、结果
        CheckCountResDto result = new CheckCountResDto();
        Date queryTime;
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        //Date today = new Date();
        Date today = getTestTime();
        if(Constant.TODAY.equals(dto.getTodayOrTomorrow())){
            queryTime = DateUtil.getStartDateOfDay(today);
        }else{
            queryTime = DateUtil.getStartDateOfTomorrow(today);
        }
        List<Integer> labelParams = new ArrayList<>();
        labelParams.add(1001);
        Set<String> labels = iOrderLabelParamService.labels(labelParams);

        List<OrdersM> longOrders = ordersMMapper.listByPatientId(dto.getPatientId(),dto.getVisitId(),queryTime);
        handleOrder(dto, result, longOrders,CHANG,Constant.EXCUTE_TYPE_LIQUID,DateUtil.getShortDate(today),labels);
        // 处理临时医嘱
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        List<OrdersM> shortOrders = ordersMMapper.listByPatientIdShort(dto.getPatientId(),dto.getVisitId(),startDateOfDay,endDateOfDay);
        handleOrder(dto, result, shortOrders,LINSHI,Constant.EXCUTE_TYPE_LIQUID,DateUtil.getShortDate(today),labels);
        // 设置剩余的
        result.setSurplusBottles(result.getTotalBottles() - result.getCheckedBottles());
        result.setTempSurplusBottles(result.getTempTotalBottles() - result.getTempCheckedBottles());
        return result;
    }

    @Override
    public Map<String,List<DrugOrderResDto>> distributionOrders(DrugDispensionReqDto dto) {
        List<DrugOrderResDto> result = new ArrayList<>();
        Date queryTime;
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        //Date today = new Date();
        Date today = getTestTime();
        if(Constant.TODAY.equals(dto.getTodayOrTomorrow())){
            queryTime = DateUtil.getStartDateOfDay(today);
        }else{
            queryTime = DateUtil.getStartDateOfTomorrow(today);
        }
        List<Integer> labelParams = new ArrayList<>();
        labelParams.add(1001);
        Set<String> labels = iOrderLabelParamService.labels(labelParams);

        List<DrugOrderResDto> longTimeOrder = new ArrayList<>();
        // 长期
        List<OrdersM> orders = ordersMMapper.listByPatientId(dto.getPatientId(),dto.getVisitId(),queryTime);
        handleOrderInfos(dto, queryTime, longTimeOrder, orders,Constant.EXCUTE_TYPE_LIQUID,DateUtil.getShortDate(today),labels);
        // 临时
        // 获取临时医嘱的时间范围
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        // 临时医嘱返回值
        List<DrugOrderResDto> shortTimeOrder = new ArrayList<>();
        // 查询临时医嘱
        List<OrdersM> shortOrders = ordersMMapper.listByPatientIdShort(dto.getPatientId(),dto.getVisitId(),startDateOfDay,endDateOfDay);
        handleOrderInfos(dto, today, shortTimeOrder, shortOrders,Constant.EXCUTE_TYPE_LIQUID,DateUtil.getShortDate(today),labels);
        // 封装结果
        Map<String,List<DrugOrderResDto>> resultMap = new HashMap<>();
        List<DrugOrderResDto> noChecked = new ArrayList<>();
        List<DrugOrderResDto> checked = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(shortTimeOrder)){
            group(shortTimeOrder, noChecked, checked);
        }
        if(CollectionUtil.isNotEmpty(longTimeOrder)){
            group(longTimeOrder, noChecked, checked);
        }
        resultMap.put("noChecked",noChecked);
        resultMap.put("checked",checked);
        return resultMap;
    }

    private void group(List<DrugOrderResDto> longTimeOrder, List<DrugOrderResDto> noChecked, List<DrugOrderResDto> checked) {
        List<DrugOrderResDto> noCheckedOrder = new ArrayList<>();
        List<DrugOrderResDto> checkedOrder = new ArrayList<>();
        groupList(longTimeOrder, noCheckedOrder, checkedOrder);
        noChecked.addAll(noCheckedOrder);
        checked.addAll(checkedOrder);
    }

    private void groupList(List<DrugOrderResDto> orders, List<DrugOrderResDto> noCheckedOrder, List<DrugOrderResDto> checkedOrder) {
        orders.forEach(shortOrder -> {
            if (CollectionUtil.isNotEmpty(shortOrder.getOrderExcuteLogs())) {
                checkedOrder.add(shortOrder);
            } else {
                noCheckedOrder.add(shortOrder);
            }
        });
    }

    private List<OrderExcuteLog> initAddLog(List<CheckReqDto> drugCheckReqDtoList, UserResDto currentUser, LocalDateTime now,String type) {
        List<OrderExcuteLog> addLog = new ArrayList<>();
        drugCheckReqDtoList.forEach(drugCheckReqDto -> {
            OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
            BeanUtils.copyProperties(drugCheckReqDto,orderExcuteLog);
            orderExcuteLog.setExcuteDate(LocalDateUtils.str2LocalDate(drugCheckReqDto.getShouldExcuteDate()));
            orderExcuteLog.setExcuteUserCode(currentUser.getUserName());
            orderExcuteLog.setExcuteUserName(currentUser.getName());
            orderExcuteLog.setExcuteStatus(ExcuteStatusEnum.PREPARED.code());
            orderExcuteLog.setCheckStatus("1");
            orderExcuteLog.setCheckTime(now);
            orderExcuteLog.setType(type);

            addLog.add(orderExcuteLog);

            iOrderExcuteLogService.getBaseMapper().insert(orderExcuteLog);
        });
        return addLog;
    }


    private Date getTestTime(){
        String str = "2022-07-29 12:32:54";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
