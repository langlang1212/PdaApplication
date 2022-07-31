package com.pda.api.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.OrdersM;
import com.pda.common.dto.DictDto;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-07-31
 */
public interface OrdersMMapper extends BaseMapper<OrdersM> {

    List<DictDto> selectWardByOrder(String userName);
}
