package com.dataKing.process.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.model.process.ProcessTemplate;
import com.dataKing.model.process.ProcessType;
import com.dataKing.process.mapper.OaProcessTemplateMapper;
import com.dataKing.process.service.OaProcessTemplateService;
import com.dataKing.process.service.OaProcessTypeService;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * ClassName: OaProcessTemplateServiceImpl
 * Package: com.dataKing.process.service.impl
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 10:41
 * @Version 1.0
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    @Autowired
    private RepositoryService repositoryService;

    //获取分页内容，并把ProcessTemplate类中的processTypeName属性获取出来
    @Override
    public Page<ProcessTemplate> listAndProcessTypeName(Page<ProcessTemplate> templatePage) {

        //1.先获取分页内容
        Page<ProcessTemplate> processTemplatePage = this.page(templatePage);
        //2.将分页内容转换成List集合
        List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();
        //3.遍历集合，填充每个ProcessTemplate元素的processTypeName属性
        for (ProcessTemplate processTemplate : processTemplateList){
            ProcessType processType = oaProcessTypeService.getById(processTemplate.getProcessTypeId());
            processTemplate.setProcessTypeName(processType.getName());
        }
        //4.封装成Page类型
        //processTemplateList.
        return processTemplatePage;
    }

    @Override
    public void publish(Long id) {
        //修改发布状态，1为已发布，0为未发布
        ProcessTemplate processTemplate = this.getById(id);
        processTemplate.setStatus(1);
        this.updateById(processTemplate);
        //TODO 部署流程定义，后续完善
        if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())){
            this.deployByZip(processTemplate.getProcessDefinitionPath());
        }
    }

    //部署流程定义
    @Override
    public void deployByZip(String deployPath) {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(deployPath);

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());

    }
}
