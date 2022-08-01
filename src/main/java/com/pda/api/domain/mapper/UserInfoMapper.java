package com.pda.api.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pda.api.domain.entity.UserInfo;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-08-01
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Delete("delete from user_info")
    int deleteAll();
}
