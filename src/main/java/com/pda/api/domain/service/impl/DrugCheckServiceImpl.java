package com.pda.api.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.handler.HandleOrderService;
import com.pda.api.domain.service.DrugCheckService;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.dto.*;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.dto.query.LogQuery;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.common.Constant;
import com.pda.common.ExcuteStatusEnum;
import com.pda.exception.BusinessException;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import com.pda.utils.PdaTimeUtil;
import com.pda.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Classname DrugCheckServiceImpl
 * @Description TODO
 * @Date 2022-08-03 21:14
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class DrugCheckServiceImpl implements DrugCheckService {

    private static final List<String> DRUG_TYPES = Arrays.asList(Constant.EXCUTE_TYPE_DRUG);

    private static final List<String> LIQUID_TYPES = Arrays.asList(Constant.EXCUTE_TYPE_LIQUID);

    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private IOrderExcuteLogService iOrderExcuteLogService;
    @Autowired
    private IOrderTypeDictService iOrderTypeDictService;
    @Autowired
    private HandleOrderService handleOrderService;

    /**
     * 摆药统计
     * @param dto
     * @return
     */
    @Override
    public BaseCountDto drugDispensionCount(DrugDispensionReqDto dto) {
        // 1、结果
        BaseCountDto result = new BaseCountDto();
        // 拿到时间
        Date queryTime = PdaTimeUtil.getTodayOrTomorrow(dto.getTodayOrTomorrow());
        // 拿到所有用法
        Set<String> labels = iOrderTypeDictService.findLabelsByType(null);
        // 查询病人所有药
        List<Integer> statusList = Arrays.asList(2,3);
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(dto.getPatientId(),dto.getVisitId(),queryTime,labels,statusList);
        // 获取操作日志
        LogQuery logQuery = LogQuery.create(dto,longOrders,DRUG_TYPES,queryTime);
        List<OrderExcuteLog> distinctLogs = iOrderExcuteLogService.findDistinctLog(logQuery);
        // 处理医嘱
        handleOrderService.countOrder(dto,result,longOrders,Constant.CHANG,distinctLogs);
        // 处理临时医嘱
        Date startDateOfDay = DateUtil.getStartDateOfDay();
        Date endDateOfDay = DateUtil.getEndDateOfDay();
        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(dto.getPatientId(),dto.getVisitId(),startDateOfDay,endDateOfDay,labels,statusList);
        LogQuery logQueryShort = LogQuery.create(dto,shortOrders,DRUG_TYPES,queryTime);
        List<OrderExcuteLog> shortDistinctLogs = iOrderExcuteLogService.findDistinctLog(logQueryShort);
        handleOrderService.countOrder(dto,result,shortOrders,Constant.LINSHI,shortDistinctLogs);
        // 设置剩余的
        result.setSurplusBottles(result.getTotalBottles() - result.getCheckedBottles());
        result.setTempSurplusBottles(result.getTempTotalBottles() - result.getTempCheckedBottles());
        return result;
    }

    /**
     * 摆药明细
     * @param dto
     * @return
     */
    @Override
    public Map<String,List<BaseOrderDto>> drugOrders(DrugDispensionReqDto dto) {
        // 拿到时间
        Date queryTime = PdaTimeUtil.getTodayOrTomorrow(dto.getTodayOrTomorrow());
        // 拿到所有用法
        Set<String> labels = iOrderTypeDictService.findLabelsByType(null);

        List<Integer> statusList = Arrays.asList(2,3);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(dto.getPatientId(),dto.getVisitId(),queryTime,labels,statusList);
        // 拿到所有核查日志
        LogQuery logQuery = LogQuery.create(dto,longOrders,DRUG_TYPES,queryTime);
        List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findOperLog(logQuery);
        // 处理返回数据
        List<BaseOrderDto> longResOrders = handleOrderService.handleOrder(dto,longOrders,longCheckedLogs,Constant.EXCUTE_TYPE_DRUG,queryTime);
        // 临时
        // 获取临时医嘱的时间范围
        Date startDateOfDay = DateUtil.getStartDateOfDay();
        Date endDateOfDay = DateUtil.getEndDateOfDay();
        // 查询临时医嘱
        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(dto.getPatientId(),dto.getVisitId(),startDateOfDay,endDateOfDay,labels,statusList);
        // 拿到所有核查日志
        LogQuery shortLogQuery = LogQuery.create(dto,longOrders,DRUG_TYPES,queryTime);
        List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(shortLogQuery);
        // 处理返回数据
        List<BaseOrderDto> shortResOrders = handleOrderService.handleOrder(dto,shortOrders,shortCheckedLogs,Constant.EXCUTE_TYPE_DRUG,queryTime);

        Map<String,List<BaseOrderDto>> map = new HashMap<>();
        if(CollectionUtil.isNotEmpty(longResOrders)){
            map.put("longOrder",longResOrders);
        }
        if(CollectionUtil.isNotEmpty(shortResOrders)){
            map.put("shortOrder",shortResOrders);
        }
        return map;
    }

    @Transactional(transactionManager = "ds2TransactionManager",rollbackFor = Exception.class)
    @Override
    public void check(List<CheckReqDto> drugCheckReqDtoList,String type) {
        if(CollectionUtil.isEmpty(drugCheckReqDtoList)){
            throw new BusinessException("需要核查的摆药不能为空!");
        }
        // 登陆人
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        // 插入核查日志
        initAddLog(drugCheckReqDtoList, currentUser,type);
    }
    /**
     * 配液核对统计
     * @param dto
     * @return
     */
    @Override
    public BaseCountDto distributionCount(DrugDispensionReqDto dto) {
        // 1、结果
        BaseCountDto result = new BaseCountDto();
        // 拿到时间
        Date queryTime = PdaTimeUtil.getTodayOrTomorrow();

        Set<String> labels = getLiquidLabels();
        //
        List<Integer> statusList = Arrays.asList(2,3);
        // 查询病人所有药
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(dto.getPatientId(),dto.getVisitId(),queryTime,labels,statusList);
        // 获取操作日志
        LogQuery logQuery = LogQuery.create(dto,longOrders,LIQUID_TYPES,queryTime);
        List<OrderExcuteLog> distinctLogs = iOrderExcuteLogService.findDistinctLog(logQuery);
        handleOrderService.countOrder(dto,result,longOrders,Constant.CHANG,distinctLogs);
        // 处理临时医嘱
        // 处理临时医嘱
        Date startDateOfDay = DateUtil.getStartDateOfDay();
        Date endDateOfDay = DateUtil.getEndDateOfDay();
        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(dto.getPatientId(),dto.getVisitId(),startDateOfDay,endDateOfDay,labels,statusList);
        LogQuery logQueryShort = LogQuery.create(dto,shortOrders,LIQUID_TYPES,queryTime);
        List<OrderExcuteLog> shortDistinctLogs = iOrderExcuteLogService.findDistinctLog(logQueryShort);
        handleOrderService.countOrder(dto,result,shortOrders,Constant.LINSHI,shortDistinctLogs);
        // 设置剩余的
        result.setSurplusBottles(result.getTotalBottles() - result.getCheckedBottles());
        result.setTempSurplusBottles(result.getTempTotalBottles() - result.getTempCheckedBottles());
        return result;
    }

    private Set<String> getLiquidLabels() {
        List<String> types = new ArrayList<>();
        types.add(ModuleTypeEnum.TYPE3.code());
        types.add(ModuleTypeEnum.TYPE5.code());
        types.add(ModuleTypeEnum.TYPE6.code());
        return iOrderTypeDictService.findLabelsByType(types);
    }

    @Override
    public Map<String,List<BaseOrderDto>> distributionOrders(DrugDispensionReqDto dto) {
        // 拿到时间
        Date queryTime = PdaTimeUtil.getTodayOrTomorrow();
        // 拿到配液用法
        Set<String> labels = getLiquidLabels();
        //
        List<Integer> statusList = Arrays.asList(2,3);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(dto.getPatientId(),dto.getVisitId(),queryTime,labels,statusList);
        // 拿到所有核查日志
        LogQuery logQuery = LogQuery.create(dto,longOrders,LIQUID_TYPES,queryTime);
        List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findOperLog(logQuery);
        // 处理返回数据
        List<BaseOrderDto> longResOrders = handleOrderService.handleOrder(dto,longOrders,longCheckedLogs,Constant.EXCUTE_TYPE_LIQUID,queryTime);
        // 临时
        // 获取临时医嘱的时间范围
        Date startDateOfDay = DateUtil.getStartDateOfDay();
        Date endDateOfDay = DateUtil.getEndDateOfDay();
        // 查询临时医嘱
        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(dto.getPatientId(),dto.getVisitId(),startDateOfDay,endDateOfDay,labels,statusList);
        // 拿到所有核查日志
        LogQuery shortLogQuery = LogQuery.create(dto,longOrders,LIQUID_TYPES,queryTime);
        List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(shortLogQuery);
        // 处理返回数据
        List<BaseOrderDto> shortResOrders = handleOrderService.handleOrder(dto,shortOrders,shortCheckedLogs,Constant.EXCUTE_TYPE_LIQUID,queryTime);
        // 封装结果
        Map<String,List<BaseOrderDto>> resultMap = new HashMap<>();
        List<BaseOrderDto> noChecked = new ArrayList<>();
        List<BaseOrderDto> checked = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(longResOrders)){
            group(longResOrders, noChecked, checked);
        }
        if(CollectionUtil.isNotEmpty(shortResOrders)){
            group(shortResOrders, noChecked, checked);
        }
        resultMap.put("noChecked",noChecked);
        resultMap.put("checked",checked);
        return resultMap;
    }

    private void group(List<BaseOrderDto> longTimeOrder, List<BaseOrderDto> noChecked, List<BaseOrderDto> checked) {
        List<BaseOrderDto> noCheckedOrder = new ArrayList<>();
        List<BaseOrderDto> checkedOrder = new ArrayList<>();
        groupList(longTimeOrder, noCheckedOrder, checkedOrder);
        noChecked.addAll(noCheckedOrder);
        checked.addAll(checkedOrder);
    }

    private void groupList(List<BaseOrderDto> orders, List<BaseOrderDto> noCheckedOrder, List<BaseOrderDto> checkedOrder) {
        orders.forEach(shortOrder -> {
            if (CollectionUtil.isNotEmpty(shortOrder.getOrderExcuteLogs())) {
                checkedOrder.add(shortOrder);
            } else {
                noCheckedOrder.add(shortOrder);
            }
        });
    }

    private List<OrderExcuteLog> initAddLog(List<CheckReqDto> drugCheckReqDtoList, UserResDto currentUser,String type) {
        List<OrderExcuteLog> addLog = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
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
}
