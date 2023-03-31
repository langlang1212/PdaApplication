package com.pda.api.mapper.slave;

import com.pda.api.dto.PatientPatrolDto;
import com.pda.api.dto.PatrolLabelDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-08-03
 */
public interface PatrolMapper{

    @Select("select id 'patrolId',name 'patrolName',code 'patrolCode' from patrol ")
    List<PatrolLabelDto> selectPatrolLabel();

    @Select("select patient_id 'patientId',visit_id 'visitId'," +
            "patrol_id 'patrolId',patrol_name 'patrolName'," +
            "oper_user_code 'operUserCode',oper_user_name 'operUserName'," +
            "oper_time 'operTime', type " +
            "from patient_patrol where patient_id = #{patientId} and visit_id = #{visitId} and oper_time >= DATE_SUB(now(),INTERVAL 2 DAY) order by operTime desc")
    List<PatientPatrolDto> selectPatientPatrol(String patientId, Integer visitId);

    void inserPatientPatrol(PatientPatrolDto patientPatrolDto);

}
