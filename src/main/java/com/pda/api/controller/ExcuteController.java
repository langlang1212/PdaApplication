package com.pda.api.controller;

import com.pda.api.dto.ExcuteReq;
import com.pda.api.service.ExcuteService;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname ExcuteController
 * @Description TODO
 * @Date 2022-08-06 19:49
 * @Created by AlanZhang
 */
@RestController
@Api(tags = "医嘱执行、口服给药")
public class ExcuteController {

    @Autowired
    private ExcuteService excuteService;

    @GetMapping("/oral/{patientId}")
    @ApiOperation("口服给药列表")
    public Result oralList(@PathVariable("patientId") String patientId){
        return Result.success(excuteService.oralList(patientId));
    }

    @PostMapping("/oral/excute")
    public Result oralExcute(@RequestBody List<ExcuteReq> oralExcuteReqs){
        excuteService.oralExcute(oralExcuteReqs);
        return Result.success();
    }
}
