package com.pda.api.domain.service;

import com.pda.api.domain.entity.OrderExcuteLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pda.api.dto.query.LogQuery;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-03
 */
public interface IOrderExcuteLogService extends IService<OrderExcuteLog> {

    List<OrderExcuteLog> findDistinctLog(LogQuery logQuery);

    List<OrderExcuteLog> findOperLog(LogQuery logQuery);

    List<OrderExcuteLog> getCheckedLogs(String patientId, Integer visitId, Integer orderNo, String excuteDate);

    List<OrderExcuteLog> getJiaoduiLogs(String patientId, Integer visitId,Integer orderNo, String excuteDate);

    List<OrderExcuteLog> findSpecimenLog(String patientId, Integer visitId,String testNo);

    List<OrderExcuteLog> findExcuteLog(String patientId, Integer visitId, Integer orderNo, String shouldExcuteDate,String type);

    List<OrderExcuteLog> getAllExcuteLog(String shortDate);

    List<OrderExcuteLog> findPatSpecimenLog(String patientId, Integer visitId);
}
