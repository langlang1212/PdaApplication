package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pda.api.domain.constant.DomainConstant;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.handler.HandleOrderService;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.domain.service.IOrderLabelParamService;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.dto.*;
import com.pda.api.dto.base.BaseExcuteResDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.dto.base.BaseReqDto;
import com.pda.api.dto.query.LogQuery;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.mapper.slave.OrderExcuteLogMapper;
import com.pda.api.service.ExcuteService;
import com.pda.common.Constant;
import com.pda.common.ExcuteStatusEnum;
import com.pda.exception.BusinessException;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import com.pda.utils.PdaTimeUtil;
import com.pda.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname ExcuteServiceImpl
 * @Description TODO
 * @Date 2022-08-06 19:54
 * @Created by AlanZhang
 */
@Service
public class ExcuteServiceImpl implements ExcuteService {

    public static final List<String> TYPES = Arrays.asList(Constant.EXCUTE_TYPE_DRUG,Constant.EXCUTE_TYPE_LIQUID,Constant.EXCUTE_TYPE_ORDER);

    private static final List<String> EXCUTE_ORDER_TYPE = Arrays.asList(Constant.EXCUTE_TYPE_ORDER);

    private static final List<String> STATUS_LIST = Arrays.asList("2");

    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private OrderExcuteLogMapper orderExcuteLogMapper;
    @Autowired
    private IOrderTypeDictService iOrderTypeDictService;
    @Autowired
    private HandleOrderService handleOrderService;
    @Autowired
    private IOrderExcuteLogService iOrderExcuteLogService;

    @Override
    public List<? extends  BaseOrderDto> oralList(String patientId,Integer visitId) {
        List<BaseOrderDto> result = getResList(patientId, visitId);
        return result;
    }

    private List<BaseOrderDto> getResList(String patientId, Integer visitId) {
        List<BaseOrderDto> result = getBaseOrderDtos(patientId, visitId, ModuleTypeEnum.TYPE2,STATUS_LIST);
        return result;
    }

