package com.pda.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @NotNull
    private String patientId;
    @ApiModelProperty("第几次住院")
    @NotNull
    private Integer visitId;
    @ApiModelProperty("检验单号")
    @NotNull
    private String testNo;
    @ApiModelProperty("1:核对 2:送检")
    @NotNull
    private String status;
}
