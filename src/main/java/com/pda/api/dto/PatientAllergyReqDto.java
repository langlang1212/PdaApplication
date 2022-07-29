package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname PatientAllergyReqDto
 * @Description TODO
 * @Date 2022-07-22 15:38
 * @Created by AlanZhang
 */
@Data
public class PatientAllergyReqDto extends PageReqDto{

    private String patientNo;
}
