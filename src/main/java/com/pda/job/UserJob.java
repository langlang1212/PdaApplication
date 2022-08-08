package com.pda.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pda.api.domain.entity.UserInfo;
import com.pda.api.domain.service.IUserInfoService;
import com.pda.api.dto.UserResDto;
import com.pda.api.mapper.slave.UserInfoMapper;
import com.pda.api.service.PdaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname UserJob
 * @Description TODO
 * @Date 2022-08-01 22:24
 * @Created by AlanZhang
 */
@Component
@Slf4j
public class UserJob {

    @Autowired
    private PdaService pdaService;
    @Autowired
    private IUserInfoService iUserInfoService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    //添加定时任务
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    //@PostConstruct
    @Transactional(rollbackFor = Exception.class,transactionManager = "ds2TransactionManager")
    public void syncUsers() {
        log.info("同步用户信息!");
        iUserInfoService.deleteAllUsers();

        List<UserInfo> userInfos = new ArrayList<>();
        List userList = pdaService.getUsers();
        for(Object obj : userList) {
            UserInfo userInfo = new UserInfo();
            UserResDto userResDto = JSONObject.parseObject(JSON.toJSONString(obj)).toJavaObject(UserResDto.class);
            BeanUtils.copyProperties(userResDto,userInfo);
            //userInfoMapper.insert(userInfo);
            userInfos.add(userInfo);
        }

        iUserInfoService.saveBatch(userInfos);
        log.info("同步用户信息成功!");
    }
}
