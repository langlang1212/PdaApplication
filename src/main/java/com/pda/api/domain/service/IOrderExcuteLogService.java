package com.pda.api.domain.service;

import com.pda.api.domain.entity.OrderExcuteLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pda.api.dto.query.LogQuery;

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
}
