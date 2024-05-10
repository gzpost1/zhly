package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.base.infrastructure.constants.OperationConstant;
import cn.cuiot.dmp.base.infrastructure.dto.BatcheOperation;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.*;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.FormOperates;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.enums.AssignedUserType;
import cn.cuiot.dmp.baseconfig.flow.feign.SystemToFlowService;
import cn.cuiot.dmp.baseconfig.flow.mapper.TbFlowConfigMapper;
import cn.cuiot.dmp.baseconfig.flow.utils.JsonUtil;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.baseconfig.flow.utils.BpmnModelUtils.*;

@Service
public class TbFlowConfigService extends ServiceImpl<TbFlowConfigMapper, TbFlowConfig> {
    @Resource
    private RepositoryService repositoryService;
    @Autowired
    private TbFlowConfigOrgService flowConfigOrgService;
    @Autowired
    private SystemToFlowService systemToFlowService;


    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    public IPage<TbFlowPageDto> queryForPage(TbFlowConfigQuery query) {
        IPage<TbFlowPageDto> tbFlowPageDtoIPage = baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
        //填充每一行的组织机构ID
        if (Objects.nonNull(tbFlowPageDtoIPage) && CollectionUtils.isNotEmpty(tbFlowPageDtoIPage.getRecords())) {
            List<Long> dataIds = tbFlowPageDtoIPage.getRecords().stream().map(TbFlowPageDto::getId).collect(Collectors.toList());
            Map<Long, String> orgNameMap = flowConfigOrgService.queryOrgNameByFlowConfigIds(dataIds);
            if (Objects.nonNull(orgNameMap)) {
                tbFlowPageDtoIPage.getRecords().forEach(e -> {
                    if (orgNameMap.containsKey(e.getId())) {
                        List<Long> orgIds = Arrays.stream(StringUtils.split(orgNameMap.get(e.getId()), ","))
                                .map(idstr -> Long.valueOf(idstr)).collect(Collectors.toList());
                        e.setOrgIds(orgIds);
                    }
                });
            }

        }
        return tbFlowPageDtoIPage;
    }

    /**
     * 保存流程
     *
     * @param createDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFlow(FlowEngineInsertDto createDto) {
        TbFlowConfig tbFlowConfig = new TbFlowConfig();
        tbFlowConfig.setName(createDto.getName());
        tbFlowConfig.setBusinessTypeId(createDto.getBusinessTypeId());
        tbFlowConfig.setProcess(createDto.getProcess());
        tbFlowConfig.setLogo(createDto.getLogo());
        tbFlowConfig.setNotifySetting(createDto.getNotifySetting());
        tbFlowConfig.setRemark(createDto.getRemark());
        tbFlowConfig.setCommonConfigDto(createDto.getCommonConfigDto());
        tbFlowConfig.setStatus(EntityConstants.ENABLED);
        tbFlowConfig.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        tbFlowConfig.setId(IdWorker.getId());

        //处理json
        String processJson = createDto.getProcess();
        //填充每个节点的表单内容
        ChildNode childNode = processJson(processJson);
        processJson = JsonUtil.writeValueAsString(childNode);
        tbFlowConfig.setProcess(processJson);

        //保存流程和组织的中间表
        List<Long> orgIds = createDto.getOrgId();
        flowConfigOrgService.saveFlowOrg(tbFlowConfig.getId(), orgIds);



        //获取发起人配置信息
        UserInfo userInfo = childNode.getProps().getAssignedUser().get(0);
        AssertUtil.notNull(userInfo, "发起人配置信息不能为空");

        Byte flowTypeCode = AssignedUserType.getFlowTypeCode(userInfo.getType());
        if (flowTypeCode == null) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "发起人类型不能为空");
        }
        tbFlowConfig.setAssignedUserType(flowTypeCode);
        tbFlowConfig.setAssignedUserId(userInfo.getId());


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("processJson", processJson);
        BpmnModel bpmnModel = assemBpmnModel(jsonObject, childNode, createDto.getRemark(), createDto.getName(), StringUtils.join(createDto.getOrgId(), ","),
                tbFlowConfig.getId().toString());
        repositoryService.createDeployment()
                .addBpmnModel(createDto.getName() + ".bpmn", bpmnModel)
                .name(createDto.getName())
                .category(createDto.getOrgId() + "")
                .deploy();

        save(tbFlowConfig);
    }

    public ChildNode processJson(String processJson){
        ChildNode childNode = JSONObject.parseObject(processJson, new TypeReference<ChildNode>() {
        });
        processChildNode(childNode);
        return childNode;
    }

    public void processChildNode(ChildNode childNode){
        if(Objects.nonNull(childNode.getProps()) && CollectionUtils.isNotEmpty(childNode.getProps().getFormIds()){
            List<Long> formIds = childNode.getProps().getFormIds();

            FormConfigReqDTO formConfigReqDTO = new FormConfigReqDTO();
            formConfigReqDTO.setIdList(formIds);
            List<FormConfigRspDTO> formConfigRspDTOS = systemToFlowService.batchQueryFormConfig(formConfigReqDTO);
            AssertUtil.notEmpty(formConfigRspDTOS, "表单配置信息为空");

            List<FormOperates> formItems = formConfigRspDTOS.stream().map(e -> {
                FormOperates formOperates = new FormOperates();
                BeanUtils.copyProperties(e, formOperates);
                return formOperates;
            }).collect(Collectors.toList());

            childNode.getProps().setFormPerms(formItems);

            if(Objects.nonNull(childNode.getChildren())){
                processChildNode(childNode.getChildren());
            }
        }
    }


    /**
     * 组装bpmnModel
     *
     * @param jsonObject
     * @param childNode
     * @param remark
     * @param formName
     * @param groupId
     * @param templateId
     * @return
     */
    private BpmnModel assemBpmnModel(JSONObject jsonObject, ChildNode childNode, String remark,
                                     String formName, String groupId, String templateId) {
        BpmnModel bpmnModel = new BpmnModel();
        List<SequenceFlow> sequenceFlows = Lists.newArrayList();
        Map<String, ChildNode> childNodeMap = new HashMap<>();
        bpmnModel.setTargetNamespace(groupId);
        ExtensionAttribute extensionAttribute = new ExtensionAttribute();
        extensionAttribute.setName("yunjintech");
        extensionAttribute.setNamespace("http://flowable.org/bpmn");
        extensionAttribute.setValue(jsonObject.toJSONString());

        Process process = new Process();
        process.setId(WorkFlowConstants.PROCESS_PREFIX + templateId);
        process.setName(formName);
        process.setDocumentation(remark);
        process.addAttribute(extensionAttribute);
        bpmnModel.addProcess(process);

        StartEvent startEvent = createStartEvent();
        process.addFlowElement(startEvent);
        String lastNode = null;
        try {
            lastNode = create(startEvent.getId(), childNode, process, bpmnModel, sequenceFlows, childNodeMap);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "操作失败");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "操作失败");
        }
        EndEvent endEvent = createEndEvent();
        process.addFlowElement(endEvent);
        process.addFlowElement(connect(lastNode, endEvent.getId(), sequenceFlows, childNodeMap, process));

        List<FlowableListener> executionListeners = new ArrayList<>();
        FlowableListener flowableListener = new FlowableListener();
        flowableListener.setEvent(ExecutionListener.EVENTNAME_END);
        flowableListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        flowableListener.setImplementation("${processListener}");
        executionListeners.add(flowableListener);
        process.setExecutionListeners(executionListeners);
