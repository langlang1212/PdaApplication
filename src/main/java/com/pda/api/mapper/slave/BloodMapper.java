package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.BloodExcute;
import com.pda.api.domain.entity.BloodInfo;
import com.pda.api.domain.entity.BloodOperLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Classname BloodMapper
 * @Description TODO
 * @Date 2022-12-12 21:11
 * @Created by AlanZhang
 */
public interface BloodMapper extends BaseMapper<BloodInfo> {

    List<BloodOperLog> selectLogs(@Param("patientId") String patientId,@Param("visitId") Integer visitId,@Param("bloodIds") List<String> bloodIds);
}
