package com.pda.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

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

    private Integer visitId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime excuteTime;

    private String testNo;

    private String deviceNo;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public LocalDateTime getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(LocalDateTime checkTime) {
        this.checkTime = checkTime;
    }

    /**
     * 1:已核查
     */
    private String checkStatus;

    /**
     * 核查时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime checkTime;

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
        "}";
    }
}
