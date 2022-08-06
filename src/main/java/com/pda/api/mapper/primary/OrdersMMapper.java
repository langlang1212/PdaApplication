package com.pda.api.mapper.primary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.DrugDispensingCountResDto;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.api.dto.PatientInfoDto;
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
public interface OrdersMMapper extends BaseMapper<OrdersM> {

    List<DictDto> selectWardByOrder(String userName);

    List<PatientInfoDto> findMyPatient(@Param("keyword") String keyword, @Param("wardCode") String wardCode, @Param("userName") String userName);

    DrugDispensingCountResDto selectDrugDispensionCount(DrugDispensionReqDto dto);

    @Select("select * from orders_m where patient_id = #{patientId} and start_date_time <= #{endTime} order by order_no ")
    List<OrdersM> listByPatientId(@Param("patientId") String patientId, @Param("endTime") Date endTime);
}
