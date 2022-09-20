package com.pda.api.mapper.primary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.CheckCountResDto;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.api.dto.PatientInfoDto;
import com.pda.common.dto.DictDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-07-31
 */
public interface OrdersMMapper extends BaseMapper<OrdersM> {

    List<DictDto> selectWardByOrder(String userName);

    List<PatientInfoDto> findMyPatient(@Param("keyword") String keyword, @Param("wardCode") String wardCode, @Param("userName") String userName);

    List<OrdersM> listShortOrderByPatientId(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("startDateOfDay") Date startDateOfDay,@Param("endDateOfDay") Date endDateOfDay,@Param("labels") Set<String> labels,@Param("statusList") List<String> status);

    List<OrdersM> listLongOrderByPatientId(@Param("patientId") String patientId, @Param("visitId") Integer visitId, @Param("endTime") Date queryTime, @Param("labels") Set<String> labels,@Param("statusList") List<String> status);
}
