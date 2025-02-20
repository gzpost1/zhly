package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.base.infrastructure.constants.OperationConstant;
import cn.cuiot.dmp.base.infrastructure.dto.BatcheOperation;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FormObjectOperates;
import cn.cuiot.dmp.baseconfig.custommenu.service.TbFlowTaskConfigService;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskConfigVo;
import cn.cuiot.dmp.baseconfig.flow.constants.WorkFlowConstants;
import cn.cuiot.dmp.baseconfig.flow.dto.*;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.ChildNode;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.FormOperates;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.UserInfo;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.enums.AssignedUserType;
import cn.cuiot.dmp.baseconfig.flow.feign.SystemToFlowService;
import cn.cuiot.dmp.baseconfig.flow.mapper.TbFlowConfigMapper;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
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
    @Autowired
    private TbFlowTaskConfigService flowTaskConfigService;

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    public IPage<TbFlowPageDto> queryForWorkOrderPage(TbFlowConfigQuery query) {
        IPage<TbFlowPageDto> tbFlowPageDtoIPage = baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
        return tbFlowPageDtoIPage;
    }


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
    public Long saveFlow(FlowEngineInsertDto createDto) {
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
        tbFlowConfig.setIsSelectAppUser(
                Objects.isNull(childNode.getProps().getIsSelectAppUser()) ? Byte.valueOf("0") : childNode.getProps().getIsSelectAppUser());

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
        tbFlowConfig.setProcess(null);
        jsonObject.put("flowconfig", JsonUtil.writeValueAsString(tbFlowConfig));

        BpmnModel bpmnModel = assemBpmnModel(jsonObject, childNode, createDto.getRemark(), createDto.getName(), StringUtils.join(createDto.getOrgId(), ","),
                tbFlowConfig.getId().toString());
        repositoryService.createDeployment()
                .addBpmnModel(createDto.getName() + ".bpmn", bpmnModel)
                .name(createDto.getName())
                .category(createDto.getOrgId() + "")
                .deploy();

        tbFlowConfig.setProcess(processJson);
        save(tbFlowConfig);

        return tbFlowConfig.getId();
    }

    public ChildNode processJson(String processJson) {
        ChildNode childNode = JsonUtil.readValue(processJson, new TypeReference<ChildNode>() {
        });

        processChildNode(childNode);
        return childNode;
    }

    public void processChildNode(ChildNode childNode) {
        //处理任务表单
        if (Objects.nonNull(childNode.getProps()) &&
                (Objects.nonNull(childNode.getProps().getFormTaskId()) || CollectionUtils.isNotEmpty(childNode.getProps().getFormIds()))) {

            List<Long> formIds = childNode.getProps().getFormIds();
            FlowTaskConfigVo flowTaskConfigVo = null;

            if (Objects.nonNull(childNode.getProps().getFormTaskId())) {
                flowTaskConfigVo = flowTaskConfigService.queryForDetail(childNode.getProps().getFormTaskId());
                AssertUtil.notNull(flowTaskConfigVo, "表单任务为空");
                formIds = flowTaskConfigVo.getTaskMenuIds();
            }

            if (CollectionUtils.isNotEmpty(formIds)) {
                FormConfigReqDTO formConfigReqDTO = new FormConfigReqDTO();
                formConfigReqDTO.setIdList(formIds);
                List<FormConfigRspDTO> formConfigRspDTOS = systemToFlowService.batchQueryFormConfig(formConfigReqDTO);
                AssertUtil.isTrue(CollectionUtils.isNotEmpty(formConfigRspDTOS) && formConfigRspDTOS.size() == formIds.size(), "表单配置为空");

                if (Objects.nonNull(flowTaskConfigVo)&& Objects.equals(childNode.getProps().getNodeProcessType(),Byte.parseByte("1")) ) {
                    //填充任务中的每个对象的表单信息
                    flowTaskConfigVo.getTaskInfoList().stream().forEach(e -> {
                        FormConfigRspDTO formConfigRspDTO = formConfigRspDTOS.stream().filter(f -> Objects.equals(f.getId(), e.getFormId())).findFirst().orElseThrow(
                                () -> new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, e.getName() + "对象的表单配置为空"));
                        FormObjectOperates formObjectOperates = new FormObjectOperates();
                        BeanUtils.copyProperties(formConfigRspDTO, formObjectOperates);
                        e.setFormOperates(formObjectOperates);
                    });

                    childNode.getProps().setTaskConfig(flowTaskConfigVo);
                    childNode.getProps().setFormPerms(null);
                } else {
                    childNode.getProps().setFormPerms(formConfigRspDTOS.stream().map(e -> {
                        FormOperates formObjectOperates = new FormOperates();
                        BeanUtils.copyProperties(e, formObjectOperates);
                        return formObjectOperates;
                    }).collect(Collectors.toList()));
                }
            }

            if (Objects.nonNull(childNode.getChildren())) {
                processChildNode(childNode.getChildren());
            }
        } else {
            if (Objects.nonNull(childNode.getProps())) {
                childNode.getProps().setFormPerms(null);
            }
            if (Objects.nonNull(childNode.getChildren())) {
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

        config.setIsSelectAppUser(
                Objects.isNull(childNode.getProps().getIsSelectAppUser()) ? Byte.valueOf("0") : childNode.getProps().getIsSelectAppUser());

        config.setProcess(processJson);

        this.updateById(config);

        //修改流程和组织的中间表
        flowConfigOrgService.updateFlowOrg(updateDto.getId(), updateDto.getOrgId());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("processJson", processJson);
        config.setProcess(null);
        jsonObject.put("flowconfig", JsonUtil.writeValueAsString(config));

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