    private List<BaseOrderDto> getBaseOrderDtos(String patientId, Integer visitId, ModuleTypeEnum type2,List<String> statusList) {
        List<BaseOrderDto> result = new ArrayList<>();
        // 查询时间
        Date queryTime = PdaTimeUtil.getTodayOrTomorrow();
        // 口服
        List<String> types = new ArrayList<>();
        types.add(type2.code());
        Set<String> labels = iOrderTypeDictService.findLabelsByType(types);
        // 长期
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId, visitId, queryTime, labels,statusList);
        // 拿到所有核查日志
        BaseReqDto baseReqDto = BaseReqDto.create(patientId, visitId);
        LogQuery logQuery = LogQuery.create(baseReqDto, longOrders, EXCUTE_ORDER_TYPE, queryTime);
        List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findDistinctLog(logQuery);
        // 处理返回数据
        List<BaseOrderDto> longResOrders = handleOrderService.handleOrder(baseReqDto, longOrders, longCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
        // 获取临时医嘱的时间范围
        Date startDateOfDay = DateUtil.getStartDateOfDay();
        Date endDateOfDay = DateUtil.getEndDateOfDay();
        baseReqDto.setRepeatIndicator(0);
        // 查询临时医嘱
        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId, visitId, startDateOfDay, endDateOfDay, labels,statusList);
        // 拿到所有核查日志
        LogQuery shortLogQuery = LogQuery.create(baseReqDto, longOrders, EXCUTE_ORDER_TYPE, queryTime);
        List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(shortLogQuery);
        // 处理返回数据
        List<BaseOrderDto> shortResOrders = handleOrderService.handleOrder(baseReqDto, shortOrders, shortCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
        if (CollectionUtil.isNotEmpty(longCheckedLogs)) {
            result.addAll(longResOrders);
        }
        if (CollectionUtil.isNotEmpty(shortResOrders)) {
            result.addAll(shortResOrders);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "ds2TransactionManager")
    @Override
    public void oralExcute(List<ExcuteReq> oralExcuteReqs) {
        if(CollectionUtil.isEmpty(oralExcuteReqs)){
            throw new BusinessException("口服给药医嘱不能为空!");
        }
        // 登陆人
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        //
        excute(oralExcuteReqs, currentUser, now,Constant.EXCUTE_TYPE_ORDER);

    }

    private void excute(List<ExcuteReq> oralExcuteReqs, UserResDto currentUser, LocalDateTime now,String type) {
        Set<String> labels = getLiquidLabels();

        oralExcuteReqs.forEach(oralExcuteReq -> {
            OrderExcuteLog existLog = getExcuteLog(oralExcuteReq,Constant.EXCUTE_TYPE_ORDER);
            if(ObjectUtil.isNotNull(existLog) && ExcuteStatusEnum.COMPLETED.code().equals(existLog.getExcuteStatus())){
                throw new BusinessException("当前订单："+existLog.getOrderNo()+"今日执行已完成!");
            }
            if(ObjectUtil.isNotNull(existLog)){
                existLog.setExcuteUserCode(currentUser.getUserName());
                existLog.setExcuteUserName(currentUser.getName());
                existLog.setExcuteStatus(oralExcuteReq.getExcuteStatus());
                existLog.setExcuteTime(now);
                if("5".equals(oralExcuteReq.getExcuteStatus())){
                    // 校验配液类的是否核对
                    if(labels.contains(oralExcuteReq.getAdministration())){
                        List<OrderExcuteLog> orderCheckLog = getOrderCheckLog(oralExcuteReq,Constant.EXCUTE_TYPE_LIQUID);
                        if(CollectionUtil.isNotEmpty(orderCheckLog)){
                            throw new BusinessException("当前医嘱没有核对,请先核对医嘱单!");
                        }
                    }
                }
                orderExcuteLogMapper.updateLog(existLog);
            }else{
                OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
                orderExcuteLog.setPatientId(oralExcuteReq.getPatientId());
                orderExcuteLog.setVisitId(oralExcuteReq.getVisitId());
                orderExcuteLog.setOrderNo(oralExcuteReq.getOrderNo());
                orderExcuteLog.setExcuteDate(LocalDateUtils.str2LocalDate(oralExcuteReq.getExcuteDate()));
                orderExcuteLog.setExcuteUserCode(currentUser.getUserName());
                orderExcuteLog.setExcuteUserName(currentUser.getName());
                orderExcuteLog.setExcuteStatus(oralExcuteReq.getExcuteStatus());
                orderExcuteLog.setExcuteTime(now);
                orderExcuteLog.setType(type);
                // 插入
                orderExcuteLogMapper.insert(orderExcuteLog);
            }
        });
    }

    private OrderExcuteLog getExcuteLog(ExcuteReq excuteReq,String type) {
        LambdaQueryWrapper<OrderExcuteLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logLambdaQueryWrapper.eq(OrderExcuteLog::getPatientId,excuteReq.getPatientId()).eq(OrderExcuteLog::getVisitId,excuteReq.getVisitId())
                .eq(OrderExcuteLog::getOrderNo,excuteReq.getOrderNo())
                .eq(OrderExcuteLog::getType,type).eq(OrderExcuteLog::getExcuteDate,excuteReq.getExcuteDate());
        OrderExcuteLog orderExcuteLog = orderExcuteLogMapper.selectOne(logLambdaQueryWrapper);
        return orderExcuteLog;
    }

    private List<OrderExcuteLog> getOrderCheckLog(ExcuteReq excuteReq,String type) {
        LambdaQueryWrapper<OrderExcuteLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logLambdaQueryWrapper.eq(OrderExcuteLog::getPatientId,excuteReq.getPatientId()).eq(OrderExcuteLog::getVisitId,excuteReq.getVisitId())
                .eq(OrderExcuteLog::getOrderNo,excuteReq.getOrderNo())
                .eq(OrderExcuteLog::getType,type).eq(OrderExcuteLog::getCheckStatus,"1");
        List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectList(logLambdaQueryWrapper);
        return orderExcuteLogs;
    }

    @Override
    public List<? extends BaseOrderDto> skinList(String patientId,Integer visitId) {
        List<BaseOrderDto> result = getBaseOrderDtos(patientId, visitId, ModuleTypeEnum.TYPE7,STATUS_LIST);
        return result;
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "ds2TransactionManager")
    @Override
    public void skinExcute(List<ExcuteReq> skinExcuteReqs) {
        if(CollectionUtil.isEmpty(skinExcuteReqs)){
            throw new BusinessException("皮试医嘱不能为空!");
        }
        // 登陆人
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();

        excute(skinExcuteReqs, currentUser, now,Constant.EXCUTE_TYPE_ORDER);
    }

    /**
     * 医嘱执行条数统计
     * @param patientId
     * @return
     */
    @Override
    public OrderCountResDto orderCount(String patientId,Integer visitId,String drugType) {
        // 最后结果
        OrderCountResDto result = new OrderCountResDto();
        // 拿到时间
        Date queryTime = PdaTimeUtil.getTodayOrTomorrow();
        // 拿到所有用法
        Set<String> labels = null;
        if("0".equals(drugType)){  // 为0就是全部
            labels = iOrderTypeDictService.findLabelsByType(null);
        }else{
            labels = iOrderTypeDictService.findLabelsByType(Arrays.asList(drugType));
        }

        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId,visitId, queryTime,labels,STATUS_LIST);
        // 获取操作日志
        BaseReqDto baseReqDto = BaseReqDto.create(patientId,visitId);
        LogQuery logQuery = LogQuery.create(baseReqDto,longOrders,EXCUTE_ORDER_TYPE,queryTime);
        List<OrderExcuteLog> distinctLogs = iOrderExcuteLogService.findDistinctLog(logQuery);
        // 处理医嘱
        handleOrderService.countOrder(baseReqDto,result,longOrders,Constant.CHANG,distinctLogs);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay();
        Date endDateOfDay = DateUtil.getEndDateOfDay();
        baseReqDto.setRepeatIndicator(0);

        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId,visitId,startDateOfDay,endDateOfDay,labels,STATUS_LIST);
        LogQuery logQueryShort = LogQuery.create(baseReqDto,shortOrders,EXCUTE_ORDER_TYPE,queryTime);
        List<OrderExcuteLog> shortDistinctLogs = iOrderExcuteLogService.findDistinctLog(logQueryShort);
        handleOrderService.countOrder(baseReqDto,result,shortOrders,Constant.LINSHI,shortDistinctLogs);

