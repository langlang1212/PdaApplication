package com.pda.api.domain.controller;

import com.pda.api.domain.service.DrugCheckService;
import com.pda.api.dto.DrugDispensionReqDto;
import com.pda.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname DrugCheckController
 * @Description TODO
 * @Date 2022-08-03 20:57
 * @Created by AlanZhang
 */
@RestController
public class DrugCheckController {

    @Autowired
    private DrugCheckService drugCheckService;

    /**
     * 摆药核查  0:今天 1:明天
     * @return
     */
    @PostMapping("/drug/dispension/count")
    public Result DrugDispensionCheck(@Validated @RequestBody DrugDispensionReqDto dto){
        return Result.success(drugCheckService.drugDispensionCount(dto));
    }

    @PostMapping("/drug/order")
    public Result DrugOrders(@Validated @RequestBody DrugDispensionReqDto dto){
        return Result.success(drugCheckService.drugOrders(dto));
    }
}
