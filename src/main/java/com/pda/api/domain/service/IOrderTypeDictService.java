package com.pda.api.domain.service;

import com.pda.api.domain.entity.OrderTypeDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-09-04
 */
public interface IOrderTypeDictService extends IService<OrderTypeDict> {

    Set<String> findLabelsByType(List<String> types);
}
