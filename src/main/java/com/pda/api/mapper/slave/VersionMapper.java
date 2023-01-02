package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.PdaVersion;
import org.apache.ibatis.annotations.Select;

public interface VersionMapper extends BaseMapper<PdaVersion> {

    @Select("select * from pda_version order by version desc limit 1")
    PdaVersion selectVersion();
}
