package com.pda.api.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.dto.DrugSubOrderDto;
import com.pda.utils.DateUtil;
import com.pda.utils.LocalDateUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Classname BaseOrderDto
 * @Description TODO
 * @Date 2022-09-18 17:46
 * @Created by AlanZhang
 */
@Data
public class BaseOrderDto implements Comparable<BaseOrderDto>{

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
    /**
     * 次数
     */
    private Integer frequencyCount;
    /**
     * 2:可以完成
     */
    private String finishFlag;

    private String excuteDate;

    private List<String> schedule;
    /**
     * 0: 未停  1:停嘱
     */
    private String isStop;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime stopDateTime;

    private List<? extends BaseSubOrderDto> subOrderDtoList;

    private List<OrderExcuteLog> orderExcuteLogs;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime latestOperTime = LocalDateTime.of(2020, 1, 1, 0, 0);;

    @Override
    public int compareTo(BaseOrderDto o) {
        return this.getOrderNo() - o.getOrderNo();
    }
}
