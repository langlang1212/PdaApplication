package com.pda.api.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    TYPE1("1001"),TYPE2("1002"),TYPE3("1003"),TYPE4("1004"),TYPE5("1005"),
    TYPE6("1006"),TYPE7("1007"),TYPE8("1008"),TYPE9("1009");

    private String code;

    public String code(){
        return this.code;
    }

    public static List<String> getAllCodes(){
        List<String> types = new ArrayList<>();

        ModuleTypeEnum[] values = values();
        for(ModuleTypeEnum moduleTypeEnum : values){
            types.add(moduleTypeEnum.code());
        }
        return types;
    }

    public static List<String> getHisTypeCodes(){
        List<String> types = new ArrayList<>();
        types.add(TYPE1.code());
        types.add(TYPE2.code());
        types.add(TYPE3.code());
        types.add(TYPE4.code());
        types.add(TYPE5.code());
        types.add(TYPE6.code());
        return types;
    }
}
