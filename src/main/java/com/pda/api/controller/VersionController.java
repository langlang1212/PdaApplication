package com.pda.api.controller;

import cn.hutool.core.util.ObjectUtil;
import com.pda.api.domain.entity.PdaVersion;
import com.pda.api.mapper.slave.VersionMapper;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AlanZhang
 * @version 1.0
 * @classname VersionController
 * @description
 * @date 2023/01/02 10:40
 */
@RestController
@Api("版本更新检查")
public class VersionController {

    @Autowired
    private VersionMapper versionMapper;

    @GetMapping("/version/{version}")
    public Result checkUpdate(@PathVariable("version") Long version){
        PdaVersion pdaVersion = versionMapper.selectVersion();
        if(ObjectUtil.isNotNull(pdaVersion) && pdaVersion.getVersion().longValue() > version.longValue()){
            return Result.success(pdaVersion);
        }else{
            return Result.success(null);
        }
    }
}
