package com.pda.api.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pda.api.domain.entity.OrderTypeDict;
import com.pda.api.mapper.OrderTypeDictMapper;
import com.pda.api.domain.service.IOrderTypeDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        // 1、查询数据库
        LambdaQueryWrapper<OrderTypeDict> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(OrderTypeDict::getCode,types);
        List<OrderTypeDict> list = list(lambdaQueryWrapper);
        // 2、处理所有用法
        List<String> labels = new ArrayList<>();
        list.forEach(obj -> {
            List<String> texts = Arrays.asList(obj.getText().split(";"));
            labels.addAll(texts);
        });
        return labels.stream().distinct().collect(Collectors.toSet());
    }
}
