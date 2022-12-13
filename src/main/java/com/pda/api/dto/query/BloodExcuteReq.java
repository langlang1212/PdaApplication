package com.pda.api.dto.query;

import lombok.Data;

/**
 * @Classname BloodExcuteReq
 * @Description TODO
 * @Date 2022-12-13 19:46
 * @Created by AlanZhang
 */
@Data
public class BloodExcuteReq {

    private String patientId;

    private Integer visitId;

    private String bloodId;

    /**
     * 0：取血 1:接血 2:输血前核对 3:复核 4:开始执行 5:执行完毕
     */
    private Integer status;
}
