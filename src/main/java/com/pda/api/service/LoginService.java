package com.pda.api.service;

import com.pda.api.dto.UserResDto;

import java.util.Map;

/**
 * @Classname LoginService
 * @Description TODO
 * @Date 2022-07-23 9:50
 * @Created by AlanZhang
 */
public interface LoginService {
    Map<String,Object> login(String account, String password);

    void logout(String accessToken);

    Map<String, Object> loginByQrcode(String account);
}
