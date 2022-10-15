package com.pda.job;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.entity.UserInfo;
import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.dto.OrdersN;
import com.pda.api.dto.UserResDto;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.common.Constant;
import com.pda.common.ExcuteStatusEnum;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname ExcuteLogJob
 * @Description TODO
 * @Date 2022-10-15 16:53
 * @Created by AlanZhang
 */
@Component
@Slf4j
public class ExcuteLogJob {

    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private IOrderTypeDictService iOrderTypeDictService;
    @Autowired
    private IOrderExcuteLogService iOrderExcuteLogService;

    //添加定时任务
    @Scheduled(cron = "* * 0-3 * * ? ")
    //@PostConstruct
    @Transactional(rollbackFor = Exception.class,transactionManager = "ds2TransactionManager")
    public void excute() {
        log.info("刷新不用校对医嘱状态!");
        LocalDateTime now = LocalDateTime.now();
        LocalDate dateNow = LocalDate.now();
        List<OrderExcuteLog> logs = new ArrayList<>();
        List<String> types = new ArrayList<>();
        types.add(ModuleTypeEnum.TYPE3.code());
        types.add(ModuleTypeEnum.TYPE4.code());
        types.add(ModuleTypeEnum.TYPE5.code());
        types.add(ModuleTypeEnum.TYPE6.code());
        Set<String> labels = iOrderTypeDictService.findLabelsByType(types);
        List<OrderExcuteLog> existLogs = iOrderExcuteLogService.getAllExcuteLog(DateUtil.getShortDate(new Date()));
        Map<String, List<OrderExcuteLog>> group = existLogs.stream().collect(Collectors.groupingBy(log -> {
            return log.getPatientId() + "_" + log.getVisitId() + "_" + log.getOrderNo();
        }));
        // 1、拿到所有不用校对的医嘱
        List<OrdersM> ordersMs = ordersMMapper.selectAllTodoOrders(labels);
        if(CollectionUtil.isNotEmpty(ordersMs)){
            addTodoLog(now, dateNow, logs, ordersMs,group);
        }
        List<OrdersN> ordersNs = ordersMMapper.selectAllTodoOrderN(labels);
        if(CollectionUtil.isNotEmpty(ordersNs)){
            addTodoLog(now, dateNow, logs, ordersNs,group);
        }

        if(CollectionUtil.isNotEmpty(logs)){
            iOrderExcuteLogService.saveBatch(logs);
        }
        log.info("刷新不用校对医嘱状态成功!");
    }

    private void addTodoLog(LocalDateTime now, LocalDate dateNow, List<OrderExcuteLog> logs, List<? extends OrdersM> ordersMs,Map<String, List<OrderExcuteLog>> group) {
        ordersMs.forEach(o ->{
            String key = o.getPatientId() + "_" + o.getVisitId() + "_"+o.getOrderNo();
            if(!group.containsKey(key)){
                log.info("刷新不用校对医嘱:"+key);
                OrderExcuteLog orderExcuteLog = new OrderExcuteLog();
                orderExcuteLog.setPatientId(o.getPatientId());
                orderExcuteLog.setVisitId(o.getVisitId());
                orderExcuteLog.setOrderNo(o.getOrderNo());
                orderExcuteLog.setExcuteDate(dateNow);
                orderExcuteLog.setExcuteUserCode("system");
                orderExcuteLog.setExcuteUserName("system");
                orderExcuteLog.setExcuteStatus(ExcuteStatusEnum.NO_EXCUTE.code());
                orderExcuteLog.setCheckStatus("1");
                orderExcuteLog.setCheckTime(now);
                orderExcuteLog.setType(Constant.EXCUTE_TYPE_ORDER);

                logs.add(orderExcuteLog);
            }
        });
    }
}
