package com.dataKing.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dataKing.model.process.Process;
import com.dataKing.vo.process.ApprovalVo;
import com.dataKing.vo.process.ProcessFormVo;
import com.dataKing.vo.process.ProcessQueryVo;
import com.dataKing.vo.process.ProcessVo;

import java.util.Map;

/**
 * ClassName: OaProcessService
 * Package: com.dataKing.process.service
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 16:27
 * @Version 1.0
 */
public interface OaProcessService extends IService<Process> {
    IPage<ProcessVo> selectPage(Page<ProcessVo> processPage, ProcessQueryVo processQueryVo);

    void startup(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> pageObject);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> objectPage);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
