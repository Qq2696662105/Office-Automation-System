package com.dataKing.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.auth.service.SysUserService;
import com.dataKing.model.process.ProcessRecord;
import com.dataKing.model.system.SysUser;
import com.dataKing.process.mapper.OaProcessRecordMapper;
import com.dataKing.process.service.OaProcessRecordService;
import com.dataKing.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: OaProcessRecordServiceImpl
 * Package: com.dataKing.process.service.impl
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/20 0020 16:33
 * @Version 1.0
 */

@Service
public class OaProcessRecordServiceImpl extends ServiceImpl<OaProcessRecordMapper, ProcessRecord> implements OaProcessRecordService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void addRecord(Long processId, String description, Integer status) {

        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);

        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setDescription(description);
        processRecord.setStatus(status);
        processRecord.setOperateUser(sysUser.getName());
        processRecord.setOperateUserId(userId);
        this.save(processRecord);
    }
}
