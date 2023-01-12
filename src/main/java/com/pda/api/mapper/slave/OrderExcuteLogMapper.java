package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.OrderExcuteLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-08-03
 */
public interface OrderExcuteLogMapper extends BaseMapper<OrderExcuteLog> {

    List<OrderExcuteLog> selectCheckedExcuteLog(@Param("patientId") String patientId,@Param("visitId") Integer visitId, @Param("orderNos") List<Integer> orderNos,  @Param("types") List<String> types,@Param("excuteDate") String excuteDate);

    List<OrderExcuteLog> selectCheckedLatestLog(@Param("patientId") String patientId,@Param("visitId") Integer visitId, @Param("orderNos") List<Integer> orderNos, @Param("types") List<String> types,@Param("excuteDate") String excuteDate);

    List<OrderExcuteLog> selectExcuteLatestLog(@Param("patientId") String patientId,@Param("visitId") Integer visitId, @Param("orderNos") List<Integer> orderNos, @Param("type") String type,@Param("excuteDate") String excuteDate);

    @Update("update order_excute_log set excute_user_code = #{excuteUserCode},excute_user_name = #{excuteUserName},excute_status = #{excuteStatus},excute_time=#{excuteTime},remark=#{remark} " +
            "where patient_id = #{patientId} and visit_id = #{visitId} and order_no = #{orderNo} and type = #{type}")
    void updateLog(OrderExcuteLog existLog);

    @Select("select * from order_excute_log where patient_id = #{patientId} and visit_id = #{visitId} and order_no = #{orderNo} and excute_date = #{excuteDate} and check_status = '1' and type = '1'")
    List<OrderExcuteLog> selectCheckedLog(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("orderNo") Integer orderNo,@Param("excuteDate") String excuteDate);

    @Select("select * from order_excute_log where patient_id = #{patientId} and visit_id = #{visitId} and order_no = #{orderNo} and excute_date = #{excuteDate} and check_status = '1' and type = '2'")
    List<OrderExcuteLog> selectJiaoduiLog(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("orderNo") Integer orderNo,@Param("excuteDate") String excuteDate);

    @Select("select * from order_excute_log where patient_id = #{patientId} and visit_id = #{visitId} and test_no = #{testNo} and type in ('6','7') and check_status = '1' order by type,check_time")
    List<OrderExcuteLog> selectSpecimenLog(String patientId, Integer visitId,String testNo);

    @Select("select * from order_excute_log where type = #{type} and excute_date = #{shortDate}")
    List<OrderExcuteLog> selectAllExcuteLog(@Param("shortDate") String shortDate,@Param("type") String type);

    @Select("select * from order_excute_log where patient_id = #{patientId} and visit_id = #{visitId} and type in ('6','7') and check_status = '1' order by type,check_time")
    List<OrderExcuteLog> selectPatSpecimenLog(@Param("patientId") String patientId,@Param("visitId") Integer visitId);
}
