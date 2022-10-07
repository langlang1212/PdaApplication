package com.pda.api.controller;

import com.pda.api.dto.LoginDto;
import com.pda.api.dto.UserResDto;
import com.pda.api.service.LoginService;
import com.pda.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname LoginController
 * @Description TODO
 * @Date 2022-07-23 9:47
 * @Created by AlanZhang
 */
@RestController
@Api(tags = "登陆模块")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 先传emp_no
     * @param loginDto
     * @return
     */
    @ApiOperation(value = "登陆")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto){
        Map<String,Object> result = loginService.login(loginDto.getAccount(),loginDto.getPassword());
        return Result.success(result);
    }

    @ApiOperation(value = "扫码登陆")
    @PostMapping("/loginByQrcode")
    public Result loginByQrcode(@RequestBody LoginDto loginDto){
        Map<String,Object> result = loginService.loginByQrcode(loginDto.getAccount());
        return Result.success(result);
    }

    @GetMapping("/logout")
    public Result logout(@RequestHeader String accessToken){
        loginService.logout(accessToken);
        return Result.success();
    }
}
