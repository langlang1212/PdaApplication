package com.pda.api.dto.query;

import lombok.Data;

import java.util.List;

/**
 * @Classname ExcuteLogQuery
 * @Description TODO
 * @Date 2022-09-18 22:41
 * @Created by AlanZhang
 */
@Data
public class ExcuteLogQuery extends LogQuery {

    private List<String> types;
}
