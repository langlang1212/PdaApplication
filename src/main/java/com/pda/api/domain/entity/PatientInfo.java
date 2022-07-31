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
@TableName("patient_info")
public class PatientInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String patientId;

    private Integer visitId;

    private String inpNo;

    private String name;

    private String sex;

    private LocalDateTime dateOfBirth;

    private LocalDateTime admissionDateTime;

    private String wardCode;

    private String deptCode;

    private String wardName;

    private String deptName;

    private Integer bedNo;

    private String bedLabel;

    private String roomNo;

    private String doctorInCharge;

    private String diagnosis;

    private String alergyDrugs;

    private String patientStatusName;

    private String nursingClass;

    private String nursingClassName;

    private String nursingClassColor;

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
    public String getInpNo() {
        return inpNo;
    }

    public void setInpNo(String inpNo) {
        this.inpNo = inpNo;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public LocalDateTime getAdmissionDateTime() {
        return admissionDateTime;
    }

    public void setAdmissionDateTime(LocalDateTime admissionDateTime) {
        this.admissionDateTime = admissionDateTime;
    }
    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }
    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public Integer getBedNo() {
        return bedNo;
    }

    public void setBedNo(Integer bedNo) {
        this.bedNo = bedNo;
    }
    public String getBedLabel() {
        return bedLabel;
    }

    public void setBedLabel(String bedLabel) {
        this.bedLabel = bedLabel;
    }
    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
    public String getDoctorInCharge() {
        return doctorInCharge;
    }

    public void setDoctorInCharge(String doctorInCharge) {
        this.doctorInCharge = doctorInCharge;
    }
    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public String getAlergyDrugs() {
        return alergyDrugs;
    }

    public void setAlergyDrugs(String alergyDrugs) {
        this.alergyDrugs = alergyDrugs;
    }
    public String getPatientStatusName() {
        return patientStatusName;
    }

    public void setPatientStatusName(String patientStatusName) {
        this.patientStatusName = patientStatusName;
    }
    public String getNursingClass() {
        return nursingClass;
    }

    public void setNursingClass(String nursingClass) {
        this.nursingClass = nursingClass;
    }
    public String getNursingClassName() {
        return nursingClassName;
    }

    public void setNursingClassName(String nursingClassName) {
        this.nursingClassName = nursingClassName;
    }
    public String getNursingClassColor() {
        return nursingClassColor;
    }

    public void setNursingClassColor(String nursingClassColor) {
        this.nursingClassColor = nursingClassColor;
    }

    @Override
    public String toString() {
        return "PatientInfo{" +
            "patientId=" + patientId +
            ", visitId=" + visitId +
            ", inpNo=" + inpNo +
            ", name=" + name +
            ", sex=" + sex +
            ", dateOfBirth=" + dateOfBirth +
            ", admissionDateTime=" + admissionDateTime +
            ", wardCode=" + wardCode +
            ", deptCode=" + deptCode +
            ", wardName=" + wardName +
            ", deptName=" + deptName +
            ", bedNo=" + bedNo +
            ", bedLabel=" + bedLabel +
            ", roomNo=" + roomNo +
            ", doctorInCharge=" + doctorInCharge +
            ", diagnosis=" + diagnosis +
            ", alergyDrugs=" + alergyDrugs +
            ", patientStatusName=" + patientStatusName +
            ", nursingClass=" + nursingClass +
            ", nursingClassName=" + nursingClassName +
            ", nursingClassColor=" + nursingClassColor +
        "}";
    }
}
