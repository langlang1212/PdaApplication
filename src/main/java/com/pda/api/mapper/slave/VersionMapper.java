package com.pda.api.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.PdaVersion;
import org.apache.ibatis.annotations.Select;

public interface VersionMapper extends BaseMapper<PdaVersion> {

    @Select("select top 1 * from pda_version order by version desc")
    PdaVersion selectVersion();
}
