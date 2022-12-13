package com.pda.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Classname BloodExcute
 * @Description TODO
 * @Date 2022-12-13 20:30
 * @Created by AlanZhang
 */
@Data
public class BloodExcute {

    private String patientId;

    private Integer visitId;

    private String bloodId;

    private Integer status;

    private String excuteUserCode;

    private String excuteUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date excuteTime;
}