//        new BpmnAutoLayout(bpmnModel).execute();
        return bpmnModel;
    }

    /**
     * 更新流程
     *
     * @param updateDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateFlow(FlowEngineUpdateDto updateDto) {
        TbFlowConfig config = this.getById(updateDto.getId());
        AssertUtil.notNull(config, "找不到流程配置信息");
        BeanUtils.copyProperties(updateDto, config);

        //处理json
        String processJson = updateDto.getProcess();
        //填充每个节点的表单ID
        ChildNode childNode = processJson(processJson);
        processJson = JsonUtil.writeValueAsString(childNode);
        config.setProcess(processJson);

        this.updateById(config);

        //修改流程和组织的中间表
        flowConfigOrgService.updateFlowOrg(updateDto.getId(), updateDto.getOrgId());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("processJson", updateDto.getProcess());
        BpmnModel bpmnModel = assemBpmnModel(jsonObject, childNode, updateDto.getRemark(), updateDto.getName(), StringUtils.join(updateDto.getOrgId(), ","),
                updateDto.getId().toString());
        repositoryService.createDeployment()
                .addBpmnModel(updateDto.getName() + ".bpmn", bpmnModel)
                .name(updateDto.getName())
                .category(updateDto.getOrgId() + "")
                .deploy();
    }

    /**
     * 更新状态
     *
     * @param updateStatusParam
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(UpdateStatusParam updateStatusParam) {
        TbFlowConfig entity = this.getById(updateStatusParam.getId());
        entity.setStatus(updateStatusParam.getStatus());
        this.updateById(entity);
    }

    /**
     * 批量操作
     *
     * @param batcheOperation
     */
    public void batchedOperation(BatcheOperation batcheOperation) {
        if (Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.DELETE)) {
            //删除
            this.delete(batcheOperation.getIds());
        } else if (Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.STATUS)) {
            //修改状态
            LambdaUpdateWrapper<TbFlowConfig> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(TbFlowConfig::getId, batcheOperation.getIds());
            updateWrapper.set(TbFlowConfig::getStatus, batcheOperation.getOperaToStatus());
            this.update(updateWrapper);
        } else if (Objects.equals(batcheOperation.getOperaTionType(), OperationConstant.MOVE)) {
            //移动业务类型
            LambdaUpdateWrapper<TbFlowConfig> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(TbFlowConfig::getId, batcheOperation.getIds());
            updateWrapper.set(TbFlowConfig::getBusinessTypeId, batcheOperation.getOperaToStatus());
            this.update(updateWrapper);
        }
    }

    /**
     * 删除流程配置
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        this.removeByIds(ids);

        //删除流程和组织的中间表
        flowConfigOrgService.deleteByFlowConfigIds(ids);
    }

    /**
     * 查询流程配置详情
     *
     * @param id
     * @return
     */
    public FlowEngineVo queryForDetail(Long id) {
        TbFlowConfig tbFlowConfig = this.getById(id);
        AssertUtil.notNull(tbFlowConfig, "找不到流程配置信息");
        FlowEngineVo flowEngineVo = new FlowEngineVo();
        BeanUtils.copyProperties(tbFlowConfig, flowEngineVo);
        List<Long> orgIds = flowConfigOrgService.queryOrgIdsByFlowConfigId(id);
        flowEngineVo.setOrgId(orgIds);
        return flowEngineVo;
    }
}
