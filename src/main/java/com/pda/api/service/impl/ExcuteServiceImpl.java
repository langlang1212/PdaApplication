package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
import com.pda.api.service.PatientService;
import com.pda.common.Constant;
import com.pda.common.ExcuteStatusEnum;
import com.pda.common.PdaBaseService;
import com.pda.common.redis.service.RedisService;
import com.pda.exception.BusinessException;
import com.pda.job.ExcuteLogJob;
import com.pda.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname ExcuteServiceImpl
 * @Description TODO
 * @Date 2022-08-06 19:54
 * @Created by AlanZhang
 */
@Service
@Slf4j
public class ExcuteServiceImpl extends PdaBaseService implements ExcuteService {

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
    @Autowired
    private RedisService redisService;

    @Autowired
    private PatientService patientService;

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
        if (CollectionUtil.isNotEmpty(longResOrders)) {
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
        //
        excute(oralExcuteReqs,Constant.EXCUTE_TYPE_ORDER);

    }

    public void excute(List<ExcuteReq> oralExcuteReqs,String type) {
        // 配液的类型
        Set<String> labels = getLiquidLabels();
        // 皮试医嘱用法类型
        Set<String> skinLabels = getSkinLabels();

        oralExcuteReqs.forEach(oralExcuteReq -> {
            doExcute(type, labels, skinLabels, oralExcuteReq);
        });
    }

