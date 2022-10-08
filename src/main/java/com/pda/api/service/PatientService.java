package com.pda.api.service;

import com.pda.api.dto.*;

import java.util.List;

/**
 * @Classname PatientService
 * @Description TODO
 * @Date 2022-07-22 14:47
 * @Created by AlanZhang
 */
public interface PatientService {
    String fintPatientInhInfo(PatientReqDto patientReqDto);

    String fintAllergyInfo(Integer pageNum, String patientNo);

    String fintTzInfo(Integer pageNum, String patientNo,Integer upTime);

    String findPatientInfo(PatientReqDto patientReqDto);

    List<PatientInfoDto> findMyPatient(String keyword,String wardCode);

    PatrolDto findPatrol(String patientId, Integer visitId);

    void oper(PatrolOperDto patrolOperDto);
}
