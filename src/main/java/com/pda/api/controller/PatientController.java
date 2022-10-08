package com.pda.api.controller;

import com.pda.api.dto.PatientInfoDto;
import com.pda.api.dto.PatrolDto;
import com.pda.api.dto.PatrolOperDto;
import com.pda.api.service.PatientService;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname PatientController
 * @Description TODO
 * @Date 2022-07-31 20:18
 * @Created by AlanZhang
 */
@Api(tags = "病人模块")
@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @ApiOperation(value = "我的病人")
    @GetMapping("/mypatient")
    public Result myPatient(@RequestParam(value = "keyword",required = false) String keyword,@RequestParam("wardCode") String wardCode){
        List<PatientInfoDto> result = patientService.findMyPatient(keyword,wardCode);
        return Result.success(result);
    }

    @ApiOperation(value = "患者巡视")
    @GetMapping("/patrol/{patientId}/{visitId}")
    public Result patrol(@PathVariable("patientId") String patientId, @PathVariable("visitId") Integer visitId){
        PatrolDto result = patientService.findPatrol(patientId,visitId);
        return Result.success(result);
    }

    @ApiOperation("患者巡视记录写入")
    @PostMapping("/patrol/oper")
    public Result doPatrol(@RequestBody PatrolOperDto patrolOperDto){
        patientService.oper(patrolOperDto);
        return Result.success();
    }
}