    private void doExcute(String type, Set<String> labels, Set<String> skinLabels, ExcuteReq oralExcuteReq) {
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        // 当前登录人
        // 登陆人
        UserResDto currentUser = SecurityUtil.getCurrentUser();
        // 1、从redis 查询是否有 patientId+visitId+orderNo的key，如果有，通知用户目前订单正在执行，如果没有则继续执行
        String key = String.format("%s%s%s%s", "excute-", oralExcuteReq.getPatientId(), oralExcuteReq.getVisitId(), oralExcuteReq.getOrderNo());
        try {
            if (redisService.hasKey(key)) {
                log.info("医嘱单,patientId:{},visitId:{},orderNo:{}正在执行......", oralExcuteReq.getPatientId(), oralExcuteReq.getVisitId(), oralExcuteReq.getOrderNo());
                throw new BusinessException("当前医嘱单正在执行,请稍后再执行下一步!");
            } else {
                redisService.setCacheObject(key, 1);
            }

            Integer freqCount = getCompleteExcuteLogCount(oralExcuteReq, type);
            log.info("=============执行完成次数:{}，执行频次：{}============", freqCount, oralExcuteReq.getFrequencyCount());
            if (ObjectUtil.isNull(oralExcuteReq.getFrequencyCount())) {
                // 如果频次是空的话，有一条执行完成日志，就代表这个医嘱已经执行完成了
                if (freqCount == 1) {
                    throw new BusinessException("当前订单：" + oralExcuteReq.getOrderNo() + "今日执行已完成!");
                }
            } else {
                if (oralExcuteReq.getFrequencyCount().intValue() == freqCount.intValue()) {
                    throw new BusinessException("当前订单：" + oralExcuteReq.getOrderNo() + "今日执行已完成!");
                }
            }
            /*List<OrderExcuteLog> orderCheckedLog = getOrderCheckLog(oralExcuteReq,Constant.EXCUTE_TYPE_DRUG);
            if(CollectionUtil.isEmpty(orderCheckedLog)){
                throw new BusinessException("当前医嘱没有核查,请先核查医嘱单!");
            }*/
            // 校验配液类的是否核对
            if (labels.contains(oralExcuteReq.getAdministration())) {
                List<OrderExcuteLog> orderCheckLog = getOrderCheckLog(oralExcuteReq, Constant.EXCUTE_TYPE_LIQUID);
                if (CollectionUtil.isEmpty(orderCheckLog)) {
                    throw new BusinessException("当前医嘱没有配液核对,请先核对医嘱单!");
                }
            }
            OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
            orderExcuteLog.setPatientId(oralExcuteReq.getPatientId());
            orderExcuteLog.setVisitId(oralExcuteReq.getVisitId());
            orderExcuteLog.setOrderNo(oralExcuteReq.getOrderNo());
            orderExcuteLog.setExcuteDate(now.toLocalDate());
            orderExcuteLog.setExcuteUserCode(currentUser.getUserName());
            orderExcuteLog.setExcuteUserName(currentUser.getName());
            orderExcuteLog.setExcuteStatus(oralExcuteReq.getExcuteStatus());
            orderExcuteLog.setCheckStatus("1");
            orderExcuteLog.setExcuteTime(now);
            orderExcuteLog.setCheckTime(now);
            orderExcuteLog.setType(type);

            if ("5".equals(oralExcuteReq.getExcuteStatus()) && skinLabels.contains(oralExcuteReq.getAdministration())) {
                orderExcuteLog.setRemark(oralExcuteReq.getResult());
                String typeCode = reverseWriteSkin(currentUser, oralExcuteReq);
                log.info("皮试医嘱结果 typeCode:{}", typeCode);
                if (StringUtil.isNotEmpty(typeCode) && "1".equals(typeCode)) {
                    // 插入
                    orderExcuteLogMapper.insert(orderExcuteLog);
                } else {
                    log.error("患者:{},vistiId:{},皮试医嘱{},反写his失败!", orderExcuteLog.getPatientId(), orderExcuteLog.getVisitId(), orderExcuteLog.getOrderNo());
                    throw new BusinessException("皮试医嘱反写失败!");
                }
            } else if ("5".equals(oralExcuteReq.getExcuteStatus())||"3".equals(oralExcuteReq.getExcuteStatus())) {
                orderExcuteLog.setRemark(oralExcuteReq.getResult());
                String typeCode = reverseWriteInstructions(currentUser, oralExcuteReq);
                log.info("医嘱执行反写结果 typeCode:{}", typeCode);
                if (StringUtil.isNotEmpty(typeCode) && "AA".equals(typeCode)) {
                    // 插入
                    orderExcuteLogMapper.insert(orderExcuteLog);
                } else {
                    log.error("患者:{},vistiId:{},医嘱执行{},反写his失败!", orderExcuteLog.getPatientId(), orderExcuteLog.getVisitId(), orderExcuteLog.getOrderNo());
                    throw new BusinessException("医嘱执行反写失败!");
                }
            } else {
                // 插入
                orderExcuteLogMapper.insert(orderExcuteLog);
            }


        } catch (Exception e) {
            log.error("医嘱执行失败:{}", e.getMessage());
            throw new BusinessException("医嘱执行失败:" + e.getMessage());
        } finally {
            // 一定要删除key,不然锁死医嘱单
            redisService.deleteObject(key);
        }

    }

    /**
     * 皮试医嘱反写
     * @param oralExcuteReq
     */
    private String reverseWriteSkin(UserResDto currentUser,ExcuteReq oralExcuteReq) {
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>TJ641</msgType>\n" +
                "\t\t<msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "\t\t<createTime>"+ PdaTimeUtil.getCreateTime(new Date()) +"</createTime>\n" +
                "\t\t<sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "\t\t<targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "\t\t<sysPassword/>\n" +
                "\t</AuthHeader>\n" +
                "\t<ControlActProcess>\n" +
                "\t\t<ListInfo>\n" +
                "\t\t\t<List>\n" +
                "\t\t\t\t<PatientNo>"+String.format("%s%s",oralExcuteReq.getPatientId(),oralExcuteReq.getVisitId())+"</PatientNo>\n" +
                "\t\t\t\t<GroupNo>"+String.format("%s%s%s%s","YZ",oralExcuteReq.getPatientId(),oralExcuteReq.getVisitId(),oralExcuteReq.getOrderNo())+"</GroupNo>\n" +
                "\t\t\t\t<test_result>"+oralExcuteReq.getResult()+"</test_result>\n" +
                "\t\t\t\t<perform_date>"+PdaTimeUtil.getLongTime(new Date())+"</perform_date>\n" +
                "\t\t\t\t<perform_user>"+currentUser.getUserName()+"</perform_user>\n" +
                "\t\t\t</List>\n" +
                "\t\t</ListInfo>\n" +
                "\t</ControlActProcess>\n" +
                "</root>\n";
        String typeCode = "";
        try {
            log.info("皮试反写入参:{}",param);
            String result = CxfClient.excute(getWsProperties().getReverseUrl(), getWsProperties().getMethodName(), param);
            log.info("皮试医嘱反写结果:{}",result);
            if(StringUtil.isNotEmpty(result)){
                Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(result);
                typeCode = new JSONObject(stringObjectMap).getJSONObject("ControlActProcess").getJSONObject("Response").getString("TypeCode");
            }else{
                throw new BusinessException("皮试医嘱反写结果为空!");
            }
        }catch (Exception e){
            throw new BusinessException("皮试医嘱反写失败!",e);
        }
        return typeCode;
    }

