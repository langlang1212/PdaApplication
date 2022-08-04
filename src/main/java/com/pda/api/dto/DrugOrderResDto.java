package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pda.api.domain.entity.OrderExcuteLog;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Classname DrugOrderResDto
 * @Description TODO
 * @Date 2022-08-04 21:19
 * @Created by AlanZhang
 */
@Data
public class DrugOrderResDto {

    private String patientId;

    private Integer orderNo;

    private String administration;
    /**
     * 例如: 1/日
     */
    private String frequency;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date excuteDate;

    private List<DrugSubOrderDto> subOrderDtoList;

    private List<OrderExcuteLog> orderExcuteLogs;
}
