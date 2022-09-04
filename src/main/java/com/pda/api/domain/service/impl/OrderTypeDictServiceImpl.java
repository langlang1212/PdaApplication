package com.pda.api.domain.service.impl;

import com.pda.api.domain.entity.OrderTypeDict;
import com.pda.api.mapper.OrderTypeDictMapper;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-09-04
 */
@Service
public class OrderTypeDictServiceImpl extends ServiceImpl<OrderTypeDictMapper, OrderTypeDict> implements IOrderTypeDictService {

}
