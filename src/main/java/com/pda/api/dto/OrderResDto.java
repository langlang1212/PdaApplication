package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname OrderResDto
 * @Description TODO
 * @Date 2022-08-07 14:40
 * @Created by AlanZhang
 */
@Data
public class OrderResDto extends DrugOrderResDto{

    /**
     *
     */
    private String excuteStatus;
}
