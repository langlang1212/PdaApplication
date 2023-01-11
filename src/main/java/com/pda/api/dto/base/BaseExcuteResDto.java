package com.pda.api.dto.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Classname BaseExcuteResDto
 * @Description TODO
 * @Date 2022-09-18 23:15
 * @Created by AlanZhang
 */
@Data
public class BaseExcuteResDto extends BaseOrderDto{

    private String excuteStatus;
}
