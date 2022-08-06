package com.pda.api.controller;

import com.pda.api.domain.service.DrugCheckService;
import com.pda.api.dto.DrugCheckReqDto;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname DrugCheckController
 * @Description TODO
 * @Date 2022-08-03 20:57
 * @Created by AlanZhang
 */
@RestController
@Api(tags = "摆药核查模块、配液核对模块")
public class CheckController {

    @Autowired
    private DrugCheckService drugCheckService;

    /**
     * 摆药核查  0:今天 1:明天
     * @return
     */
    @ApiOperation(value = "摆药核查统计")
    @PostMapping("/drug/check/count")
    public Result DrugDispensionCheck(@Validated @RequestBody DrugDispensionReqDto dto){
        return Result.success(drugCheckService.drugDispensionCount(dto));
    }

    @ApiOperation(value = "摆药核查明细")
    @PostMapping("/drug/check/order")
    public Result DrugOrders(@Validated @RequestBody DrugDispensionReqDto dto){
        return Result.success(drugCheckService.drugOrders(dto));
    }

    @ApiOperation(value = "摆药核查")
    @PostMapping("/drug/check")
    public Result DrugCheck(@RequestBody List<DrugCheckReqDto> drugCheckReqDtoList){
        drugCheckService.check(drugCheckReqDtoList);
        return Result.success();
    }

    @ApiOperation(value = "配液核对统计")
    @PostMapping("/distribution/check/count")
    public Result liquidCheckCount(@Validated @RequestBody DrugDispensionReqDto dto){
        return Result.success(drugCheckService.distributionCount(dto));
    }

    @ApiOperation(value = "配液核对明细")
    @PostMapping("/distribution/check/order")
    public Result distributionOrders(@Validated @RequestBody DrugDispensionReqDto dto){
        return Result.success(drugCheckService.distributionOrders(dto));
    }
}
