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
 * @since 2022-08-16
 */
@TableName("specimen_check")
public class SpecimenCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    private String patientId;

    private Integer visitId;

    private String testNo;

    /**
     * 1:已核对 2:已送检
     */
    private String status;

    private String operUserCode;

    private String operUser;

    private LocalDateTime operTime;

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
    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperUserCode() {
        return operUserCode;
    }

    public void setOperUserCode(String operUserCode) {
        this.operUserCode = operUserCode;
    }
    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }
    public LocalDateTime getOperTime() {
        return operTime;
    }

    public void setOperTime(LocalDateTime operTime) {
        this.operTime = operTime;
    }

    @Override
    public String toString() {
        return "SpecimenCheck{" +
            "patientId=" + patientId +
            ", visitId=" + visitId +
            ", testNo=" + testNo +
            ", status=" + status +
            ", operUserCode=" + operUserCode +
            ", operUser=" + operUser +
            ", operTime=" + operTime +
        "}";
    }
}
