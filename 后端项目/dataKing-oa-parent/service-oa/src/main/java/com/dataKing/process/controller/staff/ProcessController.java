package com.dataKing.process.controller.staff;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataKing.auth.service.SysUserService;
import com.dataKing.common.result.Result;
import com.dataKing.model.process.Process;
import com.dataKing.model.process.ProcessTemplate;
import com.dataKing.model.process.ProcessType;
import com.dataKing.process.service.OaProcessService;
import com.dataKing.process.service.OaProcessTemplateService;
import com.dataKing.process.service.OaProcessTypeService;
import com.dataKing.vo.process.ApprovalVo;
import com.dataKing.vo.process.ProcessFormVo;
import com.dataKing.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ClassName: ProcessController
 * Package: com.dataKing.process.controller.staff
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 20:41
 * @Version 1.0
 */

@Api("员工端接口")
@RestController
@RequestMapping("/admin/process")
@CrossOrigin    //解决跨域问题
public class ProcessController {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @Autowired
    private OaProcessService oaProcessService;

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("启动实例")
    @PostMapping("/startUp")
    public Result startup(@RequestBody ProcessFormVo processFormVo) {
        oaProcessService.startup(processFormVo);
        return Result.ok();
    }


    @ApiOperation("获取模板的样式")
    @GetMapping("getProcessTemplate/{processTempleteId}")
    public Result getProcessTemplate(@PathVariable Long processTempleteId) {

        ProcessTemplate processTemplate = oaProcessTemplateService.getById(processTempleteId);
        return Result.ok(processTemplate);
    }

    @ApiOperation("获取每个类型中所有的流程")
    @GetMapping("findProcessType")
    public Result findProcessType() {

        List<ProcessType> processTypeList = oaProcessTypeService.findProcessType();
        return Result.ok(processTypeList);
    }

    @ApiOperation("查找待审批的任务列表")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable Long page,@PathVariable Long limit){

        Page<Process> pageObject = new Page<>(page,limit);
        IPage<ProcessVo> processVoIPage = oaProcessService.findPending(pageObject);

        return Result.ok(processVoIPage);
    }

    @ApiOperation("查看待审批的详情信息")
    @GetMapping("/show/{id}")
    public Result show(@PathVariable Long id){
        Map<String,Object> map = oaProcessService.show(id);
        return Result.ok(map);
    }

    @ApiOperation("审批通过或拒绝")
    @PostMapping("/approve")
    public Result approve(@RequestBody ApprovalVo approvalVo){
        oaProcessService.approve(approvalVo);
        return Result.ok();
    }

    @ApiOperation("查询已审批")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(@PathVariable Long page,@PathVariable Long limit){
        Page<Process> objectPage = new Page<>(page, limit);
        IPage<ProcessVo> iPage = oaProcessService.findProcessed(objectPage);
        return Result.ok(iPage);
    }

    @ApiOperation(value = "查询已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = oaProcessService.findStarted(pageParam);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取当前用户基本信息")
    @GetMapping("/getCurrentUser")
    public Result getCurrentUser() {
        Map<String,Object> map = sysUserService.getCurrentUser();
        return Result.ok(map);
    }
}
