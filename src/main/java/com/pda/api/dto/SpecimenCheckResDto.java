package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pda.api.domain.entity.OrderExcuteLog;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Classname SpecimenCheckResDto
 * @Description TODO
 * @Date 2022-08-16 20:39
 * @Created by AlanZhang
 */
@Data
public class SpecimenCheckResDto {

    private String patientId;

    private Integer visitId;

    private String testNo;

    /**
     * 检查名称
     */
    private String subject;

    private String performedBy;

    private String performedDeptName;
    /**
     * 标本
     */
    private String specimen;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date requestedDateTime;
    /**
     * 申请人
     */
    private String requester;

    /**
     * 1:已核对 2:已送检
     */
    private String status;

    private List<OrderExcuteLog> orderExcuteLogList;
}
