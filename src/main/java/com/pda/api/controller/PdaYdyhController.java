package com.pda.api.controller;

import com.pda.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname PdaYdyhController
 * @Description TODO
 * @Date 2022-07-23 12:36
 * @Created by AlanZhang
 */
@RestController
@RequestMapping("/mine")
public class PdaYdyhController {

    @GetMapping("/patients")
    public Result myPatients(){
        return Result.success();
    }
}
