package com.pda.api.domain.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pda.api.domain.entity.OrderLabelParam;
import com.pda.api.domain.service.IOrderLabelParamService;
import com.pda.api.mapper.slave.OrderLabelParamMapper;
import com.pda.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-09
 */
@Service
public class OrderLabelParamServiceImpl extends ServiceImpl<OrderLabelParamMapper, OrderLabelParam> implements IOrderLabelParamService {

    @Override
    public Set<String> labels(List<Integer> ids) {
        LambdaQueryWrapper<OrderLabelParam> paramQueryWrapper = new LambdaQueryWrapper();
        paramQueryWrapper.in(OrderLabelParam::getId,ids);
        List<OrderLabelParam> params = this.list(paramQueryWrapper);
        Set<String> labelSet = params.stream().map(OrderLabelParam::getLabel).collect(Collectors.toSet());
        return labelSet;
    }

    @Override
    public Set<String> getLaeblsByModule(String moduleCode) {
        LambdaQueryWrapper<OrderLabelParam> labelParamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        labelParamLambdaQueryWrapper.eq(OrderLabelParam::getModuleCode,moduleCode);
        List<OrderLabelParam> list = this.list(labelParamLambdaQueryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            Set<String> set = list.stream().map(OrderLabelParam::getLabel).collect(Collectors.toSet());
            return set;
        }
        return null;
    }
}
