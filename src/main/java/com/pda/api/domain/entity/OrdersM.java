package com.pda.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-07-31
 */
@TableName("orders_m")
public class OrdersM implements Serializable {

    private static final long serialVersionUID = 1L;

    private String wardCode;

    private String patientId;

    private Integer visitId;

    private Integer orderNo;

    private Integer orderSubNo;

    private Integer repeatIndicator;

    private String orderClass;

    private String orderClassName;

    private String orderStatus;

    private String orderStatusName;

    private String orderText;

    private String orderCode;

    private String dosage;

    private String dosageUnits;

    private String administration;

    private Integer duration;

    private String durationUnits;

    private LocalDateTime startDateTime;

    private LocalDateTime stopDateTime;

    private String frequency;

    private Integer freqCounter;

    private Integer freqInterval;

    private String freqIntervalUnit;

    private String freqDetail;

    private String performSchedule;

    private String performResult;

    private String orderingDept;

    private String doctor;

    private String stopDoctor;

    private String nurse;

    private String stopNurse;

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
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
    public Integer getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(Integer repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }
    public String getOrderClass() {
        return orderClass;
    }

    public void setOrderClass(String orderClass) {
        this.orderClass = orderClass;
    }
    public String getOrderClassName() {
        return orderClassName;
    }

    public void setOrderClassName(String orderClassName) {
        this.orderClassName = orderClassName;
    }
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }
    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }
    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public String getDosageUnits() {
        return dosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        this.dosageUnits = dosageUnits;
    }
    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    public String getDurationUnits() {
        return durationUnits;
    }

    public void setDurationUnits(String durationUnits) {
        this.durationUnits = durationUnits;
    }
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    public LocalDateTime getStopDateTime() {
        return stopDateTime;
    }

    public void setStopDateTime(LocalDateTime stopDateTime) {
        this.stopDateTime = stopDateTime;
    }
    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public Integer getFreqCounter() {
        return freqCounter;
    }

    public void setFreqCounter(Integer freqCounter) {
        this.freqCounter = freqCounter;
    }
    public Integer getFreqInterval() {
        return freqInterval;
    }

    public void setFreqInterval(Integer freqInterval) {
        this.freqInterval = freqInterval;
    }
    public String getFreqIntervalUnit() {
        return freqIntervalUnit;
    }

    public void setFreqIntervalUnit(String freqIntervalUnit) {
        this.freqIntervalUnit = freqIntervalUnit;
    }
    public String getFreqDetail() {
        return freqDetail;
    }

    public void setFreqDetail(String freqDetail) {
        this.freqDetail = freqDetail;
    }
    public String getPerformSchedule() {
        return performSchedule;
    }

    public void setPerformSchedule(String performSchedule) {
        this.performSchedule = performSchedule;
    }
    public String getPerformResult() {
        return performResult;
    }

    public void setPerformResult(String performResult) {
        this.performResult = performResult;
    }
    public String getOrderingDept() {
        return orderingDept;
    }

    public void setOrderingDept(String orderingDept) {
        this.orderingDept = orderingDept;
    }
    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
    public String getStopDoctor() {
        return stopDoctor;
    }

    public void setStopDoctor(String stopDoctor) {
        this.stopDoctor = stopDoctor;
    }
    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }
    public String getStopNurse() {
        return stopNurse;
    }

    public void setStopNurse(String stopNurse) {
        this.stopNurse = stopNurse;
    }

    @Override
    public String toString() {
        return "OrdersM{" +
            "wardCode=" + wardCode +
            ", patientId=" + patientId +
            ", visitId=" + visitId +
            ", orderNo=" + orderNo +
            ", orderSubNo=" + orderSubNo +
            ", repeatIndicator=" + repeatIndicator +
            ", orderClass=" + orderClass +
            ", orderClassName=" + orderClassName +
            ", orderStatus=" + orderStatus +
            ", orderStatusName=" + orderStatusName +
            ", orderText=" + orderText +
            ", orderCode=" + orderCode +
            ", dosage=" + dosage +
            ", dosageUnits=" + dosageUnits +
            ", administration=" + administration +
            ", duration=" + duration +
            ", durationUnits=" + durationUnits +
            ", startDateTime=" + startDateTime +
            ", stopDateTime=" + stopDateTime +
            ", frequency=" + frequency +
            ", freqCounter=" + freqCounter +
            ", freqInterval=" + freqInterval +
            ", freqIntervalUnit=" + freqIntervalUnit +
            ", freqDetail=" + freqDetail +
            ", performSchedule=" + performSchedule +
            ", performResult=" + performResult +
            ", orderingDept=" + orderingDept +
            ", doctor=" + doctor +
            ", stopDoctor=" + stopDoctor +
            ", nurse=" + nurse +
            ", stopNurse=" + stopNurse +
        "}";
    }
}
