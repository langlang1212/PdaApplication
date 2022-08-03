package com.pda.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Classname DrugDispensionReqDto
 * @Description TODO
 * @Date 2022-08-03 21:18
 * @Created by AlanZhang
 */
@Data
public class DrugDispensionReqDto {

    @NotBlank(message = "病人id不能为空")
    private String patientId;
    /**
     * j今天或者明天 0  1
     */
    @NotNull
    private String todayOrTomorrow;
    /**
     * 临时或者长期  0  1
     */
    private Integer repeatIndicator = 1;
}
