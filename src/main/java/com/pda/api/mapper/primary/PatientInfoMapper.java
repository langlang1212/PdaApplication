package com.pda.api.mapper.primary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.PatientInfo;
import com.pda.api.dto.PatientInfoDto;
import com.pda.common.dto.DictDto;
import org.apache.ibatis.annotations.Param;
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

    @Select("select distinct bed_no from patient_info where ward_code = #{wardCode}")
    List<Integer> findAlreadyBed(String wardCode);
}
