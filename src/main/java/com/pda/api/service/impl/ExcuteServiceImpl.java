package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.ExcuteReq;
import com.pda.api.dto.OralResDto;
import com.pda.api.dto.SkinResDto;
import com.pda.api.dto.UserResDto;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.mapper.slave.OrderExcuteLogMapper;
import com.pda.api.service.ExcuteService;
import com.pda.common.Constant;
import com.pda.exception.BusinessException;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import com.pda.utils.SecurityUtil;
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

    @Override
    public List<OralResDto> oralList(String patientId) {
        List<OralResDto> result = new ArrayList<>();
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        //Date today = new Date();
        Date today = getTestTime();
        Date queryTime = DateUtil.getStartDateOfDay(today);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        List<OrdersM> shortOrders = ordersMMapper.listShortOralByPatientId(patientId,startDateOfDay,endDateOfDay);
        addOral(result, queryTime, shortOrders,patientId,Constant.EXCUTE_TYPE_ORAL);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOralByPatientId(patientId, queryTime);
        addOral(result,queryTime,longOrders,patientId,Constant.EXCUTE_TYPE_ORAL);

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

        addExcuteLog(oralExcuteReqs, currentUser, now,Constant.EXCUTE_TYPE_ORAL);

    }

    private void addExcuteLog(List<ExcuteReq> oralExcuteReqs, UserResDto currentUser, LocalDateTime now,String type) {
        oralExcuteReqs.forEach(oralExcuteReq -> {
            OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
            orderExcuteLog.setPatientId(oralExcuteReq.getPatientId());
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
        });
    }

    @Override
    public List<SkinResDto> skinList(String patientId) {
        List<SkinResDto> result = new ArrayList<>();
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        //Date today = new Date();
        Date today = getTestTime();
        Date queryTime = DateUtil.getStartDateOfDay(today);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        List<OrdersM> shortOrders = ordersMMapper.listShortOrderByPatientId(patientId,startDateOfDay,endDateOfDay,Constant.EXCUTE_TYPE_SKIN);
        addSkin(result, queryTime, shortOrders,patientId,Constant.EXCUTE_TYPE_SKIN);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOrderByPatientId(patientId, queryTime,Constant.EXCUTE_TYPE_SKIN);
        addSkin(result,queryTime,longOrders,patientId,Constant.EXCUTE_TYPE_SKIN);

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

        addExcuteLog(skinExcuteReqs, currentUser, now,Constant.EXCUTE_TYPE_SKIN);
    }

    private void addSkin(List<SkinResDto> result, Date queryTime, List<OrdersM> orders,String patientId,String type) {
        if(CollectionUtil.isNotEmpty(orders)){
            List<Integer> orderNos = orders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectCheckedExcuteLog(patientId,orderNos, type);
            orders.forEach(ordersM -> {
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
            });
        }
    }

    private void addOral(List<OralResDto> result, Date queryTime, List<OrdersM> shortOrders,String patientId,String type) {
        if(CollectionUtil.isNotEmpty(shortOrders)){
            List<Integer> orderNos = shortOrders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectCheckedExcuteLog(patientId,orderNos, type);
            shortOrders.forEach(ordersM -> {
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
}
