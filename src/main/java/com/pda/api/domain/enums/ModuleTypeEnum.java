package com.pda.api.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Classname ModuleTypeEnum
 * @Description TODO
 * @Date 2022-09-04 17:17
 * @Created by AlanZhang
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ModuleTypeEnum {

    TYPE1("1001"),TYPE2("1002"),TYPE3("1003"),TYPE4("1004"),TYPE5("1005"),TYPE6("1006");

    private String code;

    public String code(){
        return this.code;
    }
}
