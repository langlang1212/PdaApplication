package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Classname PatrolSyncDto
 * @Description TODO
 * @Date 2023-03-28 21:08
 * @Created by AlanZhang
 */
@Data
public class PatrolSyncDto {

    private String zhuyuanId;

    private String xunchaTime;

    private String xunchaHushiId;

    private String xunchaHushiName;

    private String hisDisplayZhuyuanhao;

    private String xingming;

    private String bingchuangHao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    private String dataSource;

    private Integer yiyuanId;

    private String bingquId;

    private String bingquName;

    private String keshiId;

    private String keshiName;

    private String saomiaoSource;

    private String isBulu;

    private List<PatrolSyncItemDto> items;
}
