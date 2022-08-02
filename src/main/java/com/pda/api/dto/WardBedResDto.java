package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname WardBedResDto
 * @Description TODO
 * @Date 2022-08-02 19:38
 * @Created by AlanZhang
 */
@Data
public class WardBedResDto {

    private String wardCode;

    private Integer bedNo;
    /**
     * 0:没人 1:有人
     */
    private String status;
}
