package com.dataKing.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataKing.common.result.Result;
import com.dataKing.model.process.Process;
import com.dataKing.process.service.OaProcessService;
import com.dataKing.vo.process.ProcessQueryVo;
import com.dataKing.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: OaProcessController
 * Package: com.dataKing.process.controller
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 16:29
 * @Version 1.0
 */
@Api("审批管理列表")
@RestController
@RequestMapping("/admin/process")
public class OaProcessController {

    @Autowired
    private OaProcessService oaProcessService;


    //需要关联其他三张表，查出Name的信息，且模糊查询相关信息
    @ApiOperation("分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageList(@PathVariable Long page,
                           @PathVariable Long limit,
                           ProcessQueryVo processQueryVo){
        Page<ProcessVo> processPage = new Page<>(page, limit);

        IPage<ProcessVo> processPageret = oaProcessService.selectPage(processPage,processQueryVo);

        return Result.ok(processPageret);
    }




}
