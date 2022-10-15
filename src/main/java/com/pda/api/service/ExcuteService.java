package com.pda.api.service;

import com.pda.api.dto.*;
import com.pda.api.dto.base.BaseOrderDto;

import java.util.List;

/**
 * @Classname ExcuteService
 * @Description TODO
 * @Date 2022-08-06 19:54
 * @Created by AlanZhang
 */
public interface ExcuteService {
    List<? extends BaseOrderDto> oralList(String patientId, Integer visitId);

    void oralExcute(List<ExcuteReq> oralExcuteReq);

    List<? extends BaseOrderDto> skinList(String patientId,Integer visitId);

    void skinExcute(List<ExcuteReq> skinExcuteReqs);

    OrderCountResDto orderCount(String patientId,Integer visitId,String drugType);

    List<? extends BaseOrderDto> orderExcuteList(String patientId,Integer visitId,String drugType);

    void excute(List<ExcuteReq> excuteReqs);

    void refresh();
}
