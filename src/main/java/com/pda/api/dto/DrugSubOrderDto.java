package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname DrugSubOrderDto
 * @Description TODO
 * @Date 2022-08-04 21:25
 * @Created by AlanZhang
 */
@Data
public class DrugSubOrderDto {

    private Integer orderSubNo;

    private String orderText;

    private String dosage;

    private String administation;

    private String freqDetail;
}
