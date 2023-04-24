package com.dataKing.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dataKing.model.process.ProcessType;

import java.util.List;

/**
 * ClassName: OaProcessTypeService
 * Package: com.dataKing.process.service
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 10:03
 * @Version 1.0
 */
public interface OaProcessTypeService extends IService<ProcessType> {
    List<ProcessType> findProcessType();
}
