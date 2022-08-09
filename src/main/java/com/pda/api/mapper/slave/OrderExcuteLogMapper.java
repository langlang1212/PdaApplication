package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.OrderExcuteLog;
import org.apache.ibatis.annotations.Param;
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

    List<OrderExcuteLog> selectCheckedExcuteLog(@Param("patientId") String patientId,@Param("visitId") Integer visitId, @Param("orderNos") List<Integer> orderNos, @Param("type") String type,@Param("excuteDate") String excuteDate);

    List<OrderExcuteLog> selectExcuteLog(@Param("patientId") String patientId,@Param("visitId") Integer visitId, @Param("orderNos") List<Integer> orderNos, @Param("type") String type,@Param("excuteDate") String excuteDate);

    @Update("update order_excute_log set excute_user_code = #{excuteUserCode},excute_user_name = #{excuteUserName},excute_status = #{excuteStatus},excute_time=#{excuteTime} " +
            "where patient_id = #{patientId} and visit_id = #{visitId} and order_no = #{orderNo} and order_sub_no = #{orderSubNo}")
    void updateLog(OrderExcuteLog existLog);
}
