package com.pda.api.dto.base;

import lombok.Data;

/**
 * @Classname BaseSubOrderDto
 * @Description TODO
 * @Date 2022-09-18 21:58
 * @Created by AlanZhang
 */
@Data
public class BaseSubOrderDto {

    private Integer orderSubNo;

    private String orderText;

    private String dosage;

    private String administation;

    private String freqDetail;
}
