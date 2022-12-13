package com.pda.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Classname BloodOperLog
 * @Description TODO
 * @Date 2022-12-12 20:09
 * @Created by AlanZhang
 */
@Data
public class BloodOperLog {

    private String patientId;

    private Integer visitId;

    private String bloodId;
    /**
     * 0：取血 1:接血 2:输血前核对 3:复核 4:开始执行 5:执行完毕
     */
    private Integer status;

    private String operUserCode;

    private String operUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operTime;

    private String createUserCode;

    private String createUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
