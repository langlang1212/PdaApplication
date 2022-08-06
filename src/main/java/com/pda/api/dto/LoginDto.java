package com.pda.api.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname LoginDto
 * @Description TODO
 * @Date 2022-07-31 12:40
 * @Created by AlanZhang
 */
@Data
@ApiModel("LoginDto")
public class LoginDto {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;
}
