package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pda.api.domain.constant.DomainConstant;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.service.IOrderLabelParamService;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.dto.*;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.mapper.slave.OrderExcuteLogMapper;
import com.pda.api.service.ExcuteService;
import com.pda.common.Constant;
import com.pda.common.ExcuteStatusEnum;
import com.pda.exception.BusinessException;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
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

    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private OrderExcuteLogMapper orderExcuteLogMapper;
    @Autowired
    private IOrderLabelParamService iOrderLabelParamService;
    @Autowired
    private IOrderTypeDictService iOrderTypeDictService;

    @Override
    public List<OralResDto> oralList(String patientId,Integer visitId) {
        List<OralResDto> result = new ArrayList<>();
        Date today = new Date();
        Date queryTime = DateUtil.getStartDateOfDay(today);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        // 口服
        List<String> types = new ArrayList<>();
        types.add(ModuleTypeEnum.TYPE2.code());
        Set<String> labels = iOrderTypeDictService.findLabelsByType(types);

        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId,visitId,startDateOfDay,endDateOfDay);
        addOral(result,visitId, queryTime, shortOrders,patientId,Constant.EXCUTE_TYPE_ORDER,labels);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId,visitId,queryTime);
        addOral(result,visitId,queryTime,longOrders,patientId,Constant.EXCUTE_TYPE_ORDER,labels);

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
        addExcuteLog(oralExcuteReqs, currentUser, now,Constant.EXCUTE_TYPE_ORDER);

    }

    private void addExcuteLog(List<ExcuteReq> oralExcuteReqs, UserResDto currentUser, LocalDateTime now,String type) {
        oralExcuteReqs.forEach(oralExcuteReq -> {
            OrderExcuteLog existLog = getExcuteLog(oralExcuteReq,Constant.EXCUTE_TYPE_ORDER,oralExcuteReq.getVisitId());
            if(ObjectUtil.isNotNull(existLog) && ExcuteStatusEnum.COMPLETED.code().equals(existLog.getExcuteStatus())){
                throw new BusinessException("当前订单："+existLog.getOrderNo()+"今日执行已完成!");
            }
            if(ObjectUtil.isNotNull(existLog)){
                existLog.setExcuteUserCode(currentUser.getUserName());
                existLog.setExcuteUserName(currentUser.getName());
                existLog.setExcuteStatus(oralExcuteReq.getExcuteStatus());
                existLog.setExcuteTime(now);
                if("5".equals(oralExcuteReq.getExcuteStatus())){
                    // TODO: 2022-08-07 反写his
                }
                orderExcuteLogMapper.updateLog(existLog);
            }else{
                OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
                orderExcuteLog.setPatientId(oralExcuteReq.getPatientId());
                orderExcuteLog.setVisitId(oralExcuteReq.getVisitId());
                orderExcuteLog.setOrderNo(oralExcuteReq.getOrderNo());
                orderExcuteLog.setOrderSubNo(oralExcuteReq.getOrderSubNo());
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

    private OrderExcuteLog getExcuteLog(ExcuteReq excuteReq,String type,Integer visitId) {
        LambdaQueryWrapper<OrderExcuteLog> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logLambdaQueryWrapper.eq(OrderExcuteLog::getPatientId,excuteReq.getPatientId()).eq(OrderExcuteLog::getVisitId,visitId)
                .eq(OrderExcuteLog::getOrderNo,excuteReq.getOrderNo())
                .eq(OrderExcuteLog::getOrderSubNo,excuteReq.getOrderSubNo()).eq(OrderExcuteLog::getType,type).eq(OrderExcuteLog::getExcuteDate,excuteReq.getExcuteDate());
        OrderExcuteLog orderExcuteLog = orderExcuteLogMapper.selectOne(logLambdaQueryWrapper);
        return orderExcuteLog;
    }

    @Override
    public List<SkinResDto> skinList(String patientId,Integer visitId) {
        List<SkinResDto> result = new ArrayList<>();
        Date today = new Date();
        Date queryTime = DateUtil.getStartDateOfDay(today);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        // 皮试用法
        Set<String> labels = DomainConstant.skinSet;

        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId,visitId,startDateOfDay,endDateOfDay);
        addSkin(result,visitId, queryTime, shortOrders,patientId,Constant.EXCUTE_TYPE_ORDER,labels);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId,visitId, queryTime);
        addSkin(result,visitId,queryTime,longOrders,patientId,Constant.EXCUTE_TYPE_ORDER,labels);

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

        addExcuteLog(skinExcuteReqs, currentUser, now,Constant.EXCUTE_TYPE_ORDER);
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
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        Date today = new Date();
        Date queryTime = DateUtil.getStartDateOfDay(today);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);

        List<String> types = new ArrayList<>();
        if(!StringUtils.isBlank(drugType)){  // 为空就是全部
            types = ModuleTypeEnum.getAllCodes();
        }else{
            types.add(drugType);
        }
        Set<String> labels = iOrderTypeDictService.findLabelsByType(types);

        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId,visitId,startDateOfDay,endDateOfDay);
        handleOrder(patientId,visitId,result,shortOrders,0,Constant.EXCUTE_TYPE_ORDER,DateUtil.getShortDate(today),labels);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId,visitId, queryTime);
        handleOrder(patientId,visitId,result,longOrders,1,Constant.EXCUTE_TYPE_ORDER,DateUtil.getShortDate(today),labels);
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
    public List<OrderResDto> orderExcuteList(String patientId,Integer visitId,String drugType) {
        List<OrderResDto> result = new ArrayList<>();

        Date today = new Date();
        Date queryTime = DateUtil.getStartDateOfDay(today);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);

        List<String> types = new ArrayList<>();
        if(StringUtils.isBlank(drugType)){
            types = ModuleTypeEnum.getAllCodes();
        }else{
            types.add(drugType);
        }
        Set<String> labels = iOrderTypeDictService.findLabelsByType(types);

        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId,visitId,startDateOfDay,endDateOfDay);
        addOrder(result,visitId,queryTime,shortOrders,patientId,Constant.EXCUTE_TYPE_ORDER,labels);
        // 处理短期医嘱
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId,visitId, queryTime);
        addOrder(result,visitId,queryTime,longOrders,patientId,Constant.EXCUTE_TYPE_ORDER,labels);
        // 处理长期医嘱
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

        addExcuteLog(excuteReqs, currentUser, now,Constant.EXCUTE_TYPE_ORDER);
    }

    private void addOrder(List<OrderResDto> result,Integer visitId, Date queryTime, List<OrdersM> orders,String patientId,String type,Set<String> labels) {
        if(CollectionUtil.isNotEmpty(orders)){
            List<Integer> orderNos = orders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectExcuteLog(patientId,visitId,orderNos, type,DateUtil.getShortDate(queryTime));
            orders.forEach(ordersM -> {
                if(labels.contains(ordersM.getAdministration())){
                    OrderResDto orderResDto = new OrderResDto();
                    orderResDto.setPatientId(ordersM.getPatientId());
                    orderResDto.setVisitId(ordersM.getVisitId());
                    orderResDto.setOrderNo(ordersM.getOrderNo());
                    orderResDto.setOrderSubNo(ordersM.getOrderSubNo());
                    orderResDto.setOrderText(ordersM.getOrderText());
                    orderResDto.setRepeatIndicator(ordersM.getRepeatIndicator());
                    orderResDto.setDosAge(String.format("%s%s",ordersM.getDosage(),ordersM.getDosageUnits()));
                    orderResDto.setFrequency(String.format("%s/%s",ordersM.getFreqCounter(),ordersM.getFreqIntervalUnit()));
                    orderResDto.setAdministration(ordersM.getAdministration());
                    orderResDto.setStartDateTime(ordersM.getStartDateTime());
                    orderResDto.setStopDateTime(ordersM.getStopDateTime());
                    orderResDto.setExcuteDate(DateUtil.getShortDate(queryTime));
                    if(CollectionUtil.isNotEmpty(orderExcuteLogs)){
                        setExcuteStatus(orderResDto,orderExcuteLogs);
                    }
                    result.add(orderResDto);
                }
            });
        }
    }

    private void addSkin(List<SkinResDto> result,Integer visitId, Date queryTime, List<OrdersM> orders,String patientId,String type,Set<String> labels) {
        if(CollectionUtil.isNotEmpty(orders)){
            List<Integer> orderNos = orders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectExcuteLog(patientId,visitId,orderNos, type,DateUtil.getShortDate(queryTime));
            orders.forEach(ordersM -> {
                if(labels.contains(ordersM.getAdministration())){
                    SkinResDto skinResDto = new SkinResDto();
                    skinResDto.setPatientId(ordersM.getPatientId());
                    skinResDto.setOrderNo(ordersM.getOrderNo());
                    skinResDto.setOrderSubNo(ordersM.getOrderSubNo());
                    skinResDto.setOrderText(ordersM.getOrderText());
                    skinResDto.setRepeatIndicator(0);
                    skinResDto.setDosAge(String.format("%s%s",ordersM.getDosage(),ordersM.getDosageUnits()));
                    skinResDto.setFrequency(String.format("%s/%s",ordersM.getFreqCounter(),ordersM.getFreqIntervalUnit()));
                    skinResDto.setAdministration(ordersM.getAdministration());
                    skinResDto.setStartDateTime(ordersM.getStartDateTime());
                    skinResDto.setStopDateTime(ordersM.getStopDateTime());
                    skinResDto.setExcuteDate(DateUtil.getShortDate(queryTime));
                    if(CollectionUtil.isNotEmpty(orderExcuteLogs)){
                        setExcuteStatus(skinResDto,orderExcuteLogs);
                    }
                    result.add(skinResDto);
                }
            });
        }
    }

    private void addOral(List<OralResDto> result,Integer visitId ,Date queryTime, List<OrdersM> shortOrders,String patientId,String type,Set<String> labels) {
        if(CollectionUtil.isNotEmpty(shortOrders)){
            List<Integer> orderNos = shortOrders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectExcuteLog(patientId,visitId,orderNos, type,DateUtil.getShortDate(queryTime));
            shortOrders.forEach(ordersM -> {
                if(labels.contains(ordersM.getAdministration())){
                    OralResDto oralResDto = new OralResDto();
                    oralResDto.setPatientId(ordersM.getPatientId());
                    oralResDto.setOrderNo(ordersM.getOrderNo());
                    oralResDto.setOrderSubNo(ordersM.getOrderSubNo());
                    oralResDto.setOrderText(ordersM.getOrderText());
                    oralResDto.setRepeatIndicator(0);
                    oralResDto.setDosAge(String.format("%s%s",ordersM.getDosage(),ordersM.getDosageUnits()));
                    oralResDto.setFrequency(String.format("%s/%s",ordersM.getFreqCounter(),ordersM.getFreqIntervalUnit()));
                    oralResDto.setAdministration(ordersM.getAdministration());
                    oralResDto.setStartDateTime(ordersM.getStartDateTime());
                    oralResDto.setStopDateTime(ordersM.getStopDateTime());
                    oralResDto.setExcuteDate(DateUtil.getShortDate(queryTime));
                    if(CollectionUtil.isNotEmpty(orderExcuteLogs)){
                        setExcuteStatus(oralResDto,orderExcuteLogs);
                    }
                    result.add(oralResDto);
                }
            });
        }
    }

    private void setExcuteStatus(OralResDto oralResDto,List<OrderExcuteLog> orderExcuteLogs){
        for (OrderExcuteLog orderExcuteLog : orderExcuteLogs) {
            if(oralResDto.getPatientId().equals(orderExcuteLog.getPatientId()) && oralResDto.getOrderNo() == orderExcuteLog.getOrderNo() && oralResDto.getOrderSubNo() == orderExcuteLog.getOrderSubNo()){
                oralResDto.setExcuteStatus(orderExcuteLog.getExcuteStatus());
            }
        }
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

    private void handleOrder(String patientId,Integer visitId ,CheckCountResDto result, List<OrdersM> orders, Integer repeatRedicator,String type,String excuteDate,Set<String> labels) {
        if(CollectionUtil.isNotEmpty(orders)){
            // 总条数
            if(1 == repeatRedicator){
                result.setTotalBottles(orders.size());
            }else{
                result.setTempTotalBottles(orders.size());
            }
            // 已核查条数
            List<Integer> orderNos = orders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            // 查出已经核查过该病人的医嘱
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectExcuteLog(patientId,visitId,orderNos, type,excuteDate);
            if(CollectionUtil.isNotEmpty(orders)){
                orders.forEach(order -> {
                    if(labels.contains(order.getAdministration())){
                        if(CollectionUtil.isNotEmpty(orderExcuteLogs)){
                            orderExcuteLogs.forEach(orderExcuteLog -> {
                                if(order.getOrderNo() == orderExcuteLog.getOrderNo() && order.getOrderSubNo() == orderExcuteLog.getOrderSubNo() && ExcuteStatusEnum.COMPLETED.code().equals(orderExcuteLog.getExcuteStatus())){
                                    if(1 == repeatRedicator){
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
}
