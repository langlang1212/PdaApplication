package com.pda.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Classname BloodInfo
 * @Description TODO
 * @Date 2022-12-12 20:27
 * @Created by AlanZhang
 */
@Data
public class BloodInfo {

    private String patientId;

    private Integer visitId;

    private String bloodId;

    private Integer bloodQty;
    /**
     * 输血要求
     */
    private String bloodDiagnose;
    /**
     * 输血目的
     */
    private String sxmd;
    /**
     * 输血目的备注
     */
    private String sxmd1;
    /**
     * 需血时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date applyDate;
    /**
     * 是否有输血史  1:有 0:无
     */
    private String isFanying;
    /**
     * 是否有妊娠史 1:有 0:无
     */
    private String isRenshen;
    /**
     * 复检人编号
     */
    private String auditMan;
    /**
     * 配血人编号
     */
    private String matchOperator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date matchDate;
    /**
     * 发血人编号
     */
    private String jsOperator;
    /**
     * 发血时间
     */
    private String jssj;
    /**
     * 血型
     */
    private String sqabo;
    /**
     * 血液成分
     */
    private String bloodTypeName;
    /**
     * 血液容量
     */
    private String transCapacity;
    /**
     * 0:未执行 1:执行中 2:暂停 3:执行完毕
     */
    private String status;

    private List<BloodOperLog> logs;
}
