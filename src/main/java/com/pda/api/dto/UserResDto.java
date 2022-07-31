package com.pda.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.pda.common.dto.DictDto;
import lombok.Data;

import java.util.List;

/**
 * @Classname UserResDto
 * @Description TODO
 * @Date 2022-07-23 9:52
 * @Created by AlanZhang
 */
@Data
public class UserResDto {

    private String action;

    private String itemVersion;
    @JSONField(name = "EMP_NO")
    private String empNo;
    @JSONField(name = "DEPT_CODE")
    private String deptCode;
    /**
     * 科室名称
     */
    private String deptName;
    @JSONField(name = "NAME")
    private String name;
    @JSONField(name = "INPUT_CODE")
    private String inputCode;
    @JSONField(name = "ID")
    private String id;
    @JSONField(name = "USER_NAME")
    private String userName;
    @JSONField(name = "JOB")
    private String job;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "ID_NO")
    private String idNo;
    @JSONField(name = "SEX")
    private String sex;
    @JSONField(name = "CREATE_DATE")
    private String createDate;
    @JSONField(name = "STATUS")
    private String status;

    private List<DictDto> wards;


}
