package com.pda.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Classname SpecimenCheckOperDto
 * @Description TODO
 * @Date 2022-08-17 20:00
 * @Created by AlanZhang
 */
@Data
@ApiModel("标本送检操作请求对象")
public class SpecimenCheckOperDto {

    @ApiModelProperty("病人id")
    @NotEmpty(message = "病人id为空")
    private String patientId;
    @ApiModelProperty("第几次住院")
    @NotNull(message = "来访次数不能为null")
    private Integer visitId;
    @ApiModelProperty("检验单号")
    @NotEmpty(message = "测试号不能为空")
    private String testNo;
    @ApiModelProperty("1:核对 2:送检")
    @NotEmpty(message = "状态不能为空")
    private String status;
    @ApiModelProperty("设备号")
    private String deviceNo;
}
