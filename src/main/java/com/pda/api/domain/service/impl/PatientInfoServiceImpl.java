package com.pda.api.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pda.api.domain.entity.PatientInfo;
import com.pda.api.domain.service.IPatientInfoService;
import com.pda.api.mapper.primary.PatientInfoMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-07-31
 */
@Service
public class PatientInfoServiceImpl extends ServiceImpl<PatientInfoMapper, PatientInfo> implements IPatientInfoService {

}