    /**
     * 反写医嘱执行记录
     * @param currentUser
     * @param oralExcuteReq
     * @return
     */
    private String reverseWriteInstructions(UserResDto currentUser,ExcuteReq oralExcuteReq) {

        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "\t<AuthHeader>\n" +
                "\t\t<msgType>TJ615</msgType>\n" +
                "\t\t<msgId>49219b98-c3c6-11ee-b5e7-525400b396fe</msgId>\n" +
                "\t\t<createTime>" + PdaTimeUtil.getCreateTime(new Date()) + "</createTime>\n" +
                "\t\t<sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "\t\t<targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "\t\t<sysPassword/>\n" +
                "\t</AuthHeader>\n" +
                "\t<ControlActProcess>\n" +
                "\t\t<ListInfo>\n" +
                "\t\t\t<List>\n" +
                "\t\t\t\t<PatientNo>" + String.format("%s%s", oralExcuteReq.getPatientId(), oralExcuteReq.getVisitId()) + "</PatientNo>\n" +
                "\t\t\t\t<InHospNo>" + String.format("%s", oralExcuteReq.getVisitId()) + "</InHospNo>\n" +
                "\t\t\t\t<GroupNo>" + String.format("%s%s%s%s", "YZ", oralExcuteReq.getPatientId(), oralExcuteReq.getVisitId(), oralExcuteReq.getOrderNo()) + "</GroupNo>\n" +
                "\t\t\t\t<PerformNo>" + UUID.randomUUID().toString() + "</PerformNo>\n"
                + (oralExcuteReq.getExcuteStatus().equals("3") ? "\t\t\t\t<ExecTime>" + PdaTimeUtil.getLongTime(new Date()) + "</ExecTime>\n"
                : "\t\t\t\t<ExecEndTime>" + PdaTimeUtil.getLongTime(new Date()) + "</ExecEndTime>\n")
                + "\t\t\t\t<ExecUserCode>" + currentUser.getUserName() + "</ExecUserCode>\n" +
                "\t\t\t\t<ExecUserName>" + currentUser.getName() + "</ExecUserName>\n" +
                "\t\t\t\t<UpTime>" + PdaTimeUtil.getLongTime(new Date()) + "</UpTime>\n" +
                "\t\t\t</List>\n" +
                "\t\t</ListInfo>\n" +
                "\t</ControlActProcess>\n" +
                "</root>\n";
        String typeCode = "";
        try {
            log.info("医嘱执行反写入参:{}", param);
            String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
            log.info("医嘱执行反写结果:{}", result);
            if (StringUtil.isNotEmpty(result)) {
                Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(result);
                typeCode = new JSONObject(stringObjectMap).getJSONObject("ControlActProcess").getJSONObject("Response").getString("TypeCode");
            } else {
                throw new BusinessException("医嘱执行反写结果为空!");
            }
        } catch (Exception e) {
            throw new BusinessException("医嘱执行反写失败!", e);
        }
        return typeCode;
    }

