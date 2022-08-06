package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname CheckReqDto
 * @Description TODO
 * @Date 2022-08-06 12:40
 * @Created by AlanZhang
 */
@Data
public class CheckReqDto {

    private String patientId;

    private Integer orderNo;

    private Integer orderSubNo;
    /**
     * yyyy-MM-dd
     */
    private String shouldExcuteDate;
}
