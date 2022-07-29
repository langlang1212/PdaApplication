package com.pda.api.service;

/**
 * @Classname AdviceService
 * @Description TODO
 * @Date 2022-07-22 17:26
 * @Created by AlanZhang
 */
public interface AdviceService {
    String fintAdviceInfo(Integer pageNum);

    String fintAdviceHandelInfo(Integer pageNum);

    String fintAdviceUsageInfo(Integer pageNum);

    String fintAdviceFrequencyInfo(Integer pageNum);

}
