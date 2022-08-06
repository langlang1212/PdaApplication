package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname DrugCheckReqDto
 * @Description TODO
 * @Date 2022-08-06 12:40
 * @Created by AlanZhang
 */
@Data
public class DrugCheckReqDto {

    private String patientId;

    private Integer orderNo;

    private Integer orderSubNo;

    private String shouldExcuteDate;
}
