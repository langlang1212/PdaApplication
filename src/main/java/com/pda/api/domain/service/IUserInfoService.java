package com.pda.api.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pda.api.domain.entity.UserInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-01
 */
public interface IUserInfoService extends IService<UserInfo> {

    void deleteAllUsers();
}
