package com.pda.api.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname ExcuteReq
 * @Description TODO
 * @Date 2022-08-07 10:21
 * @Created by AlanZhang
 */
@Data
@ApiModel("口服给药执行")
public class ExcuteReq {

    @ApiModelProperty("病人id")
    private String patientId;

    @ApiModelProperty("住院次数")
    private Integer visitId;

    @ApiModelProperty("单号no")
    private Integer orderNo;

    @ApiModelProperty("子单号")
    private Integer orderSubNo;

    @ApiModelProperty("1:待配液 2:未执行 3:正在执行 4:暂停 5:执行完毕  口服给药的时候没有记录就是未执行，医嘱执行没有记录就是待配液  传入的是下一个状态")
    private String excuteStatus;

    @ApiModelProperty("1:摆药 2:配液 3:医嘱执行 4:口服给药 5:皮试医嘱")
    private String type;

    @ApiModelProperty("执行日期 yyyy-MM-dd")
    private String excuteDate;
}
