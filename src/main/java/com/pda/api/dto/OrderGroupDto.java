package com.pda.api.dto;

import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.base.BaseReqDto;
import com.pda.utils.DateUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Classname OrderGroupDto
 * @Description TODO
 * @Date 2023-03-18 20:48
 * @Created by AlanZhang
 */
@Data
public class OrderGroupDto {

    private BaseReqDto req;

    private List<OrdersM> orders;

    private Set<String> labels;

    private List<String> status;

    private String excuteType;

    private Date queryTime;

    @Override
    public String toString() {
        return "OrderGroupDto{" +
                "req=" + req +
                ", labels=" + labels +
                ", status=" + status +
                ", excuteType='" + excuteType + '\'' +
                ", queryTime=" + DateUtil.formatDateToStr(queryTime) +
                '}';
    }
}
