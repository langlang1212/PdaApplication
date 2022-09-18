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

    @GetMapping("/oral/{patientId}/{visitId}")
    @ApiOperation("口服给药列表")
    public Result oralList(@PathVariable("patientId") String patientId,@PathVariable("visitId") Integer visitId){
        return Result.success(excuteService.oralList(patientId,visitId));
    }

    @PostMapping("/oral/excute")
    @ApiOperation("口服给药执行")
    public Result oralExcute(@RequestBody List<ExcuteReq> oralExcuteReqs){
        excuteService.oralExcute(oralExcuteReqs);
        return Result.success();
    }

    @GetMapping("/skin/{patientId}/{visitId}")
    @ApiOperation("皮试医嘱")
    public Result skinExcute(@PathVariable("patientId") String patientId,@PathVariable("visitId") Integer visitId){
        return Result.success(excuteService.skinList(patientId,visitId));
    }

    @PostMapping("/skin/excute")
    @ApiOperation("皮试医嘱执行")
    public Result skinExcute(@RequestBody List<ExcuteReq> skinExcuteReqs){
        excuteService.skinExcute(skinExcuteReqs);
        return Result.success();
    }

    /**
     * 0:全部 1:输液 2:雾化 3、注射 4、静推
     * @param patientId
     * @param drugType
     * @return
     */
    @GetMapping("/order/count/{patientId}/{visitId}/{drugType}")
    @ApiOperation("医嘱执行统计")
    public Result orderCount(@PathVariable("patientId") String patientId,
                             @PathVariable("visitId") Integer visitId,
                             @PathVariable("drugType") String drugType)
    {
        return Result.success(excuteService.orderCount(patientId,visitId,drugType));
    }

    @GetMapping("/order/{patientId}/{visitId}/{drugType}")
    @ApiOperation("医嘱执行列表")
    public Result orderExcute(@PathVariable("patientId") String patientId,
                              @PathVariable("visitId") Integer visitId,
                              @PathVariable("drugType") String drugType)
    {
        return Result.success(excuteService.orderExcuteList(patientId,visitId,drugType));
    }

    @PostMapping("/order/excute")
    public Result doExcute(@RequestBody List<ExcuteReq> excuteReqs){
        excuteService.excute(excuteReqs);
        return Result.success();
    }
}
