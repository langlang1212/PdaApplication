package com.pda.api.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.PatientInfo;
import com.pda.api.dto.PatientInfoDto;
import com.pda.common.dto.DictDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-07-31
 */
public interface PatientInfoMapper extends BaseMapper<PatientInfo> {

    @Select("select distinct ward_code 'key',ward_name 'value' from patient_info where doctor_in_charge = #{userName}")
    List<DictDto> selectWardByPatient(String userName);

    List<PatientInfoDto> findMyPatient(String keyword,String wardCode,String userName);
}
