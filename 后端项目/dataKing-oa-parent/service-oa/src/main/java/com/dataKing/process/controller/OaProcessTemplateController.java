package com.dataKing.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dataKing.common.result.Result;
import com.dataKing.model.process.ProcessTemplate;
import com.dataKing.process.service.OaProcessTemplateService;
import io.netty.util.internal.ResourcesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: OaProcessTemplateController
 * Package: com.dataKing.process.controller
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 10:42
 * @Version 1.0
 */


@Api("流程模板管理")
@RestController
@RequestMapping("/admin/process/processTemplate")
public class OaProcessTemplateController {

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;



    @ApiOperation("分页获取流程模板")
    @GetMapping("{page}/{limit}")
    public Result getProcessTemplate(@PathVariable Long page,@PathVariable Long limit){

        Page<ProcessTemplate> templatePage = new Page<>(page, limit);

        //获取分页内容，并把ProcessTemplate类中的processTypeName属性获取出来
        Page<ProcessTemplate> processTemplatePage = oaProcessTemplateService.listAndProcessTypeName(templatePage);

        return Result.ok(processTemplatePage);
    }

    @ApiOperation("根据id获取模板信息")
    @GetMapping("/get/{id}")
    public Result getProcessTemplateById(@PathVariable Long id){

        ProcessTemplate processTemplateServiceById = oaProcessTemplateService.getById(id);

        return Result.ok(processTemplateServiceById);
    }

    @ApiOperation("增加模板")
    @PostMapping("/save")
    public Result save(@RequestBody ProcessTemplate processTemplate){

        oaProcessTemplateService.save(processTemplate);
        return Result.ok();
    }

    @ApiOperation("修改模板")
    @PutMapping("/update")
    public Result update(@RequestBody ProcessTemplate processTemplate){

        oaProcessTemplateService.updateById(processTemplate);

        return Result.ok();
    }

    @ApiOperation("删除模板")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){

        oaProcessTemplateService.removeById(id);
        return Result.ok();
    }

    @ApiOperation("上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {

        //获取process文件夹的绝对路径
        String path = new File(ResourceUtils
                .getURL("classpath:")
                .getPath()).getAbsolutePath();
        //设置上传目录
        File tempFile = new File(path + "/processes/");
        //判断该目录是否已经存在
        if (!tempFile.exists()){
            tempFile.mkdirs();//创建目录
        }
        //创建空文件实现文件写入
        String fileName = file.getOriginalFilename();
        File zipFile = new File(tempFile+"/"+fileName);

        //保存文件
        try {
            file.transferTo(zipFile);
        } catch (IOException e) {
            return Result.fail("文件上传失败");
        }

        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + fileName);
        map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));
        return Result.ok(map);

    }

    @ApiOperation("流程发布")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id){

        oaProcessTemplateService.publish(id);
        return Result.ok();
    }



}
