package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.BloodExcute;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Classname BloodExcuteMapper
 * @Description TODO
 * @Date 2022-12-14 10:01
 * @Created by AlanZhang
 */
public interface BloodExcuteMapper extends BaseMapper<BloodExcute> {

    @Update("update blood_excute set status = #{status},excute_user_code=#{excuteUserCode},excute_user_name=#{excuteUserName},excute_time=#{excuteTime} where patient_id = #{patientId} and visit_id = #{visitId} and blood_id = #{bloodId}")
    int updateExcute(BloodExcute excute);

    @Select("select patient_id as patientId,visit_id as visitId,blood_id as bloodId,status from blood_excute where patient_id = #{patientId} and visit_id = #{visitId}")
    List<BloodExcute> selectBloodStatus(@Param("patientId") String patientId, @Param("visitId") Integer visitId);
}
