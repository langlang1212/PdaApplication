package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname PatientReqDto
 * @Description TODO
 * @Date 2022-07-22 15:02
 * @Created by AlanZhang
 */
@Data
public class PatientReqDto extends PageReqDto{

    private String startTime;

    private String endTime;

    private String status;
}
