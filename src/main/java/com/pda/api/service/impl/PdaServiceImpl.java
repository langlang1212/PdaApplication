package com.pda.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pda.api.dto.UserResDto;
import com.pda.api.dto.WardBedResDto;
import com.pda.api.service.AsyncService;
import com.pda.api.service.DeptService;
import com.pda.api.service.PdaService;
import com.pda.api.service.UserService;
import com.pda.common.PdaBaseService;
import com.pda.common.redis.service.RedisService;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import com.pda.utils.PdaToJavaObjectUtil;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname PdaServiceImpl
 * @Description TODO
 * @Date 2022-07-31 20:56
 * @Created by AlanZhang
 */
@Service
public class PdaServiceImpl extends PdaBaseService implements PdaService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private AsyncService asyncService;
    @Override
    public List getUsers() {
        List userList = redisService.getCacheList("user_list");
        if(CollectionUtil.isEmpty(userList)){
            String userStr = userService.list(1);
            userList = PdaToJavaObjectUtil.convertList(userStr);
            // 存入redis
            asyncService.saveList("user_list",userList);
        }
        return userList;
    }

    @Override
    public List getDepts() {
        List deptList = redisService.getCacheList("dept_list");
        if(CollectionUtil.isEmpty(deptList)){
            String deptStr = deptService.list(1);
            deptList = PdaToJavaObjectUtil.convertList(deptStr);
            // 存入redis
            asyncService.saveList("dept_list",deptList);
        }
        return deptList;
    }

    @Override
    public UserResDto getCurrentUser() {

        return null;
    }

    @Override
    public UserResDto getUserByCode(String userName) {
        List users = getUsers();
        for(Object obj : users) {
            UserResDto userResDto = JSONObject.parseObject(JSON.toJSONString(obj)).toJavaObject(UserResDto.class);
            if(userName.equals(userResDto.getUserName())){
                return userResDto;
            }
        }
        return null;
    }

    /**
     * 获取床位信息
     * @param wardCode
     * @return
     */
    @Override
    public List<WardBedResDto> beds(String wardCode,Integer pageNum) {
        List bedList = redisService.getCacheList("bed_list");
        if(CollectionUtil.isEmpty(bedList)){
            String bedStr = getBeds(pageNum);
            Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(bedStr);
            Map<String,Object> controlActMap = (Map<String, Object>) stringObjectMap.get("ControlActProcess");
            Map<String,Object> listInfoMap = (Map<String, Object>) controlActMap.get("ListInfo");
            bedList = (List) listInfoMap.get("List");
            // 存入redis
            asyncService.saveList("bed_list",bedList);
        }
        return bedList;
    }

    private String getBeds(Integer pageNum){
        String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "    <AuthHeader>\n" +
                "        <msgType>TJ620</msgType>\n" +
                "        <msgId>F4A4F960-5B0E-4889-874B-DA732ECD0844</msgId>\n" +
                "        <createTime>"+PdaTimeUtil.getCreateTime(new Date())+"</createTime>\n" +
                "        <sourceId>1.3.6.1.4.1.1000000.2016.100</sourceId>\n" +
                "        <targetId>1.3.6.1.4.1.1000000.2016.xxx</targetId>\n" +
                "        <sysPassword/>\n" +
                "    </AuthHeader>\n" +
                "    <ControlActProcess>\n" +
                "        <PageNum>"+pageNum+"</PageNum>\n" +
                "    </ControlActProcess>\n" +
                "</root>";
        String result = CxfClient.excute(getWsProperties().getForwardUrl(), getWsProperties().getMethodName(), param);
        return result;
    }
}
