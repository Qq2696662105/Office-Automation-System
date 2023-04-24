package com.dataKing.process.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dataKing.auth.service.SysUserService;
import com.dataKing.model.process.Process;
import com.dataKing.model.process.ProcessRecord;
import com.dataKing.model.process.ProcessTemplate;
import com.dataKing.model.system.SysUser;
import com.dataKing.process.mapper.OaProcessMapper;
import com.dataKing.process.service.OaProcessRecordService;
import com.dataKing.process.service.OaProcessService;
import com.dataKing.process.service.OaProcessTemplateService;
import com.dataKing.security.custom.LoginUserInfoHelper;
import com.dataKing.vo.process.ApprovalVo;
import com.dataKing.vo.process.ProcessFormVo;
import com.dataKing.vo.process.ProcessQueryVo;
import com.dataKing.vo.process.ProcessVo;
import com.sun.javafx.tk.Toolkit;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: OaProcessServiceImpl
 * Package: com.dataKing.process.service.impl
 * Description:
 *
 * @Author dataKing
 * @Create 2023/4/19 0019 16:28
 * @Version 1.0
 */

@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OaProcessRecordService oaProcessRecordService;


    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> processPage,
                                       ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> pageModel = baseMapper.selectPage(processPage,processQueryVo);
        return pageModel;
    }

    @Autowired
    private HistoryService historyService;

    @Override
    public void startup(ProcessFormVo processFormVo) {
        //1 根据当前用户id获取用户信息
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        //2 根据审批模板id把模板信息查询出来
        ProcessTemplate processTemplate = oaProcessTemplateService.getById(processFormVo.getProcessTemplateId());

        //3 保存提交审批信息到业务表，oa_process
        Process process = new Process();
        BeanUtils.copyProperties(processFormVo,process);
        String processCode = System.currentTimeMillis() + "";
        process.setProcessCode(processCode);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName()+"发起"+processTemplate.getName()+"申请");
        process.setStatus(1);
        this.save(process);
        //4 启动流程实例 - RuntimeService
        //4.1 流程定义key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        //4.2 业务key   oa_process表中的主键id
        String processId = process.getId()+"";
        //4.3 流程参数 form表单json数据，转换成map集合
        Map<String,Object> dataMap = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(processFormVo.getFormValues());
        JSONObject formData = jsonObject.getJSONObject("formData");
        for (Map.Entry<String,Object> entry:formData.entrySet()){
            dataMap.put(entry.getKey(),entry.getValue());
        }
        Map<String,Object> variables = new HashMap<>();
        variables.put("data",dataMap);
        //
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, processId, variables);
        //业务表关联当前流程实例id
        process.setProcessInstanceId(processInstance.getId());

        //5 查询下一个审批人    可能有多个（并行审批） 并行审批可能返回多个任务，即返回List集合
        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        List<String> assigneeList = new ArrayList<>();
        for (Task task:taskList){
            String assignee = task.getAssignee();
            SysUser sysUserByUsername = sysUserService.getByUsername(assignee);
            String name = sysUserByUsername.getName();
            assigneeList.add(name);
            //6 TODO 推送消息
        }
        process.setDescription("等待"+ StringUtils.join(assigneeList.toArray(),",")+"审批");

        //更新
        this.updateById(process);
        //添加记录表数据
        oaProcessRecordService.addRecord(process.getId(),sysUser.getName()+"发起申请",process.getStatus());

    }

    @Override
    public IPage<ProcessVo> findPending(Page<Process> pageObject) {
        //1 封装查询条件，根据当前的用户名称
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();
        //2 调用方法分页条件查询，返回list集合，待办任务集合
        //第一个参数开始位置，第二个参数每页记录数
        int begin = (int)((pageObject.getCurrent()-1)*(pageObject.getSize()));
        int size = (int)pageObject.getSize();
        List<Task> taskList = taskQuery.listPage(begin, size);
        long totalCount = taskQuery.count();

        //3 封装返回list集合数据到List<ProcessVo>里面
        //List<Task> --> List<ProcessVo>
        List<ProcessVo> processVoList = new ArrayList<>();
        for (Task task:taskList) {
            //从task中获取流程实例
            String processInstanceId = task.getProcessInstanceId();
            //通过流程实例得到oa_process表中的对象
            Process process = this.getOne(new LambdaQueryWrapper<Process>().eq(Process::getProcessInstanceId, processInstanceId));
            if (process==null){
                continue;
            }
            //将得到的Process对象转换成ProcessVo对象
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVo.setTaskId(task.getId());
            processVoList.add(processVo);
        }

        //4 封装返回Ipage对象
        Page<ProcessVo> retPage = new Page<>(pageObject.getCurrent(), pageObject.getSize(), totalCount);
        retPage.setRecords(processVoList);
        return retPage;
    }

    @Override
    public Map<String, Object> show(Long id) {

        //根据id获取oa_process表中的信息
        Process process = this.getById(id);
        //获取process_record记录信息
        List<ProcessRecord> recordList = oaProcessRecordService.list(new LambdaQueryWrapper<ProcessRecord>()
                .eq(ProcessRecord::getProcessId, process.getId()));
        //获取该申请详情的模板信息
        ProcessTemplate processTemplate = oaProcessTemplateService.getById(process.getProcessTemplateId());

        //判断当前用户是否有审批权限
        boolean isApprove = false;
        for (Task task : this.getCurrentTaskList(process.getProcessInstanceId())) {
            if (task.getAssignee().equals(LoginUserInfoHelper.getUsername())){
                isApprove = true;
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", recordList);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);
        return map;

    }

    @Override
    public void approve(ApprovalVo approvalVo) {

        //先获取任务id，根据任务id获取流程变量
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);
        for (Map.Entry<String,Object> entry : variables.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        //判断审批状态值
        if (approvalVo.getStatus().intValue() == 1){
            //审批通过
            //如果有流程变量就传入
            Map<String,Object> variable = new HashMap<>();
            taskService.complete(taskId,variable);
        }else {
            //审批失败
            this.endTask(taskId);
        }
        //记录审批相关过程process_record
        String username = LoginUserInfoHelper.getUsername();
        SysUser user = sysUserService.getByUsername(username);
        String description = approvalVo.getStatus().intValue()==1 ? user.getName()+"审批通过" : username+"审批驳回";
        oaProcessRecordService.addRecord(approvalVo.getProcessId(),
                description,
                approvalVo.getStatus());

        //查询下一个审批人，更新流程表记录process表记录
        Process process = this.getById(approvalVo.getProcessId());
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (!CollectionUtils.isEmpty(taskList)){
            List<String> assigneeList = new ArrayList<>();
            for (Task task:taskList){
                String assignee = task.getAssignee();
                SysUser sysUser = sysUserService.getByUsername(assignee);
                String name = sysUser.getName();
                assigneeList.add(name);
                // TODO 通知下一个人
            }
            process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
            process.setStatus(1);
        }else {
            if (approvalVo.getStatus().intValue() == 1){
                process.setDescription("审批完成（同意）");
                process.setStatus(2);
            }else {
                process.setDescription("审批完成（驳回）");
                process.setStatus(-1);
            }
        }
        //更新oa_process表数据
        this.updateById(process);



    }

    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> objectPage) {
        //从historyService中取得当前用户处理过的任务
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished().orderByTaskCreateTime().desc();
        //通过封装条件进行分页查询
        int begin = (int)((objectPage.getCurrent()-1)*(objectPage.getSize()));
        int size = (int)objectPage.getSize();
        List<HistoricTaskInstance> historicTaskInstanceList = query.listPage(begin, size);
        //将HistoricTaskInstance类型转换成ProcessVo类型
        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance historicTaskInstance:historicTaskInstanceList){
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            Process process = this.getOne(new LambdaQueryWrapper<Process>()
                    .eq(Process::getProcessInstanceId, processInstanceId));
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVoList.add(processVo);
        }
        //将List<ProcessVo>  -->  Ipage<ProcessVo>
        IPage<ProcessVo> iPage = new Page<>(objectPage.getCurrent(),objectPage.getSize());
        iPage.setRecords(processVoList);
        return iPage;
    }

    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        IPage<ProcessVo> iPage = this.selectPage(pageParam, processQueryVo);
        return iPage;
    }

    private void endTask(String taskId) {
        //  当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        // 并行任务可能为null
        if(CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());

    }

    private List<Task> getCurrentTaskList(String processInstanceId) {

        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }
}
