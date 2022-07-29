package com.pda.api.service;

/**
 * @Classname CheckService
 * @Description TODO
 * @Date 2022-07-22 18:12
 * @Created by AlanZhang
 */
public interface CheckService {
    String findCheckoutInfo(Integer pageNum);

    String findCheckInfo(Integer pageNum);

    String findCheckApplyInfo(Integer pageNum);
}
