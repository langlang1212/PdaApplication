package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname BaseCountDto
 * @Description TODO
 * @Date 2022-09-18 17:56
 * @Created by AlanZhang
 */
@Data
public class BaseCountDto {

    /**
     * 总瓶数
     */
    private Integer totalBottles = 0;
    /**
     * 已核查瓶数
     */
    private Integer checkedBottles = 0;
    /**
     * 剩余
     */
    private Integer surplusBottles = 0;

    /**
     * 临时总瓶数
     */
    private Integer tempTotalBottles = 0;
    /**
     * 临时已核查瓶数
     */
    private Integer tempCheckedBottles = 0;
    /**
     * 临时剩余
     */
    private Integer tempSurplusBottles = 0;
}
