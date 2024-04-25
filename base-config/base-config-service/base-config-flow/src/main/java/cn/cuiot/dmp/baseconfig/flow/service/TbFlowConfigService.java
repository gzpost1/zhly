package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.baseconfig.flow.dto.FlowEngineInsertDto;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowConfigQuery;
import cn.cuiot.dmp.baseconfig.flow.dto.TbFlowPageDto;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.enums.AssignedUserType;
import cn.cuiot.dmp.baseconfig.flow.mapper.TbFlowConfigMapper;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants;
import org.flowable.bpmn.BpmnAutoLayout;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.cuiot.dmp.baseconfig.flow.utils.BpmnModelUtils.*;

@Service
public class TbFlowConfigService extends ServiceImpl<TbFlowConfigMapper, TbFlowConfig> {
    @Resource
    private RepositoryService repositoryService;

    /**
     * 分页查询
     * @param query
     * @return
     */
    public IPage<TbFlowPageDto> queryForPage(TbFlowConfigQuery query) {
        return baseMapper.queryForPage(new Page(query.getCurrentPage(), query.getPageSize()), query);
    }

    /**
     * 保存流程
     * @param createDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFlow(FlowEngineInsertDto createDto) {
        TbFlowConfig tbFlowConfig = new TbFlowConfig();
        tbFlowConfig.setName(createDto.getName());
        tbFlowConfig.setBusinessTypeId(createDto.getBusinessTypeId());
        tbFlowConfig.setOrgId(createDto.getOrgId());
        tbFlowConfig.setProcess(createDto.getProcess());
        tbFlowConfig.setLogo(createDto.getLogo());
        tbFlowConfig.setNotifySetting(createDto.getNotifySetting());
        tbFlowConfig.setRemark(createDto.getRemark());
        tbFlowConfig.setCommonConfigDto(createDto.getCommonConfigDto());
        tbFlowConfig.setStatus(EntityConstants.ENABLED);
        tbFlowConfig.setId(IdWorker.getId());

        //处理json
        String processJson = createDto.getProcess();
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>(){});
        //获取发起人配置信息
        UserInfo userInfo = childNode.getProps().getAssignedUser().get(0);
        AssertUtil.notNull(userInfo,"发起人配置信息不能为空");

        Byte flowTypeCode = AssignedUserType.getFlowTypeCode(userInfo.getType());
        if (flowTypeCode == null) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "发起人类型不能为空");
        }
        tbFlowConfig.setAssignedUserType(flowTypeCode);
        tbFlowConfig.setAssignedUserId(userInfo.getId());


        JSONObject jsonObject=new JSONObject();
        jsonObject.put("processJson", processJson);
        BpmnModel bpmnModel = assemBpmnModel(jsonObject, childNode, createDto.getRemark(), createDto.getName(), createDto.getOrgId().toString(),
                tbFlowConfig.getId().toString());
        repositoryService.createDeployment()
                .addBpmnModel(createDto.getName() + ".bpmn", bpmnModel)
                .name(createDto.getName())
                .category(createDto.getOrgId() + "")
                .deploy();

        save(tbFlowConfig);
    }

    /**
     * 组装bpmnModel
     * @param jsonObject
     * @param childNode
     * @param remark
     * @param formName
     * @param groupId
     * @param templateId
     * @return
     */
    private BpmnModel assemBpmnModel(JSONObject jsonObject, ChildNode childNode, String remark,
                                     String formName, String groupId, String templateId)
    {
        BpmnModel bpmnModel =new BpmnModel();
        List<SequenceFlow> sequenceFlows = Lists.newArrayList();
        Map<String,ChildNode> childNodeMap=new HashMap<>();
        bpmnModel.setTargetNamespace(groupId);
        ExtensionAttribute extensionAttribute=new ExtensionAttribute();
        extensionAttribute.setName("yunjintech");
        extensionAttribute.setNamespace("http://flowable.org/bpmn");
        extensionAttribute.setValue(jsonObject.toJSONString());

        Process process =new Process();
        process.setId(WorkFlowConstants.PROCESS_PREFIX+templateId);
        process.setName(formName);
        process.setDocumentation(remark);
        process.addAttribute(extensionAttribute);
        bpmnModel.addProcess(process);

        StartEvent startEvent = createStartEvent();
        process.addFlowElement(startEvent);
        String lastNode = null;
        try {
            lastNode = create(startEvent.getId(), childNode,process,bpmnModel,sequenceFlows,childNodeMap);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL,"操作失败");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL,"操作失败");
        }
        EndEvent endEvent = createEndEvent();
        process.addFlowElement(endEvent);
        process.addFlowElement(connect(lastNode, endEvent.getId(),sequenceFlows,childNodeMap,process));

        List<FlowableListener> executionListeners =new ArrayList<>();
        FlowableListener flowableListener=new FlowableListener();
        flowableListener.setEvent(ExecutionListener.EVENTNAME_END);
        flowableListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        flowableListener.setImplementation("${processListener}");
        executionListeners.add(flowableListener);
        process.setExecutionListeners(executionListeners);
        new BpmnAutoLayout(bpmnModel).execute();
        return bpmnModel;
    }
}
