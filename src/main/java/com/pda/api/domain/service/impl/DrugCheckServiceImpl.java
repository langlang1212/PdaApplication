package com.pda.api.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.mapper.OrderExcuteLogMapper;
import com.pda.api.domain.mapper.OrdersMMapper;
import com.pda.api.domain.service.DrugCheckService;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.pda.api.dto.DrugDispensingCountResDto;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.api.service.DrugService;
import com.pda.common.Constant;
import com.pda.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private static final Integer CHANG = 1;

    private static final Integer LINSHI = 0;
    @Autowired
    private OrdersMMapper ordersMMapper;
    @Autowired
    private OrderExcuteLogMapper orderExcuteLogMapper;

    @Override
    public DrugDispensingCountResDto drugDispensionCount(DrugDispensionReqDto dto) {
        // 1、结果
        DrugDispensingCountResDto result = new DrugDispensingCountResDto();
        // 2、查询出所有医嘱 今天 or  明天
        Date startTime,endTime;
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        //Date now = new Date();
        if(Constant.TODAY.equals(dto.getTodayOrTomorrow())){
            Date now = getTestTime();
            startTime = DateUtil.getStartDateOfDay(now);
            endTime = DateUtil.getEndDateOfDay(now);
        }else{
            Date now = getTomorrowTestTime();
            startTime = DateUtil.getStartDateOfTomorrow(now);
            endTime = DateUtil.getEndDateOfTomorrow(now);
        }
        List<OrdersM> orders = ordersMMapper.listByPatientId(dto.getPatientId(),startTime,endTime);
        if(CollectionUtil.isNotEmpty(orders)){
            List<Integer> orderNos = orders.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            // 3、查出已经核查过该病人的医嘱
            List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectCheckedExcuteLog(dto.getPatientId(),orderNos,Constant.EXCUTE_TYPE_DRUG);
            if(CollectionUtil.isNotEmpty(orderExcuteLogs)){
                orders.forEach(order -> {
                    if(CHANG == order.getRepeatIndicator()){
                        result.setTotalBottles(result.getTotalBottles() + 1);
                    }else if(LINSHI == order.getRepeatIndicator()){
                        result.setTempTotalBottles(result.getTempTotalBottles() + 1);
                    }

                    orderExcuteLogs.forEach(orderExcuteLog -> {
                        if(order.getOrderNo() == orderExcuteLog.getOrderNo() && order.getOrderSubNo() == orderExcuteLog.getOrderSubNo()){
                            if(CHANG == order.getRepeatIndicator()){
                                result.setCheckedBottles(result.getCheckedBottles() + 1);
                            }else if(LINSHI == order.getRepeatIndicator()){
                                result.setTempCheckedBottles(result.getTempCheckedBottles() + 1);
                            }
                        }
                    });
                });
            }else{
                result.setCheckedBottles(0);
                result.setTempCheckedBottles(0);
                orders.forEach(order -> {
                    if(CHANG == order.getRepeatIndicator()){
                        result.setTotalBottles(result.getTotalBottles() + 1);
                    }else if(LINSHI == order.getRepeatIndicator()){
                        result.setTempTotalBottles(result.getTempTotalBottles() + 1);
                    }
                });
            }
        }
        result.setSurplusBottles(result.getTotalBottles() - result.getCheckedBottles());
        result.setTempSurplusBottles(result.getTempTotalBottles() - result.getTempCheckedBottles());
        return result;
    }


    private Date getTestTime(){
        String str = "2022-07-28 12:32:54";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date getTomorrowTestTime(){
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
