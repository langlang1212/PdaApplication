package com.pda.api.dto.query;

import cn.hutool.core.collection.CollectionUtil;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.base.BaseReqDto;
import com.pda.utils.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname LogQuery
 * @Description TODO
 * @Date 2022-09-18 19:33
 * @Created by AlanZhang
 */
@Data
@Slf4j
public class LogQuery {

    private String patientId;

    private List<Integer> orderNos;

    private Integer visitId;

    private String excuteDate;

    private List<String> types;

    public static LogQuery create(BaseReqDto reqDto, List<OrdersM> ordersMS, List<String> types, Date queryTime){
        LogQuery logQuery = new LogQuery();
        logQuery.init(logQuery,reqDto,ordersMS,types,queryTime);
        return logQuery;
    }

    public void init(LogQuery logQuery,BaseReqDto reqDto,List<OrdersM> ordersMS,List<String> types, Date queryTime){
        BeanUtils.copyProperties(reqDto,logQuery);
        logQuery.setExcuteDate(DateUtil.getShortDate(queryTime));
        logQuery.setTypes(types);
        // orderNos
        if(CollectionUtil.isNotEmpty(ordersMS)){
            List<Integer> orderNos = ordersMS.stream().map(OrdersM::getOrderNo).distinct().collect(Collectors.toList());
            logQuery.setOrderNos(orderNos);
        }
    }

    public static void main(String[] args) {
        BaseReqDto dto = new BaseReqDto();
        dto.setPatientId("1223333");
        LogQuery logQuery = LogQuery.create(dto, null, null, new Date());
        System.out.println(logQuery.getPatientId());

    }
}
