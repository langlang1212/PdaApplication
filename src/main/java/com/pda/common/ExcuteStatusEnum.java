package com.pda.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Classname ExcuteStatusEnum
 * @Description TODO
 * @Date 2022-08-06 12:56
 * @Created by AlanZhang
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ExcuteStatusEnum {

    PREPARED("1","待配液"),
    NO_EXCUTE("2","未执行"),
    EXCUTEING("3","正在执行"),
    PAUSE("4","暂停"),
    COMPLETED("5","执行完毕");

    private String code;

    private String text;

    public String code(){
        return this.code;
    }

    public String text(){
        return this.text;
    }
}