        // 设置剩余的
        result.setSurplusBottles(result.getTotalBottles() - result.getCheckedBottles());
        result.setTempSurplusBottles(result.getTempTotalBottles() - result.getTempCheckedBottles());
        return result;
    }

    /**
     * 医嘱执行列表
     * @param patientId
     * @return
     */
    @Override
    public List<BaseOrderDto> orderExcuteList(String patientId,Integer visitId,String drugType) {
        List<BaseOrderDto> result = new ArrayList<>();

        Date queryTime = PdaTimeUtil.getTodayOrTomorrow();
        List<String> types = new ArrayList<>();
        if("0".equals(drugType)){
            types = ModuleTypeEnum.getAllCodes();
        }else{
            types.add(drugType);
        }
        Set<String> labels = iOrderTypeDictService.findLabelsByType(types);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId,visitId, queryTime,labels,STATUS_LIST);
        // 拿到所有核查日志
        BaseReqDto baseReqDto = BaseReqDto.create(patientId, visitId);
        LogQuery logQuery = LogQuery.create(baseReqDto, longOrders, EXCUTE_ORDER_TYPE, queryTime);
        List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findDistinctLog(logQuery);
        // 处理返回数据
        List<BaseOrderDto> longResOrders = handleOrderService.handleOrder(baseReqDto, longOrders, longCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay();
        Date endDateOfDay = DateUtil.getEndDateOfDay();
        baseReqDto.setRepeatIndicator(0);
        // 查询临时医嘱
        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId, visitId, startDateOfDay, endDateOfDay, labels,STATUS_LIST);
        // 拿到所有核查日志
        LogQuery shortLogQuery = LogQuery.create(baseReqDto, longOrders, EXCUTE_ORDER_TYPE, queryTime);
        List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(shortLogQuery);
        List<BaseOrderDto> shortResOrders = handleOrderService.handleOrder(baseReqDto, shortOrders, shortCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
        if(CollectionUtil.isNotEmpty(longResOrders)){
            result.addAll(longResOrders);
        }
        if(CollectionUtil.isNotEmpty(shortResOrders)){
            result.addAll(shortResOrders);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "ds2TransactionManager")
    @Override
    public void excute(List<ExcuteReq> excuteReqs) {
        if(CollectionUtil.isEmpty(excuteReqs)){
            throw new BusinessException("执行医嘱不能为空!");
        }
        // 登陆人
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        // 当前时间
        LocalDateTime now = LocalDateTime.now();

        excute(excuteReqs, currentUser, now,Constant.EXCUTE_TYPE_ORDER);
    }

    private Set<String> getLiquidLabels() {
        List<String> types = new ArrayList<>();
        types.add(ModuleTypeEnum.TYPE3.code());
        types.add(ModuleTypeEnum.TYPE5.code());
        types.add(ModuleTypeEnum.TYPE6.code());
        return iOrderTypeDictService.findLabelsByType(types);
    }

    private void handleOrder(String patientId,Integer visitId ,CheckCountResDto result, List<OrdersM> orders, Integer repeatRedicator,String type,String excuteDate,Set<String> labels) {
        if(CollectionUtil.isNotEmpty(orders)){
            Map<Integer,List<OrdersM>> orderGroup = orders.stream().collect(Collectors.groupingBy(OrdersM::getOrderNo));
            // 已核查条数
            List<Integer> orderNos = orders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            // 查出已经核查过该病人的医嘱
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectExcuteLatestLog(patientId,visitId,orderNos, type,excuteDate);
            if(CollectionUtil.isNotEmpty(orderGroup)){
                for(Integer orderNo : orderGroup.keySet()){
                    OrdersM ordersM = orderGroup.get(orderNo).get(0);
                    setCount(result,repeatRedicator,orderExcuteLogs,ordersM);
                }
            }
        }
    }

    private void setCount(CheckCountResDto result, Integer repeatRedicator, List<OrderExcuteLog> orderExcuteLogs, OrdersM order) {
        if (1 == repeatRedicator) {
            result.setTotalBottles(result.getTotalBottles() + 1);
        } else {
            result.setTempTotalBottles(result.getTempTotalBottles() + 1);
        }
        if (CollectionUtil.isNotEmpty(orderExcuteLogs)) {
            orderExcuteLogs.forEach(orderExcuteLog -> {
                if (order.getPatientId().equals(orderExcuteLog.getPatientId()) &&
                        order.getOrderNo() == orderExcuteLog.getOrderNo() && order.getVisitId() == orderExcuteLog.getVisitId()) {
                    if (1 == repeatRedicator) {
                        result.setCheckedBottles(result.getCheckedBottles() + 1);
                    } else {
                        result.setTempCheckedBottles(result.getTempCheckedBottles() + 1);
                    }
                }
            });
        }
    }
}
