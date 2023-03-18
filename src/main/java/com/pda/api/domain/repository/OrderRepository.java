package com.pda.api.domain.repository;

import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.base.BaseReqDto;

import java.util.List;

/**
 * @Classname OrderRepository
 * @Description 摆药核查、配液核对repository接口
 * @Date 2023-03-18 19:56
 * @Created by AlanZhang
 */
public interface OrderRepository {

    List<OrdersM> listOrder( BaseReqDto dto);
}
