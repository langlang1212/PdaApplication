package com.pda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Classname OralResDto
 * @Description TODO
 * @Date 2022-08-06 20:06
 * @Created by AlanZhang
 */
@Data
public class OralResDto {

    private String orderText;

    private Integer repeatIndicator;

    private String dosAge;

    private String frequency;

    private String administration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private LocalDateTime stopDateTime;

    private String excuteDate;

    /**
     *
     */
    private String excuteStatus;
}
