package com.dataKing.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dataKing.model.process.ProcessRecord;

/**
 * ClassName: OaProcessRecordService
 * Package: com.dataKing.process.service
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/20 0020 16:33
 * @Version 1.0
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {

    void addRecord(Long processId, String description, Integer status);
}
