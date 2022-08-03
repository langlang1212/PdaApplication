package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Classname PatientInfoDto
 * @Description TODO
 * @Date 2022-07-31 20:09
 * @Created by AlanZhang
 */
@Data
public class PatientInfoDto {
    /**
     * 病人id
     */
    private String patientId;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date birthDay;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date admissionDate;
    /**
     *  自己计算
     */
    private String age;

    private String sex;

    private String inpNo;
    /**
     *  自己计算 住院天数
     */
    private Integer inpDays;

    private Integer bedNo;

    private String bedDesc;

    private String wardCode;

    private String wardName;

    private String doctorCode;
    /**
     *  设置值
     */
    private String doctorName;

    private String nurseCode;

    private String nurseName;

    private String diagnosis;
}
