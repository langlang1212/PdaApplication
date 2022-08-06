package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.OralResDto;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.mapper.slave.OrderExcuteLogMapper;
import com.pda.api.service.ExcuteService;
import com.pda.common.Constant;
import com.pda.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        addOral(result, queryTime, shortOrders);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOralByPatientId(patientId, queryTime);
        addOral(result,queryTime,longOrders);

        //
        Map<String,List<OrdersM>> resultMap = new HashMap<>();
        /*List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectCheckedExcuteLog(dto.getPatientId(),orderNos, type);*/
        return result;
    }

    private void addOral(List<OralResDto> result, Date queryTime, List<OrdersM> shortOrders) {
        if(CollectionUtil.isNotEmpty(shortOrders)){
            shortOrders.forEach(ordersM -> {
                OralResDto oralResDto = new OralResDto();
                oralResDto.setOrderText(ordersM.getOrderText());
                oralResDto.setRepeatIndicator(0);
                oralResDto.setDosAge(String.format("%s%s",ordersM.getDosage(),ordersM.getDosageUnits()));
                oralResDto.setFrequency(String.format("%s/%s",ordersM.getFreqCounter(),ordersM.getFreqIntervalUnit()));
                oralResDto.setAdministration(ordersM.getAdministration());
                oralResDto.setStartDateTime(ordersM.getStartDateTime());
                oralResDto.setStopDateTime(ordersM.getStopDateTime());
                oralResDto.setExcuteDate(DateUtil.getShortDate(queryTime));

                result.add(oralResDto);
            });
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
