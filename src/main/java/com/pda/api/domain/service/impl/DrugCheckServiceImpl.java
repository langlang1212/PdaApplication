package com.pda.api.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.enums.OrderTypeEnum;
import com.pda.api.domain.factory.OrderFactory;
import com.pda.api.domain.handler.HandleOrderService;
import com.pda.api.domain.repository.OrderRepository;
import com.pda.api.domain.service.DrugCheckService;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.dto.*;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.dto.query.LogQuery;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.service.MobileCommonService;
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

    private static final List<String> STATUS_LIST = Arrays.asList("2","3");

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderFactory orderFactory;
    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private IOrderExcuteLogService iOrderExcuteLogService;
    @Autowired
    private IOrderTypeDictService iOrderTypeDictService;
    @Autowired
    private HandleOrderService handleOrderService;
    @Autowired
    private MobileCommonService mobileCommonService;

    /**
     * 摆药统计
     * @param dto
     * @return
     */
    @Override
    public BaseCountDto drugDispensionCount(DrugDispensionReqDto dto) {
        // 结果
        BaseCountDto result = new BaseCountDto();
        // 根据传入参数查询所有医嘱
        List<OrdersM> orders = orderRepository.listOrder(dto);
        // 用法
        Set<String> labels = iOrderTypeDictService.findLabelsByType(ModuleTypeEnum.getLiquidType());
        // 装入参数
        OrderGroupDto groupDto = orderFactory.processGroupDto(dto,orders,labels,STATUS_LIST,Constant.EXCUTE_TYPE_DRUG);
        // 区分长期医嘱和临时医嘱
        Map<String,List<OrdersM>> orderGroup = orderFactory.group(groupDto);
        // 处理医嘱
        getCountResult(result, groupDto, orderGroup,DRUG_TYPES);
        return result;
    }

    private void getCountResult(BaseCountDto result, OrderGroupDto groupDto, Map<String, List<OrdersM>> orderGroup,List<String> types) {
        for(String key : orderGroup.keySet()){
            if(OrderTypeEnum.LONG.code().equals(key)){
                // 长期
                List<OrdersM> longOrders = orderGroup.get(key);
                longOrders = mobileCommonService.handleStopOrder(longOrders,groupDto.getQueryTime());
                // 获取操作日志
                LogQuery logQuery = LogQuery.create(groupDto.getReq(),longOrders,types,groupDto.getQueryTime());
                List<OrderExcuteLog> distinctLogs = iOrderExcuteLogService.findDistinctLog(logQuery);
                // 处理医嘱
                handleOrderService.countOrder(result,longOrders, Constant.CHANG,distinctLogs,groupDto.getExcuteType());
            }else {
                // 短期
                List<OrdersM> shortOrders = orderGroup.get(key);
                shortOrders = mobileCommonService.handleStopOrder(shortOrders,groupDto.getQueryTime());
                // 获取操作日志
                LogQuery logQueryShort = LogQuery.create(groupDto.getReq(),shortOrders,types,groupDto.getQueryTime());
                List<OrderExcuteLog> shortDistinctLogs = iOrderExcuteLogService.findDistinctLog(logQueryShort);
                handleOrderService.countOrder(result,shortOrders,Constant.LINSHI,shortDistinctLogs,groupDto.getExcuteType());
            }
        }
        // 设置剩余的
        result.setSurplusBottles(result.getTotalBottles() - result.getCheckedBottles());
        result.setTempSurplusBottles(result.getTempTotalBottles() - result.getTempCheckedBottles());
    }

    /**
     * 摆药明细
     * @param dto
     * @return
     */
    @Override
    public Map<String,List<BaseOrderDto>> drugOrders(DrugDispensionReqDto dto) {
        // 根据传入参数查询所有医嘱
        List<OrdersM> orders = orderRepository.listOrder(dto);
        // 用法
        Set<String> labels = iOrderTypeDictService.findLabelsByType(ModuleTypeEnum.getLiquidType());
        // 装入参数
        OrderGroupDto groupDto = orderFactory.processGroupDto(dto,orders,labels,STATUS_LIST,Constant.EXCUTE_TYPE_DRUG);
        // 区分长期医嘱和临时医嘱
        Map<String,List<OrdersM>> orderGroup = orderFactory.group(groupDto);
        // 处理订单
        Map<String, List<BaseOrderDto>> map = getDrugListResult(dto, groupDto, orderGroup);
        return map;
    }

    private Map<String, List<BaseOrderDto>> getDrugListResult(DrugDispensionReqDto dto, OrderGroupDto groupDto, Map<String, List<OrdersM>> orderGroup) {
        List<BaseOrderDto> longResOrders = Lists.newArrayList();
        List<BaseOrderDto> shortResOrders = Lists.newArrayList();
        for(String key : orderGroup.keySet()){
            if(OrderTypeEnum.LONG.code().equals(key)){
                // 长期医嘱
                List<OrdersM> longOrders = orderGroup.get(key);
                longOrders = mobileCommonService.handleStopOrder(longOrders,groupDto.getQueryTime());
                // 拿到所有核查日志
                LogQuery logQuery = LogQuery.create(dto,longOrders,DRUG_TYPES,groupDto.getQueryTime());
                List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findOperLog(logQuery);
                // 处理返回数据
                longResOrders = handleOrderService.handleOrder(longOrders,longCheckedLogs, Constant.EXCUTE_TYPE_DRUG,groupDto.getQueryTime());
            }else {
                // 临时医嘱
                List<OrdersM> shortOrders = orderGroup.get(key);
                shortOrders = mobileCommonService.handleStopOrder(shortOrders,groupDto.getQueryTime());
                // 拿到所有核查日志
                LogQuery shortLogQuery = LogQuery.create(dto,shortOrders,DRUG_TYPES,groupDto.getQueryTime());
                List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(shortLogQuery);
                // 处理返回数据
                shortResOrders = handleOrderService.handleOrder(shortOrders,shortCheckedLogs,Constant.EXCUTE_TYPE_DRUG,groupDto.getQueryTime());
            }
        }
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
        if(type.equals(Constant.EXCUTE_TYPE_LIQUID)){
            // 添加执行日志
            addExcuteLog(drugCheckReqDtoList,currentUser,Constant.EXCUTE_TYPE_ORDER);
        }
    }

    private void addExcuteLog(List<CheckReqDto> drugCheckReqDtoList, UserResDto currentUser, String type) {
        LocalDateTime now = LocalDateTime.now();
        drugCheckReqDtoList.forEach(drugCheckReqDto -> {
            // 查询已有的核查
            if(excuteLog(drugCheckReqDto,type)){
                // 继续
                OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
                BeanUtils.copyProperties(drugCheckReqDto,orderExcuteLog);
                orderExcuteLog.setExcuteDate(LocalDateUtils.str2LocalDate(drugCheckReqDto.getShouldExcuteDate()));
                orderExcuteLog.setExcuteUserCode(currentUser.getUserName());
                orderExcuteLog.setExcuteUserName(currentUser.getName());
                orderExcuteLog.setExcuteStatus(ExcuteStatusEnum.NO_EXCUTE.code());
                orderExcuteLog.setCheckStatus("1");
                orderExcuteLog.setCheckTime(now);
                orderExcuteLog.setExcuteTime(now);
                orderExcuteLog.setType(type);
                iOrderExcuteLogService.getBaseMapper().insert(orderExcuteLog);
            }
        });
    }

    private Boolean excuteLog(CheckReqDto checkReqDto, String type) {
        List<OrderExcuteLog> logs = iOrderExcuteLogService.findExcuteLog(checkReqDto.getPatientId(),checkReqDto.getVisitId(),checkReqDto.getOrderNo(),checkReqDto.getShouldExcuteDate(),type);
        if(CollectionUtil.isNotEmpty(logs)){
            return false;
        }
        return true;
    }

    /**
     * 配液核对统计
     * @param dto
     * @return
     */
    @Override
    public BaseCountDto distributionCount(DrugDispensionReqDto dto) {
        // 结果
        BaseCountDto result = new BaseCountDto();
        // 根据传入参数查询所有医嘱
        List<OrdersM> orders = orderRepository.listOrder(dto);
        // 用法
        Set<String> labels = iOrderTypeDictService.findLabelsByType(ModuleTypeEnum.getLiquidType());
        // 装入参数
        OrderGroupDto groupDto = orderFactory.processGroupDto(dto,orders,labels,STATUS_LIST,Constant.EXCUTE_TYPE_LIQUID);
        // 区分长期医嘱和临时医嘱
        Map<String,List<OrdersM>> orderGroup = orderFactory.group(groupDto);
        // 处理医嘱
        getCountResult(result, groupDto, orderGroup,LIQUID_TYPES);
        return result;
    }

    @Override
    public Map<String,List<BaseOrderDto>> distributionOrders(DrugDispensionReqDto dto) {
        // 根据传入参数查询所有医嘱
        List<OrdersM> orders = orderRepository.listOrder(dto);
        // 用法
        Set<String> labels = iOrderTypeDictService.findLabelsByType(ModuleTypeEnum.getLiquidType());
        // 装入参数
        OrderGroupDto groupDto = orderFactory.processGroupDto(dto,orders,labels,STATUS_LIST,Constant.EXCUTE_TYPE_DRUG);
        // 区分长期医嘱和临时医嘱
        Map<String,List<OrdersM>> orderGroup = orderFactory.group(groupDto);
        // 封装结果
        Map<String, List<BaseOrderDto>> resultMap = getLiquidResult(dto, groupDto, orderGroup);
        return resultMap;
    }

    private Map<String, List<BaseOrderDto>> getLiquidResult(DrugDispensionReqDto dto, OrderGroupDto groupDto, Map<String, List<OrdersM>> orderGroup) {
        List<BaseOrderDto> longResOrders = Lists.newArrayList();
        List<BaseOrderDto> shortResOrders = Lists.newArrayList();
        for(String key : orderGroup.keySet()){
            if(OrderTypeEnum.LONG.code().equals(key)){
                // 长期医嘱
                List<OrdersM> longOrders = orderGroup.get(key);
                longOrders = mobileCommonService.handleStopOrder(longOrders,groupDto.getQueryTime());
                // 拿到所有核查日志
                LogQuery logQuery = LogQuery.create(dto,longOrders,LIQUID_TYPES,groupDto.getQueryTime());
                List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findOperLog(logQuery);
                // 处理返回数据
                longResOrders = handleOrderService.handleOrder(longOrders,longCheckedLogs, groupDto.getExcuteType(),groupDto.getQueryTime());
            }else {
                // 临时医嘱
                List<OrdersM> shortOrders = orderGroup.get(key);
                shortOrders = mobileCommonService.handleStopOrder(shortOrders,groupDto.getQueryTime());
                // 拿到所有核查日志
                LogQuery shortLogQuery = LogQuery.create(dto,shortOrders,LIQUID_TYPES,groupDto.getQueryTime());
                List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(shortLogQuery);
                // 处理返回数据
                shortResOrders = handleOrderService.handleOrder(shortOrders,shortCheckedLogs,groupDto.getExcuteType(),groupDto.getQueryTime());
            }
        }
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
            // 查询已有的核查
            checkedLog(drugCheckReqDto,type);
            // 继续
            OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
            BeanUtils.copyProperties(drugCheckReqDto,orderExcuteLog);
            orderExcuteLog.setExcuteDate(LocalDateUtils.str2LocalDate(drugCheckReqDto.getShouldExcuteDate()));
            orderExcuteLog.setExcuteUserCode(currentUser.getUserName());
            orderExcuteLog.setExcuteUserName(currentUser.getName());
            orderExcuteLog.setCheckStatus("1");
            orderExcuteLog.setCheckTime(now);
            orderExcuteLog.setType(type);

            addLog.add(orderExcuteLog);

            iOrderExcuteLogService.getBaseMapper().insert(orderExcuteLog);
        });
        return addLog;
    }

    private void checkedLog(CheckReqDto checkReqDto,String type){
        List<OrderExcuteLog> logs = new ArrayList<>();
        if(Constant.EXCUTE_TYPE_DRUG.equals(type)){
            logs = iOrderExcuteLogService.getCheckedLogs(checkReqDto.getPatientId(),checkReqDto.getVisitId(),checkReqDto.getOrderNo(),checkReqDto.getShouldExcuteDate());
            if(CollectionUtil.isNotEmpty(logs) && logs.size() == 2){
                throw new BusinessException("当前医嘱已经核查过两次!");
            }
        }else if(Constant.EXCUTE_TYPE_LIQUID.equals(type)){
            logs = iOrderExcuteLogService.getJiaoduiLogs(checkReqDto.getPatientId(),checkReqDto.getVisitId(),checkReqDto.getOrderNo(),checkReqDto.getShouldExcuteDate());
            if(CollectionUtil.isNotEmpty(logs) && logs.size() == 2){
                throw new BusinessException("当前医嘱已经校对过两次!");
            }
        }
    }
}
