package com.pda.api.controller;

import cn.hutool.core.util.XmlUtil;
import com.pda.api.dto.CostReqDto;
import com.pda.api.dto.PatientAllergyReqDto;
import com.pda.api.dto.PatientReqDto;
import com.pda.api.service.*;
import com.pda.common.Result;
import com.pda.utils.CxfClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2022-07-22 12:32
 * @Created by AlanZhang
 */
@RestController
public class PdaController {

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private WardBedService wardBedService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private CostService costService;
    @Autowired
    private AdviceService adviceService;
    @Autowired
    private DrugService drugService;
    @Autowired
    private CheckService checkService;

    @GetMapping("/user/list/{pageNum}")
    public Result userList(@PathVariable("pageNum") Integer pageNum){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(userService.list(pageNum));
        return Result.success(stringObjectMap);
    }

    @GetMapping("/dept/list/{pageNum}")
    public Result deptList(@PathVariable("pageNum") Integer pageNum){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(deptService.list(pageNum));
        return Result.success(stringObjectMap);
    }

    @PostMapping("/patient/info")
    public Result wardBed(@RequestBody PatientReqDto patientReqDto){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(patientService.findPatientInfo(patientReqDto));
        return Result.success(stringObjectMap);
    }

    @PostMapping("/patient/hospitalization/info")
    public Result patientInfo(@RequestBody PatientReqDto patientReqDto){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(patientService.fintPatientInhInfo(patientReqDto));
        return Result.success(stringObjectMap);
    }

    @GetMapping("/ward/bed/{pageNum}")
    public Result wardBed(@PathVariable("pageNum") Integer pageNum){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(wardBedService.findWardBed(pageNum));
        return Result.success(stringObjectMap);
    }

    @GetMapping("/cost/info/{patientNo}/{pageNum}")
    public Result costInfo(@PathVariable("pageNum") Integer pageNum ,@PathVariable("patientNo") String patientNo){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(costService.fintCostInfo(pageNum,patientNo));
        return Result.success(stringObjectMap);
    }

    @GetMapping("/patient/allergy/{patientNo}/{pageNum}")
    public Result patientAllergy(@PathVariable("pageNum") Integer pageNum ,@PathVariable("patientNo") String patientNo){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(patientService.fintAllergyInfo(pageNum,patientNo));
        return Result.success(stringObjectMap);
    }

    /**
     * 目前返回空串
     * @param pageNum
     * @param patientNo
     * @param upTime
     * @return
     */
    @GetMapping("/patient/tz/{patientNo}/{upTime}/{pageNum}")
    public Result patientTz(@PathVariable("pageNum") Integer pageNum ,@PathVariable("patientNo") String patientNo,@PathVariable("upTime") Integer upTime){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(patientService.fintTzInfo(pageNum,patientNo,upTime));
        return Result.success(stringObjectMap);
    }

    /**
     * 返回字符串没有内容
     * @param pageNum
     * @return
     */
    @GetMapping("/advice/{pageNum}")
    public Result adviceInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(adviceService.fintAdviceInfo(pageNum));
        return Result.success(stringObjectMap);
    }

    /**
     * 返回空串
     * @param pageNum
     * @return
     */
    @GetMapping("/advice/handle/{pageNum}")
    public Result adviceHandleInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(adviceService.fintAdviceHandelInfo(pageNum));
        return Result.success(stringObjectMap);
    }

    @GetMapping("/advice/usage/{pageNum}")
    public Result adviceUsageInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(adviceService.fintAdviceUsageInfo(pageNum));
        return Result.success(stringObjectMap);
    }

    @GetMapping("/advice/frequency/{pageNum}")
    public Result adviceFrequencyInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(adviceService.fintAdviceFrequencyInfo(pageNum));
        return Result.success(stringObjectMap);
    }

    /**
     * 返回空串
     * @param pageNum
     * @return
     */
    @GetMapping("/drugmerchine/{pageNum}")
    public Result drugInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(drugService.fintDrugmerchineInfo(pageNum));
        return Result.success(stringObjectMap);
    }

    /**
     * 返回空串
     * @param pageNum
     * @return
     */
    @GetMapping("/distribution/{pageNum}")
    public Result distributionInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(drugService.findDistributionInfo(pageNum));
        return Result.success(stringObjectMap);
    }

    /**
     * 入参不能为空
     * @param pageNum
     * @return
     */
    @GetMapping("/checkout/{pageNum}")
    public Result checkoutInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(checkService.findCheckoutInfo(pageNum));
        return Result.success(stringObjectMap);
    }

    @GetMapping("/check/{pageNum}")
    public Result checkInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(checkService.findCheckInfo(pageNum));
        return Result.success(stringObjectMap);
    }

    /**
     * 入参不能为空
     * @param pageNum
     * @return
     */
    @GetMapping("/check/applyinfo/{pageNum}")
    public Result checkApplyInfo(@PathVariable("pageNum") Integer pageNum ){
        Map<String, Object> stringObjectMap = XmlUtil.xmlToMap(checkService.findCheckApplyInfo(pageNum));
        return Result.success(stringObjectMap);
    }


}
