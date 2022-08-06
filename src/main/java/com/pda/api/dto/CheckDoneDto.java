package com.pda.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Classname CheckDoneDto
 * @Description TODO
 * @Date 2022-08-06 22:58
 * @Created by AlanZhang
 */
@Data
public class CheckDoneDto {

    private List<CheckReqDto> checkReqDtoList;
}
