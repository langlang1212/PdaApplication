package com.pda.api.mapper.primary;

import com.pda.api.dto.SpecimenCheckResDto;
import com.pda.api.dto.UserResDto;
import com.pda.common.dto.DictDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-07-31
 */
public interface MobileCommonMapper {

    UserResDto checkUser(@Param("account") String account);

    @Select("select dept_code key,dept_name value from orders_czy where user_name = #{userName} and group_class = '病区医生'")
    List<DictDto> selectDoctorWard(String userName);
    @Select("select dept_code key,dept_name value from orders_czy where user_name = #{userName} and group_class = '病区护士'")
    List<DictDto> selectNurseWard(String userName);

    List<SpecimenCheckResDto> selectSubjectCheck(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("startTime") Date startTime,@Param("endTime") Date endTime);
}
