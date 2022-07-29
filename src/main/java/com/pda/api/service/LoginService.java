package com.pda.api.service;

import com.pda.api.dto.UserResDto;

/**
 * @Classname LoginService
 * @Description TODO
 * @Date 2022-07-23 9:50
 * @Created by AlanZhang
 */
public interface LoginService {
    UserResDto login(String account, String password);
}
