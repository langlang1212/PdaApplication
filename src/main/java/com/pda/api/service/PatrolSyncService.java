package com.pda.api.service;

import com.pda.api.domain.entity.PatientInfo;
import com.pda.api.dto.PatientPatrolDto;
import com.pda.api.dto.PatrolSyncDto;

/**
 * @Classname PatrolSyncService
 * @Description TODO
 * @Date 2023-03-28 21:31
 * @Created by AlanZhang
 */
public interface PatrolSyncService {

    public void syncPatrol(PatientPatrolDto patientPatrolDto, PatientInfo patientInfo, PatrolSyncDto patrolSyncDto);
}
