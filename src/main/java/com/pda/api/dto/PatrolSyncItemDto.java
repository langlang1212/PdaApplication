package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Classname PatrolSyncItemDto
 * @Description TODO
 * @Date 2023-03-28 21:26
 * @Created by AlanZhang
 */
@Data
public class PatrolSyncItemDto {

    private Integer xunchaJiluId;

    private String xunchaItemCode;

    private String xunchaItemName;

    private String xunchaItemType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    private String dataSource;

    private Integer yiyuanId;
}
