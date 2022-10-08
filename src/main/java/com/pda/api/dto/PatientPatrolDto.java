package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Classname PatientPatrolDto
 * @Description TODO
 * @Date 2022-10-08 20:19
 * @Created by AlanZhang
 */
@Data
public class PatientPatrolDto {

    private String patientId;

    private Integer visitId;

    private String patrolId;

    private String patrolName;

    private String operUserCode;

    private String operUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date operTime;

    private String type;
}
