package com.pda.api.mapper.primary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.OrderExcuteLog;
import com.pda.api.domain.entity.OrdersM;
import com.pda.api.dto.FoodNoticeDto;
import com.pda.api.dto.OrdersN;
import com.pda.api.dto.PatientInfoDto;
import com.pda.common.dto.DictDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    List<PatientInfoDto> findMyPatient(@Param("keyword") String keyword, @Param("deptCode") String deptCode,@Param("wardCode") String wardCode, @Param("userName") String userName);

    List<OrdersM> listShortOrderByPatientId(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("startDateOfDay") Date startDateOfDay,@Param("endDateOfDay") Date endDateOfDay,@Param("labels") Set<String> labels,@Param("statusList") List<String> status);

    List<OrdersM> listLongOrderByPatientId(@Param("patientId") String patientId, @Param("visitId") Integer visitId, @Param("endTime") Date queryTime, @Param("labels") Set<String> labels,@Param("statusList") List<String> status);

    @Select("select patient_id patientId,order_text orderText from orders_n where order_status = '2'")
    List<FoodNoticeDto> selectNotice();

    List<OrdersM> selectAllTodoOrders(@Param("labels") Set<String> labels);

    @Select("select distinct patient_id,visit_id,order_no from orders_n where order_status = '2'")
    List<OrdersN> selectAllTodoOrderN(Set<String> labels);
}
