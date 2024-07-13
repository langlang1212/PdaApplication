package com.pda.api.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Classname BloodBaseInfo
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/7/13 10:20
 * @Created by AlanZhang
 */
@Data
public class BloodBaseInfo {
    private String patientId;

    private Integer visitId;

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
     * 每一条血袋信息
     */
    private List<BloodInfo> infos;
    /**
     * 一组血袋的操作日志
     */
    private List<BloodOperLog> commonLogs;
}
