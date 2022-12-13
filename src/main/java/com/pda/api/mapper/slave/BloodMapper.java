package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.BloodExcute;
import com.pda.api.domain.entity.BloodInfo;
import com.pda.api.domain.entity.BloodOperLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Classname BloodMapper
 * @Description TODO
 * @Date 2022-12-12 21:11
 * @Created by AlanZhang
 */
public interface BloodMapper extends BaseMapper<BloodInfo> {

    @Select("select patient_id as patientId,visit_id as visitId,blood_id as bloodId,status from blood_excute where patient_id = #{patientId} and visit_id = #{visitId}")
    List<BloodExcute> selectBloodStatus(@Param("patientId") String patientId, @Param("visitId") Integer visitId);

    @Select("select * from blood_oper_log where patient_id = #{patientId} and visit_id = #{visitId} and blood_id = #{bloodId} order by create_time asc")
    List<BloodOperLog> selectLogs(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("bloodId") String bloodId);
}
