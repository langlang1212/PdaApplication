package com.pda.api.controller;

import com.pda.api.dto.PatientInfoDto;
import com.pda.api.service.PatientService;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
