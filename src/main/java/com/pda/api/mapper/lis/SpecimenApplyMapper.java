package com.pda.api.mapper.lis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.SpecimenCheck;
import com.pda.api.dto.SpecimenCheckResDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Classname SpecimenApplyMapper
 * @Description TODO
 * @Date 2022-11-03 20:43
 * @Created by AlanZhang
 */
public interface SpecimenApplyMapper extends BaseMapper<SpecimenCheck> {

    List<SpecimenCheckResDto> selectSubjectCheck(String patId);

    @Update("update req_master set req_stat = '3' where barcode = #{testNo} and pat_id = #{patId}")
    int sendSpecimen(@Param("testNo") String testNo,@Param("patId") String patId);
}
