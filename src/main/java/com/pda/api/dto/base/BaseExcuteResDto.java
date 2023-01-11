package com.pda.api.dto.base;

import lombok.Data;

/**
 * @Classname BaseExcuteResDto
 * @Description TODO
 * @Date 2022-09-18 23:15
 * @Created by AlanZhang
 */
@Data
public class BaseExcuteResDto extends BaseOrderDto{

    private String excuteStatus;
    /**
     * 1003 瓶签单(输液)
     */
    private String type;
}
