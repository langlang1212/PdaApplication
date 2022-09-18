package com.pad.api;

import com.pda.AppStarter;
import com.pda.api.controller.PdaController;
import com.pda.api.service.PdaService;
import com.pda.api.service.UserService;
import com.pda.common.Result;
import com.pda.common.config.WsProperties;
import com.pda.utils.CxfClient;
import com.pda.utils.PdaTimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Classname TestController
 * @Description TODO
 * @Date 2022-09-18 14:01
 * @Created by AlanZhang
 */
@SpringBootTest(classes = AppStarter.class)
@RunWith(SpringRunner.class)
public class TestController {

    @Autowired
    private PdaController pdaController;

    @Test
    public void testUser(){
        Result result = pdaController.userList(1);
        Result result1 = pdaController.deptList(1);
        System.out.println("成功");
    }
}
