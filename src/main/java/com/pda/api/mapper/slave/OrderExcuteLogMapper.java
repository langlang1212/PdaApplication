package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.OrderExcuteLog;
import org.apache.ibatis.annotations.Param;

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

    List<OrderExcuteLog> selectCheckedExcuteLog(@Param("patientId") String patientId, @Param("orderNos") List<Integer> orderNos, @Param("type") String type,@Param("excuteDate") String excuteDate);

    List<OrderExcuteLog> selectExcuteLog(@Param("patientId") String patientId, @Param("orderNos") List<Integer> orderNos, @Param("type") String type,@Param("excuteDate") String excuteDate);
}
