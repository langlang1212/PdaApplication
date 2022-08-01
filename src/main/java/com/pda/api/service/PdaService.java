package com.pda.api.service;

import com.pda.api.dto.UserResDto;

import java.util.List;

/**
 * @Classname PdaService
 * @Description TODO
 * @Date 2022-07-31 20:56
 * @Created by AlanZhang
 */
public interface PdaService {

    public List getUsers();

    public List getDepts();

    public UserResDto getCurrentUser();

    public UserResDto getUserByCode(String userName);
}
