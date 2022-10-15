package com.pda.api.domain.excuteBus.Strategy;

import com.pda.api.domain.excuteBus.ExcuteStrategy;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.service.MobileCommonService;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Classname OtherStrategy
 * @Description TODO
 * @Date 2022-10-15 23:46
 * @Created by AlanZhang
 */
public class OtherStrategy implements ExcuteStrategy {

    private MobileCommonService mobileCommonService;

    private Set<String> labels;

    public OtherStrategy(MobileCommonService mobileCommonService,Set<String> labels){
        this.mobileCommonService = mobileCommonService;
        this.labels = labels;
    }
    @Override
    public void count(BaseCountDto result, Date queryTime, String patientId, Integer visitId) {
        this.mobileCommonService.countOtherOrder(result,queryTime,patientId,visitId);
    }

    @Override
    public void list(List<BaseOrderDto> result, Date queryTime, String patientId, Integer visitId) {
        this.mobileCommonService.listOtherOrder(result,queryTime,patientId,visitId);
    }
}
