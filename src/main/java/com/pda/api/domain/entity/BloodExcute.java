package com.pda.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("blood_excute")
public class BloodExcute {

    private String patientId;

    private Integer visitId;

    private String bloodId;
    /**
     * 0:未执行 1:执行中 2:暂停 3:执行完毕
     */
    private Integer status;

    private String excuteUserCode;

    private String excuteUserName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date excuteTime;
}
