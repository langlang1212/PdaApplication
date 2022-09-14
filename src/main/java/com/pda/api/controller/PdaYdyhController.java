package com.pda.api.controller;

import com.pda.api.service.PdaService;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname PdaYdyhController
 * @Description TODO
 * @Date 2022-07-23 12:36
 * @Created by AlanZhang
 */
@RestController
@Api(tags = "床位管理")
public class PdaYdyhController {

    @Autowired
    private PdaService pdaService;

    @ApiOperation(value = "床位管理")
    @GetMapping("/bed/{wardCode}")
    public Result myPatients(@PathVariable("wardCode") String wardCode){
        return Result.success(pdaService.beds(wardCode,1));
    }
}
