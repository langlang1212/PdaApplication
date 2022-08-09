package com.pda.api.domain.service;

import com.pda.api.domain.entity.OrderLabelParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-09
 */
public interface IOrderLabelParamService extends IService<OrderLabelParam> {

    public Set<String> labels(List<Integer> ids);

}
