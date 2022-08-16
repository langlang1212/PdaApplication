package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Classname SpecimenCheckResDto
 * @Description TODO
 * @Date 2022-08-16 20:39
 * @Created by AlanZhang
 */
@Data
public class SpecimenCheckResDto {

    private String testNo;

    private String patientId;

    private Integer visitId;
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
     * 核对人
     */
    private String collartor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date collarDate;
    /**
     * 送检人
     */
    private String sendUser;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime;
    /**
     * 1:已核对 2:已送检
     */
    private String status;
}
