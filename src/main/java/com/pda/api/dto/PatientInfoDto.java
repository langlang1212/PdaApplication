package com.pda.api.dto;

import lombok.Data;

/**
 * @Classname PatientInfoDto
 * @Description TODO
 * @Date 2022-07-31 20:09
 * @Created by AlanZhang
 */
@Data
public class PatientInfoDto {

    private String name;

    private String age;

    private String sex;

    private String inpNo;

    private Integer inpDays;

    private Integer bedNo;

    private String bedDesc;

    private String wardCode;

    private String wardName;

    private String doctorCode;

    private String doctorName;

    private String nurseCode;

    private String nurseName;
}
