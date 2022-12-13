package com.pda.api.service;

import com.pda.api.dto.base.BaseKeyValueDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Classname DeptService
 * @Description TODO
 * @Date 2022-07-22 14:21
 * @Created by AlanZhang
 */
public interface DeptService {
    String list(Integer pageNum);

    List<BaseKeyValueDto> findAll();
}
