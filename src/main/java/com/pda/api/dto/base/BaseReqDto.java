package com.pda.api.dto.base;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Classname BaseReqDto
 * @Description TODO
 * @Date 2022-09-18 19:38
 * @Created by AlanZhang
 */
@Data
public class BaseReqDto {

    @NotBlank(message = "病人id不能为空")
    private String patientId;

    @NotBlank(message = "visitId不能为空")
    private Integer visitId;

    /**
     * 临时或者长期  0  1
     */
    private Integer repeatIndicator = 1;

    public static BaseReqDto create(String patientId, Integer visitId) {
        BaseReqDto dto = new BaseReqDto();
        dto.setPatientId(patientId);
        dto.setVisitId(visitId);
        return dto;
    }
}
