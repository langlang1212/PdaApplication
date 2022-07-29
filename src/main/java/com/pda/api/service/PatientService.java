package com.pda.api.service;

import com.pda.api.dto.PatientAllergyReqDto;
import com.pda.api.dto.PatientReqDto;

/**
 * @Classname PatientService
 * @Description TODO
 * @Date 2022-07-22 14:47
 * @Created by AlanZhang
 */
public interface PatientService {
    String fintPatientInfo(PatientReqDto patientReqDto);

    String fintAllergyInfo(Integer pageNum, String patientNo);

    String fintTzInfo(Integer pageNum, String patientNo,Integer upTime);

}
