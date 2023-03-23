package com.pda.api.domain.excuteBus.Strategy;

import com.pda.api.domain.enums.ModuleTypeEnum;
import com.pda.api.domain.excuteBus.ExcuteStrategy;
import com.pda.api.dto.base.BaseCountDto;
import com.pda.api.dto.base.BaseOrderDto;
import com.pda.api.service.MobileCommonService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Classname AllStrategy
 * @Description TODO
 * @Date 2022-10-15 22:29
 * @Created by AlanZhang
 */
@Slf4j
public class AllStrategy implements ExcuteStrategy {

    private MobileCommonService mobileCommonService;

    private Set<String> labels;

    public AllStrategy(MobileCommonService mobileCommonService,Set<String> labels){
        this.mobileCommonService = mobileCommonService;
        this.labels = labels;
    }

    @Override
    public void count(BaseCountDto result, Date queryTime,String patientId,Integer visitId) {
        // 1、处理其他医嘱
        /*this.mobileCommonService.countOtherOrder(result,queryTime,patientId,visitId);*/
        // 2、处理his分类医嘱
        this.mobileCommonService.countHisOrder(result,queryTime,patientId,visitId,this.labels);
    }

    @Override
    public void list(List<BaseOrderDto> result,String patientId,Integer visitId) {
        // 1、处理其他医嘱
        /*this.mobileCommonService.listOtherOrder(result,queryTime,patientId,visitId);*/
        // 2、处理his分类医嘱
        Long startTime = System.currentTimeMillis();
        this.mobileCommonService.listHisOrder(result,patientId,visitId,this.labels);
        Long endTime = System.currentTimeMillis();
        log.info("list his医嘱列表及日志用时:{}",endTime - startTime);
    }
}