    /**
     * 查询医嘱执行记录
     * @param excuteReq
     * @param type
     * @return
     */
    private Integer getCompleteExcuteLogCount(ExcuteReq excuteReq,String type) {
        LambdaQueryWrapper<OrderExcuteLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logLambdaQueryWrapper.eq(OrderExcuteLog::getPatientId,excuteReq.getPatientId())
                .eq(OrderExcuteLog::getVisitId,excuteReq.getVisitId())
                .eq(OrderExcuteLog::getOrderNo,excuteReq.getOrderNo())
                .eq(OrderExcuteLog::getType,type)
                .eq(OrderExcuteLog::getExcuteDate,excuteReq.getExcuteDate())
                .eq(OrderExcuteLog::getExcuteStatus, ExcuteStatusEnum.COMPLETED.code());
        return orderExcuteLogMapper.selectCount(logLambdaQueryWrapper);
    }

    private List<OrderExcuteLog> getOrderCheckLog(ExcuteReq excuteReq,String type) {
        LambdaQueryWrapper<OrderExcuteLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logLambdaQueryWrapper.eq(OrderExcuteLog::getPatientId,excuteReq.getPatientId()).eq(OrderExcuteLog::getVisitId,excuteReq.getVisitId())
                .eq(OrderExcuteLog::getOrderNo,excuteReq.getOrderNo())
                .eq(OrderExcuteLog::getType,type).eq(OrderExcuteLog::getCheckStatus,"1").eq(OrderExcuteLog::getExcuteDate,excuteReq.getExcuteDate());
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
        excute(skinExcuteReqs,Constant.EXCUTE_TYPE_ORDER);
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
        ExcuteStrategy excuteStrategy = getExcuteStrategy(drugType);
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
        Long startTime = System.currentTimeMillis();
        ExcuteStrategy excuteStrategy = getExcuteStrategy(drugType);
        Long endTime = System.currentTimeMillis();
        log.info("查询执行策略和用法用时:{}",endTime - startTime);
        excuteStrategy.list(result,patientId,visitId);
        return result;
    }

    private ExcuteStrategy getExcuteStrategy(String drugType) {
        ExcuteStrategy excuteStrategy = null;
        Set<String> labels = null;
        if ("0".equals(drugType)) {  // 为0就是全部
            // 用法
            List<String> hisTypeCodes = ModuleTypeEnum.getHisTypeCodes();
            labels = iOrderTypeDictService.findLabelsByType(hisTypeCodes);
            // 统计
            excuteStrategy = new AllStrategy(mobileCommonService, labels);
        } else if (ModuleTypeEnum.TYPE9.code().equals(drugType)) { // 1009其他
            excuteStrategy = new OtherStrategy(mobileCommonService, labels);
        } else {
            labels = iOrderTypeDictService.findLabelsByType(Arrays.asList(drugType));
            excuteStrategy = new SingleStrategy(mobileCommonService, labels);
        }
        return excuteStrategy;
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "ds2TransactionManager")
    @Override
    public void excute(List<ExcuteReq> excuteReqs) {
        if(CollectionUtil.isEmpty(excuteReqs)){
            throw new BusinessException("执行医嘱不能为空!");
        }
        excute(excuteReqs,Constant.EXCUTE_TYPE_ORDER);
    }

    @Override
    public void refresh() {
        excuteLogJob.excute();
    }

    private Set<String> getLiquidLabels() {
        List<String> types = new ArrayList<>();
        types.add(ModuleTypeEnum.TYPE3.code());
        types.add(ModuleTypeEnum.TYPE4.code());
        types.add(ModuleTypeEnum.TYPE5.code());
        types.add(ModuleTypeEnum.TYPE6.code());
        return iOrderTypeDictService.findLabelsByType(types);
    }

    private Set<String> getSkinLabels() {
        List<String> types = new ArrayList<>();
        types.add(ModuleTypeEnum.TYPE7.code());
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
