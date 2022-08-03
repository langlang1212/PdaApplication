package com.pda.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-08-03
 */
@TableName("order_excute_log")
public class OrderExcuteLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String patientId;

    private Integer orderNo;

    private Integer orderSubNo;

    /**
     * 应执行日期
     */
    private LocalDate excuteDate;

    private String excuteUserCode;

    private String excuteUserName;

    /**
     * 1:待配液 2:未执行 3:正在执行 4:暂停 5:执行完毕
     */
    private String excuteStatus;

    /**
     * 执行时间
     */
    private LocalDateTime excuteTime;

    /**
     * 1:已核查
     */
    private String drugDispensingStatus;

    /**
     * 核查时间
     */
    private LocalDateTime drugDispensionCheckTime;

    /**
     * 1:已校对
     */
    private String liquidStatus;

    /**
     * 校对时间
     */
    private LocalDateTime liquidTime;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
    public Integer getOrderSubNo() {
        return orderSubNo;
    }

    public void setOrderSubNo(Integer orderSubNo) {
        this.orderSubNo = orderSubNo;
    }
    public LocalDate getExcuteDate() {
        return excuteDate;
    }

    public void setExcuteDate(LocalDate excuteDate) {
        this.excuteDate = excuteDate;
    }
    public String getExcuteUserCode() {
        return excuteUserCode;
    }

    public void setExcuteUserCode(String excuteUserCode) {
        this.excuteUserCode = excuteUserCode;
    }
    public String getExcuteUserName() {
        return excuteUserName;
    }

    public void setExcuteUserName(String excuteUserName) {
        this.excuteUserName = excuteUserName;
    }
    public String getExcuteStatus() {
        return excuteStatus;
    }

    public void setExcuteStatus(String excuteStatus) {
        this.excuteStatus = excuteStatus;
    }
    public LocalDateTime getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(LocalDateTime excuteTime) {
        this.excuteTime = excuteTime;
    }
    public String getDrugDispensingStatus() {
        return drugDispensingStatus;
    }

    public void setDrugDispensingStatus(String drugDispensingStatus) {
        this.drugDispensingStatus = drugDispensingStatus;
    }
    public LocalDateTime getDrugDispensionCheckTime() {
        return drugDispensionCheckTime;
    }

    public void setDrugDispensionCheckTime(LocalDateTime drugDispensionCheckTime) {
        this.drugDispensionCheckTime = drugDispensionCheckTime;
    }
    public String getLiquidStatus() {
        return liquidStatus;
    }

    public void setLiquidStatus(String liquidStatus) {
        this.liquidStatus = liquidStatus;
    }
    public LocalDateTime getLiquidTime() {
        return liquidTime;
    }

    public void setLiquidTime(LocalDateTime liquidTime) {
        this.liquidTime = liquidTime;
    }

    @Override
    public String toString() {
        return "OrderExcuteLog{" +
            "patientId=" + patientId +
            ", orderNo=" + orderNo +
            ", orderSubNo=" + orderSubNo +
            ", excuteDate=" + excuteDate +
            ", excuteUserCode=" + excuteUserCode +
            ", excuteUserName=" + excuteUserName +
            ", excuteStatus=" + excuteStatus +
            ", excuteTime=" + excuteTime +
            ", drugDispensingStatus=" + drugDispensingStatus +
            ", drugDispensionCheckTime=" + drugDispensionCheckTime +
            ", liquidStatus=" + liquidStatus +
            ", liquidTime=" + liquidTime +
        "}";
    }
}
