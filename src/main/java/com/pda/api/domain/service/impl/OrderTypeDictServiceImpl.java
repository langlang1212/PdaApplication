package com.pda.api.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pda.api.domain.entity.OrderTypeDict;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.pda.api.mapper.slave.OrderTypeDictMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public Set<String> findLabelsByType(List<String> types) {
        Set<String> labels = new HashSet<>();
        LambdaQueryWrapper<OrderTypeDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(OrderTypeDict::getCode,types);
        List<OrderTypeDict> list = list(lambdaQueryWrapper);
        list.forEach(o -> {
            List<String> splits = Arrays.asList(o.getText().split(";"));
            labels.addAll(splits);
        });
        return labels;
    }
}
