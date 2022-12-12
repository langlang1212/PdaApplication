package com.pda.api.controller;

import com.pda.api.domain.service.BloodService;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/blood/list/{patientId}/{visitId}")
    public Result list(@PathVariable("patientId") String patientId,@PathVariable("visitId") Integer visitId){
        return Result.success(bloodService.list(patientId,visitId));
    }
}
