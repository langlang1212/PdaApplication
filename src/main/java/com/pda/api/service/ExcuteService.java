package com.pda.api.service;

import com.pda.api.dto.ExcuteReq;
import com.pda.api.dto.OralResDto;

import java.util.List;

/**
 * @Classname ExcuteService
 * @Description TODO
 * @Date 2022-08-06 19:54
 * @Created by AlanZhang
 */
public interface ExcuteService {
    List<OralResDto> oralList(String patientId);

    void oralExcute(List<ExcuteReq> oralExcuteReq);
}
