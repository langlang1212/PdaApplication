package com.pda.api.controller;

import com.pda.api.dto.UserResDto;
import com.pda.api.service.LoginService;
import com.pda.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname LoginController
 * @Description TODO
 * @Date 2022-07-23 9:47
 * @Created by AlanZhang
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 先传emp_no
     * @param account
     * @param password
     * @return
     */
    @GetMapping("/login/{account}/{password}")
    public Result login(@PathVariable(name = "account",required = true) String account,
                        @PathVariable(name = "password",required = true) String password){
        UserResDto user = loginService.login(account,password);
        return Result.success(user);
    }
}
