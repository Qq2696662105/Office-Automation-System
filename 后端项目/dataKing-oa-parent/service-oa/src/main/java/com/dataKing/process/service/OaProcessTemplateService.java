package com.dataKing.process.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dataKing.model.process.ProcessTemplate;

/**
 * ClassName: OaProcessTemplateService
 * Package: com.dataKing.process.service
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 10:41
 * @Version 1.0
 */
public interface OaProcessTemplateService extends IService<ProcessTemplate> {
    //获取分页内容，并把ProcessTemplate类中的processTypeName属性获取出来
    Page<ProcessTemplate> listAndProcessTypeName(Page<ProcessTemplate> templatePage);

    void publish(Long id);

    void deployByZip(String deployPath);
}
