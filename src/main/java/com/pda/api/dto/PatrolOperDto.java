package com.pda.api.dto;

import lombok.Data;

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

    private List<PatrolLabelDto> patrolLabelDtos;
}
