package com.pda.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Classname PdaRequest
 * @Description TODO
 * @Date 2022-07-22 13:32
 * @Created by AlanZhang
 */
@Data
public class PdaRequest {

    @JSONField(name = "AuthHeader")
    private AuthHeader authHeader;

    @JSONField(name = "ControlActProcess")
    private ControlActProcess controlActProcess;
}
