package com.pda.api.service;

import com.pda.api.dto.*;

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

    List<SkinResDto> skinList(String patientId);

    void skinExcute(List<ExcuteReq> skinExcuteReqs);

    OrderCountResDto orderCount(String patientId,String drugType);

    List<OrderResDto> orderExcuteList(String patientId,String drugType);

    void excute(List<ExcuteReq> excuteReqs);
}
