package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.BloodOperLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Classname BloodOperLogMapper
 * @Description TODO
 * @Date 2022-12-14 10:01
 * @Created by AlanZhang
 */
public interface BloodOperLogMapper extends BaseMapper<BloodOperLog> {
    void insertBatch(List<BloodOperLog> operLogs);
}
