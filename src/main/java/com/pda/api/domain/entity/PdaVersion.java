package com.pda.api.domain.entity;

import lombok.Data;

/**
 * @author AlanZhang
 * @version 1.0
 * @classname PdaVersion
 * @description
 * @date 2023/01/02 10:41
 */
@Data
public class PdaVersion {

    /**
     * 版本 数字
     */
    private Long version;
    /**
     * 版本 如 1.0.0
     */
    private String versionDesc;
    /**
     * 是否更新
     */
    private Integer isUpdate;
    /**
     * 更新内容
     */
    private String content;
}
