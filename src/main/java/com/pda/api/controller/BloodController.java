package com.pda.api.controller;

import com.pda.api.domain.service.BloodService;
import com.pda.api.dto.query.BloodExcuteReq;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname BloodController
 * @Description TODO
 * @Date 2022-12-12 20:56
 * @Created by AlanZhang
 */
@RestController
@Api(tags = "输血送血")
public class BloodController {

    @Autowired
    private BloodService bloodService;

    @ApiOperation(value = "输血送血列表")
    @GetMapping("/blood/list/{patientId}/{visitId}")
    public Result list(@PathVariable("patientId") String patientId,@PathVariable("visitId") Integer visitId){
        return Result.success(bloodService.list(patientId,visitId));
    }

    @ApiOperation(value = "输血送血执行步骤")
    @PostMapping("/blood/excute")
    public Result excute(@RequestBody BloodExcuteReq excuteReq){
        bloodService.excute(excuteReq);
        return Result.success();
    }

}
