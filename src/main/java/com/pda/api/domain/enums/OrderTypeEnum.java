package com.pda.api.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Classname OrderTypeEnum
 * @Description TODO
 * @Date 2023-03-18 20:13
 * @Created by AlanZhang
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum OrderTypeEnum {

    LONG("long",1),SHORT("short",0);

    private String code;

    private Integer value;

    public String code(){
        return this.code;
    }

    public int value(){
        return this.value.intValue();
    }

    public static Integer getValueByCode(String code){
        OrderTypeEnum[] values = values();
        for(OrderTypeEnum orderTypeEnum : values){
            if(orderTypeEnum.getCode().equals(code)){
                return orderTypeEnum.value();
            }
        }
        return null;
    }
}
