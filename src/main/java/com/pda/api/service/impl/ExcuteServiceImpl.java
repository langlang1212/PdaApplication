package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pda.api.domain.constant.DomainConstant;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.excuteBus.ExcuteStrategy;
import com.pda.api.domain.excuteBus.Strategy.AllStrategy;
import com.pda.api.domain.excuteBus.Strategy.OtherStrategy;
import com.pda.api.domain.excuteBus.Strategy.SingleStrategy;
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
import com.pda.api.service.MobileCommonService;
import com.pda.common.Constant;
import com.pda.common.ExcuteStatusEnum;
import com.pda.exception.BusinessException;
import com.pda.job.ExcuteLogJob;
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

    private static final List<String> STATUS_LIST = Arrays.asList("1","2","3");

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
    @Autowired
    private MobileCommonService mobileCommonService;
    @Autowired
    private ExcuteLogJob excuteLogJob;

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
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId, visitId, queryTime, labels,statusList);
        longOrders = mobileCommonService.handleStopOrder(longOrders, queryTime);
        // 拿到所有核查日志
        BaseReqDto baseReqDto = BaseReqDto.create(patientId, visitId);
        LogQuery logQuery = LogQuery.create(baseReqDto, longOrders, STATUS_LIST, queryTime);
        List<OrderExcuteLog> longCheckedLogs = iOrderExcuteLogService.findOperLog(logQuery);
        // 处理返回数据
        List<BaseOrderDto> longResOrders = handleOrderService.handleOrder(longOrders, longCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
        // 获取临时医嘱的时间范围
        Date startDateOfDay = DateUtil.getTimeOfYestoday();
        Date endDateOfDay = DateUtil.getEndDateOfDay(queryTime);
        baseReqDto.setRepeatIndicator(0);
        // 查询临时医嘱
        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId, visitId, startDateOfDay, endDateOfDay, labels,statusList);
        shortOrders = mobileCommonService.handleStopOrder(shortOrders, queryTime);
        // 拿到所有核查日志
        LogQuery shortLogQuery = LogQuery.create(baseReqDto, shortOrders, STATUS_LIST, queryTime);
        List<OrderExcuteLog> shortCheckedLogs = iOrderExcuteLogService.findOperLog(shortLogQuery);
        // 处理返回数据
        List<BaseOrderDto> shortResOrders = handleOrderService.handleOrder(shortOrders, shortCheckedLogs, Constant.EXCUTE_TYPE_ORDER,queryTime);
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

    public void excute(List<ExcuteReq> oralExcuteReqs, UserResDto currentUser, LocalDateTime now,String type) {
        Set<String> labels = getLiquidLabels();

        oralExcuteReqs.forEach(oralExcuteReq -> {
            OrderExcuteLog existLog = getExcuteLog(oralExcuteReq,Constant.EXCUTE_TYPE_ORDER);
            if(ObjectUtil.isNotNull(existLog) && ExcuteStatusEnum.COMPLETED.code().equals(existLog.getExcuteStatus())){
                throw new BusinessException("当前订单："+existLog.getOrderNo()+"今日执行已完成!");
            }
            List<OrderExcuteLog> orderCheckedLog = getOrderCheckLog(oralExcuteReq,Constant.EXCUTE_TYPE_DRUG);
            if(CollectionUtil.isEmpty(orderCheckedLog)){
                throw new BusinessException("当前医嘱没有核查,请先核查医嘱单!");
            }
            // 校验配液类的是否核对
            if(labels.contains(oralExcuteReq.getAdministration())){
                List<OrderExcuteLog> orderCheckLog = getOrderCheckLog(oralExcuteReq,Constant.EXCUTE_TYPE_LIQUID);
                if(CollectionUtil.isEmpty(orderCheckLog)){
                    throw new BusinessException("当前医嘱没有核对,请先核对医嘱单!");
                }
            }
            if(ObjectUtil.isNotNull(existLog)){
                existLog.setExcuteUserCode(currentUser.getUserName());
                existLog.setExcuteUserName(currentUser.getName());
                existLog.setExcuteStatus(oralExcuteReq.getExcuteStatus());
                existLog.setExcuteTime(now);
                if("5".equals(oralExcuteReq.getExcuteStatus())){

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
                orderExcuteLog.setCheckStatus("1");
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
        ExcuteStrategy excuteStrategy = null;
        Set<String> labels = null;
        if("0".equals(drugType)){  // 为0就是全部
            // 用法
            List<String> hisTypeCodes = ModuleTypeEnum.getHisTypeCodes();
            labels = iOrderTypeDictService.findLabelsByType(hisTypeCodes);
            // 统计
            excuteStrategy = new AllStrategy(mobileCommonService,labels);
        }else if(ModuleTypeEnum.TYPE9.code().equals(drugType)){ // 1009其他
            excuteStrategy = new OtherStrategy(mobileCommonService,labels);
        }else{
            labels = iOrderTypeDictService.findLabelsByType(Arrays.asList(drugType));
            excuteStrategy = new SingleStrategy(mobileCommonService,labels);
        }
        excuteStrategy.count(result,queryTime,patientId,visitId);
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
        // 结果
        List<BaseOrderDto> result = new ArrayList<>();
        // 查询时间
        Date queryTime = PdaTimeUtil.getTodayOrTomorrow();
        ExcuteStrategy excuteStrategy = null;
        Set<String> labels = null;
        if("0".equals(drugType)){  // 为0就是全部
            // 用法
            List<String> hisTypeCodes = ModuleTypeEnum.getHisTypeCodes();
            labels = iOrderTypeDictService.findLabelsByType(hisTypeCodes);
            // 统计
            excuteStrategy = new AllStrategy(mobileCommonService,labels);
        }else if(ModuleTypeEnum.TYPE9.code().equals(drugType)){ // 1009其他
            excuteStrategy = new OtherStrategy(mobileCommonService,labels);
        }else{
            labels = iOrderTypeDictService.findLabelsByType(Arrays.asList(drugType));
            excuteStrategy = new SingleStrategy(mobileCommonService,labels);
        }
        excuteStrategy.list(result,queryTime,patientId,visitId);
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

    @Override
    public void refresh() {
        excuteLogJob.excute();
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
