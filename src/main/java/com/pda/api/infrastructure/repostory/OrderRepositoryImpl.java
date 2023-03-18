package com.pda.api.infrastructure.repostory;

import com.pda.api.domain.entity.OrdersM;
import com.pda.api.domain.repository.OrderRepository;
import com.pda.api.dto.base.BaseReqDto;
import com.pda.api.mapper.primary.OrdersMMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Classname CheckRepositoryImpl
 * @Description TODO
 * @Date 2023-03-18 19:58
 * @Created by AlanZhang
 */
@Component
@Slf4j
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private OrdersMMapper ordersMMapper;
    /**
     * 获取摆药核查医嘱
     * @param dto
     * @return
     */
    @Override
    public List<OrdersM> listOrder(BaseReqDto dto) {
        List<OrdersM> orders = ordersMMapper.selectOrderByPatient(dto.getPatientId(),dto.getVisitId());
        return orders;
    }
}
