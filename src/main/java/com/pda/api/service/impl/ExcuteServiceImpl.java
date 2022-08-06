package com.pda.api.service.impl;

import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.OralResDto;
import com.pda.api.mapper.primary.OrdersMMapper;
import com.pda.api.service.ExcuteService;
import com.pda.common.Constant;
import com.pda.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<OralResDto> oralList(String patientId) {
        // TODO: 2022-08-03 联调通过 取消这行注释，删除下面的now 赋值
        //Date today = new Date();
        Date today = getTestTime();
        Date queryTime = DateUtil.getStartDateOfDay(today);
        // 临时
        Date startDateOfDay = DateUtil.getStartDateOfDay(today);
        Date endDateOfDay = DateUtil.getEndDateOfDay(today);
        List<OrdersM> shortOrders = ordersMMapper.listShortOralByPatientId(patientId,startDateOfDay,endDateOfDay);
        // 长期
        List<OrdersM> longOrders = ordersMMapper.listLongOralByPatientId(patientId, queryTime);
        return null;
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
