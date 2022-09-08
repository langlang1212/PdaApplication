package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pda.api.domain.entity.OrderExcuteLog;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Classname DrugOrderResDto
 * @Description TODO
 * @Date 2022-08-04 21:19
 * @Created by AlanZhang
 */
@Data
public class DrugOrderResDto implements Comparable<DrugOrderResDto>{

    private String patientId;

    private Integer orderNo;

    private Integer visitId;

    /**
     * 0:临时 1:长期
     */
    private Integer repeatIndicator;

    /**
     * 例如: 1/日
     */
    private String frequency;

    private String excuteDate;

    private List<String> schedule;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime stopDateTime;

    private List<DrugSubOrderDto> subOrderDtoList;

    private List<OrderExcuteLog> orderExcuteLogs;

    @Override
    public int compareTo(DrugOrderResDto o) {
        return this.getOrderNo() - o.getOrderNo();
    }
}
