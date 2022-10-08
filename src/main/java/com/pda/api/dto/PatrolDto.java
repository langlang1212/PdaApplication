package com.pda.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Classname PatrolDto
 * @Description TODO
 * @Date 2022-10-08 19:56
 * @Created by AlanZhang
 */
@Data
public class PatrolDto {

    private List<PatrolLabelDto> labels;

    /**
     * 病人巡视记录
     */

    private List<PatientPatrolDto> patientPatrolDtos;
}
