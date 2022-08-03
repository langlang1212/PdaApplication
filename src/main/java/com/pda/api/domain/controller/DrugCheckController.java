package com.pda.api.domain.controller;

import com.pda.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname DrugCheckController
 * @Description TODO
 * @Date 2022-08-03 20:57
 * @Created by AlanZhang
 */
@RestController
public class DrugCheckController {

    /**
     * 摆药核查
     * @return
     */
    @GetMapping("/drug/check")
    public Result DrugDispensionCheck(){
        return null;
    }
}
