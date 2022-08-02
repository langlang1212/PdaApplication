package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Classname DrugDispensingResDto
 * @Description TODO
 * @Date 2022-08-02 22:18
 * @Created by AlanZhang
 */
@Data
public class DrugDispensingResDto {
    /**
     * 1：长期 0:临时
     */
    private Integer repeatIndicator;
    /**
     * 用法
     */
    private String admission;
    /**
     * 频率
     */
    private String frequency;
    /**
     * 频率详情
     */
    private String freqDetail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date excuteTime;

    // TODO: 2022-08-02 这里还有核查列表  核查表结构未设计
}
