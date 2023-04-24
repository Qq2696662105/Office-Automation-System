package com.dataKing.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataKing.common.result.Result;
import com.dataKing.model.process.ProcessType;
import com.dataKing.process.service.OaProcessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: OaProcessTypeController
 * Package: com.dataKing.process.controller
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 10:06
 * @Version 1.0
 */

@Api(tags = "审批流程类型")
@RestController
@RequestMapping("/admin/process/processType")
public class OaProcessTypeController {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;


    @ApiOperation(value = "获取全部审批分类")
    @GetMapping("findAll")
    public Result findAll() {
        return Result.ok(oaProcessTypeService.list());
    }

    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation("查询所有类型")
    @GetMapping("{page}/{limit}")
    public Result getProcessType(@PathVariable Long page,
                                 @PathVariable Long limit){
        //封装Page类
        Page<ProcessType> typePage = new Page<>(page, limit);
        //调用service方法查询
        Page<ProcessType> processTypePage = oaProcessTypeService.page(typePage);

        return Result.ok(processTypePage);
    }

    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation("根据id获取对应的类型信息")
    @GetMapping("/get/{id}")
    public Result getInfoById(@PathVariable Long id){

        ProcessType processType = oaProcessTypeService.getById(id);

        return Result.ok(processType);
    }

    @PreAuthorize("hasAuthority('bnt.processType.add')")
    @ApiOperation("增加流程类型")
    @PostMapping("/save")
    public Result sava(@RequestBody ProcessType processType){

        oaProcessTypeService.save(processType);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @ApiOperation("修改流程类型")
    @PutMapping("/update")
    public Result update(@RequestBody ProcessType processType){

        oaProcessTypeService.updateById(processType);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.processType.remove')")
    @ApiOperation("删除流程类型")
    @DeleteMapping("/remove/{id}")
    public Result delete(@PathVariable Long id){

        oaProcessTypeService.removeById(id);
        return Result.ok();
    }


}
