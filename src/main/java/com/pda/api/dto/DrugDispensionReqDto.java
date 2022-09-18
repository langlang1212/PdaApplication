package com.pda.api.dto;

import com.pda.api.dto.base.BaseReqDto;
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
public class DrugDispensionReqDto extends BaseReqDto {

    /**
     * j今天或者明天 0  1
     */
    @NotNull
    private String todayOrTomorrow;
}
