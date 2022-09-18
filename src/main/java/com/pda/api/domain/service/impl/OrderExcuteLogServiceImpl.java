package com.pda.api.domain.service.impl;

import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.service.IOrderExcuteLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pda.api.dto.query.LogQuery;
import com.pda.api.mapper.slave.OrderExcuteLogMapper;
import com.pda.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-03
 */
@Service
public class OrderExcuteLogServiceImpl extends ServiceImpl<OrderExcuteLogMapper, OrderExcuteLog> implements IOrderExcuteLogService {

    @Autowired
    private OrderExcuteLogMapper orderExcuteLogMapper;

    @Override
    public List<OrderExcuteLog> findDistinctLog(LogQuery logQuery) {
        List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectCheckedLatestLog(logQuery.getPatientId(), logQuery.getVisitId(), logQuery.getOrderNos(), logQuery.getType(), logQuery.getExcuteDate());
        return orderExcuteLogs;
    }

    @Override
    public List<OrderExcuteLog> findOperLog(LogQuery logQuery) {
        List<OrderExcuteLog> orderExcuteLogs = orderExcuteLogMapper.selectCheckedExcuteLog(logQuery.getPatientId(), logQuery.getVisitId(), logQuery.getOrderNos(), logQuery.getType(), logQuery.getExcuteDate());
        return orderExcuteLogs;
    }
}
