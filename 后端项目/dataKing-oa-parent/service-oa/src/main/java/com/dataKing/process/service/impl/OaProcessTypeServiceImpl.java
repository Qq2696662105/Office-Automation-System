package com.dataKing.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.model.process.ProcessTemplate;
import com.dataKing.model.process.ProcessType;
import com.dataKing.process.mapper.OaProcessTypeMapper;
import com.dataKing.process.service.OaProcessTemplateService;
import com.dataKing.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: OaProcessTypeServiceImpl
 * Package: com.dataKing.process.service.impl
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 10:04
 * @Version 1.0
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @Override
    public List<ProcessType> findProcessType() {

        List<ProcessType> processTypeList = this.list();

        for(ProcessType processType : processTypeList){
            List<ProcessTemplate> processTemplateList = oaProcessTemplateService.list(new LambdaQueryWrapper<ProcessTemplate>()
                    .eq(ProcessTemplate::getProcessTypeId, processType.getId()));
            if (processType.getProcessTemplateList()==null){
                processType.setProcessTemplateList(processTemplateList);
            }

        }

        return processTypeList;
    }
}
