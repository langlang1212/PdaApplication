package com.pda.api.controller;

import com.pda.api.dto.LoginDto;
import com.pda.api.dto.UserResDto;
import com.pda.api.service.LoginService;
import com.pda.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

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
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto){
        UserResDto result = loginService.login(loginDto.getAccount(),loginDto.getPassword());
        return Result.success(result);
    }
}
