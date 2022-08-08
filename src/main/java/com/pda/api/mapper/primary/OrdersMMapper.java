package com.pda.api.mapper.primary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.CheckCountResDto;
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

    CheckCountResDto selectDrugDispensionCount(DrugDispensionReqDto dto);

    @Select("select * from orders_m where patient_id = #{patientId} and visit_id = #{visitId} and start_date_time <= #{endTime} and repeat_indicator = 1 " +
            "and stop_date_time is null and administration in (select distinct label from order_label_param where id = 1001) order by order_no")
    List<OrdersM> listByPatientId(@Param("patientId") String patientId, @Param("endTime") Date endTime);

    @Select("select * from orders_m where patient_id = #{patientId} and visit_id = #{visitId} and repeat_indicator = 0 and start_date_time >= #{startDateOfDay} " +
            "and start_date_time <= #{endDateOfDay} and administration in (select distinct label from order_label_param where id = 1001) order by order_no")
    List<OrdersM> listByPatientIdShort(@Param("patientId") String patientId,@Param("startDateOfDay") Date startDateOfDay,@Param("endDateOfDay") Date endDateOfDay);

    //@Select("select * from orders_m where patient_id = #{patientId} and visit_id = #{visitId} and repeat_indicator = 0 and start_date_time >= #{startDateOfDay} and start_date_time <= #{endDateOfDay} and administration = '口服' order by order_no")
    List<OrdersM> listShortOralByPatientId(@Param("patientId") String patientId,@Param("startDateOfDay") Date startDateOfDay,@Param("endDateOfDay") Date endDateOfDay,@Param("visitId")Integer visitId);

    //@Select("select * from orders_m where patient_id = #{patientId} and visit_id = #{visitId} and start_date_time <= #{endTime} and repeat_indicator = 1 and stop_date_time is null and administration = '口服' order by order_no")
    List<OrdersM> listLongOralByPatientId(@Param("patientId") String patientId, @Param("endTime") Date endTime,@Param("visitId") Integer visitId);

    List<OrdersM> listShortOrderByPatientId(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("startDateOfDay") Date startDateOfDay,@Param("endDateOfDay") Date endDateOfDay,@Param("type") String type,@Param("drugType") Integer drugType);

    List<OrdersM> listLongOrderByPatientId(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("endTime") Date queryTime,@Param("type") String type,@Param("drugType") Integer drugType);
}
