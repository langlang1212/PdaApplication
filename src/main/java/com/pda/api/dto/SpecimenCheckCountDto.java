package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname SpecimenCheckCountDto
 * @Description TODO
 * @Date 2022-08-16 22:06
 * @Created by AlanZhang
 */
@Data
public class SpecimenCheckCountDto {

    private Integer total = 0;

    private Integer collaredCount = 0;

    private Integer sentCount = 0;
}
