package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Classname PatrolOperDto
 * @Description TODO
 * @Date 2022-10-08 20:32
 * @Created by AlanZhang
 */
@Data
public class PatrolOperDto {

    private String patientId;

    private Integer visitId;

    private String type;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    private List<PatrolLabelDto> patrolLabelDtos;
}
