package com.pda.api.service;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2022-07-22 12:46
 * @Created by AlanZhang
 */
public interface WardBedService {
    String findBedByWard(Integer pageNum, String wardCode);
}
