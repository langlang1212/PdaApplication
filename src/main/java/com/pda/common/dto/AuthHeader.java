package com.pda.common.dto;

import lombok.Data;

/**
 * @Classname AuthHeader
 * @Description TODO
 * @Date 2022-07-22 13:19
 * @Created by AlanZhang
 */
@Data
public class AuthHeader {

    private String msgType;

    private String msgId = "F4A4F960-5B0E-4889-874B-DA732ECD0844";

    private String createTime;

    private String sourceId = "1.3.6.1.4.1.1000000.2016.100";

    private String targetId = "1.3.6.1.4.1.1000000.2016.100";
}
