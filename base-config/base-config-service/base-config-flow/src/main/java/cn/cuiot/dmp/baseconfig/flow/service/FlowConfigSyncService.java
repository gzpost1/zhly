package cn.cuiot.dmp.baseconfig.flow.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.*;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.base.application.service.impl.ApiSystemServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.baseconfig.custommenu.dto.FormObjectOperates;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskConfigVo;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskInfoVo;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.*;
import cn.cuiot.dmp.baseconfig.flow.dto.flowjson.Properties;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfig;
import cn.cuiot.dmp.baseconfig.flow.entity.TbFlowConfigOrg;
import cn.cuiot.dmp.baseconfig.flow.mapper.TbFlowConfigMapper;
import cn.cuiot.dmp.baseconfig.flow.mapper.TbFlowConfigOrgMapper;
import cn.cuiot.dmp.common.utils.JsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyCacheConstant.*;

/**
 * 同步企业任务配置
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class FlowConfigSyncService extends DataSyncService<TbFlowConfig> {

    @Resource
    private TbFlowConfigMapper flowConfigMapper;
    @Resource
    private TbFlowConfigOrgMapper flowConfigOrgMapper;
    @Autowired
    private ApiSystemServiceImpl apiSystemService;
    @Autowired
    private RedisUtil redisUtil;

    private static final String ASSIGNED_USER = "assignedUser";

    @Override
    public List<TbFlowConfig> fetchData(Long sourceCompanyId) {
        List<TbFlowConfig> list = flowConfigMapper.selectList(
                new LambdaQueryWrapper<TbFlowConfig>()
                        .eq(TbFlowConfig::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【流程配置】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<TbFlowConfig>> data, Long targetCompanyId) {
        data.forEach(item -> flowConfigMapper.insert(item.getEntity()));
    }

    @Override
    public List<SyncCompanyRelationDTO<TbFlowConfig>> preprocessData(List<TbFlowConfig> data, Long targetCompanyId) {

        // 业务类型
        Map<Long, BusinessTypeSyncDTO> businessTypeMap = getBusinessType(targetCompanyId);

        SyncCompanyParamDTO dto = new SyncCompanyParamDTO();
        dto.setTargetCompanyId(targetCompanyId);
        dto.setDepartmentInfo(apiSystemService.lookUpDepartmentInfo(null, null, targetCompanyId));
        dto.setFormConfigMap(getFormConfig(targetCompanyId));
        dto.setTaskConfigMap(getTaskConfig(targetCompanyId));
        dto.setTaskInfoMap(getTaskInfo(targetCompanyId));
        dto.setFormConfigDetailMap(getFormConfigDetail(targetCompanyId));
        dto.setCustomConfigMap(getCustomConfig(targetCompanyId));

        return data.stream().map(item -> {
            Long oldId = item.getId();
            TbFlowConfig entity = new TbFlowConfig();
            entity.setId(IdWorker.getId());
            entity.setName(item.getName());
            if (Objects.nonNull(item.getBusinessTypeId()) && businessTypeMap.containsKey(item.getBusinessTypeId())) {
                entity.setBusinessTypeId(businessTypeMap.get(item.getBusinessTypeId()).getId());
            }
            entity.setProcess(processJson(item.getProcess(), dto));
            entity.setLogo(item.getLogo());
            entity.setNotifySetting(item.getNotifySetting());
            entity.setRemark(item.getRemark());
            entity.setCommonConfigDto(item.getCommonConfigDto());
            entity.setStatus(item.getStatus());
            entity.setCompanyId(targetCompanyId);
            entity.setAssignedUserType(item.getAssignedUserType());
            entity.setAssignedUserId(item.getAssignedUserId());
            entity.setIsSelectAppUser(item.getIsSelectAppUser());
            return new SyncCompanyRelationDTO<>(entity, oldId);
        }).collect(Collectors.toList());
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<TbFlowConfig>> targetData, SyncCompanyDTO dto) {

        Long targetCompanyId = dto.getTargetCompanyId();

        // 获取orgId
        DepartmentDto departmentInfo = apiSystemService.lookUpDepartmentInfo(null, null, targetCompanyId);
        Long deptId = departmentInfo.getId();
        // 保存任务配置组织机构关联数据
        List<TbFlowConfigOrg> collect = targetData.stream().map(e -> {
            TbFlowConfigOrg entity = new TbFlowConfigOrg();
            entity.setOrgId(deptId);
            entity.setFlowConfigId(e.getEntity().getId());
            return entity;
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(collect)) {
            flowConfigOrgMapper.insertList(collect);
        }
    }

    /**
     * 流程json处理
     *
     * @return ChildNode
     * @Param processJson 参数
     */
    public String processJson(String processJson, SyncCompanyParamDTO dto) {
        if (StringUtils.isNotBlank(processJson)) {
            ChildNode childNode = JsonUtil.readValue(processJson, new TypeReference<ChildNode>() {
            });
            if (Objects.nonNull(childNode)) {
                processChildNode(childNode, dto);
                return JsonUtil.writeValueAsString(childNode);
            }
        }
        return null;
    }

    public void processChildNode(ChildNode childNode, SyncCompanyParamDTO dto) {

        Properties props = childNode.getProps();

        Long targetCompanyId = dto.getTargetCompanyId();
        DepartmentDto departmentInfo = dto.getDepartmentInfo();
        Map<Long, FlowTaskInfoSyncDTO> taskInfoMap = dto.getTaskInfoMap();
        Map<Long, FormConfigDetailSyncDTO> formConfigDetailMap = dto.getFormConfigDetailMap();
        Map<Long, Long> taskConfigMap = dto.getTaskConfigMap();
        Map<Long, CustomConfigDetailSyncDTO> customConfigMap = dto.getCustomConfigMap();
        Map<Long, FormConfigSyncDTO> formConfigMap = dto.getFormConfigMap();

        //处理props数据
        if (Objects.nonNull(props)) {
            // 公共方法用于处理 props.assignedUser 信息
            processAssignedUser(props);
            // 公共方法用于处理 props.nobody 信息
            processNobody(props);
            // 公共方法用于处理 props.formTaskId
            processFormTaskId(props, taskConfigMap);
            // 公共方法用于处理 props.formIds
            processFormIds(props, formConfigMap);
            // 处理props.formPerms数据
            processFormPerms(props, formConfigDetailMap, targetCompanyId);
            // 处理props.taskConfig数据
            processTaskConfig(props, taskConfigMap, departmentInfo, taskInfoMap, formConfigMap);
            // 处理props.ccInfo数据
            processCcInfo(props);
            // 处理处理节点
            processTaskInfo(props, customConfigMap);
        }
        if (Objects.nonNull(childNode.getChildren())) {
            processChildNode(childNode.getChildren(), dto);
        }
    }

    /**
     * 公共方法用于处理 assignedUser 信息
     */
    private void processAssignedUser(Properties props) {
        props.setAssignedUser(new ArrayList<>());
    }

    /**
     * 公共方法用于处理 nobody 信息
     */
    private void processNobody(Properties props) {
        Map<String, Object> map = props.getNobody();
        if (MapUtils.isNotEmpty(map) && map.containsKey(ASSIGNED_USER)) {
            UserInfo info = new UserInfo();
            info.setId(null);
            info.setName(null);
            info.setType(null);
            map.put(ASSIGNED_USER, info);
            props.setNobody(map);
        }
    }

    /**
     * 公共方法用于处理 formTaskId
     */
    private void processFormTaskId(Properties props, Map<Long, Long> taskConfigMap) {
        Long formTaskId = props.getFormTaskId();
        if (Objects.nonNull(formTaskId)) {
            props.setFormTaskId(taskConfigMap.getOrDefault(formTaskId, null));
        }
    }

    /**
     * 公共方法用于处理 formIds
     */
    private void processFormIds(Properties props, Map<Long, FormConfigSyncDTO> formConfigMap) {
        List<Long> formIds = props.getFormIds();
        if (CollectionUtils.isNotEmpty(formIds)) {
            List<Long> collect = formIds.stream()
                    .map(item -> formConfigMap.containsKey(item) ? formConfigMap.get(item).getId() : null)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            props.setFormIds(collect);
        }
    }

    /**
     * 公共方法用于处理 formPerms
     */
    private void processFormPerms(Properties props, Map<Long, FormConfigDetailSyncDTO> formConfigDetailMap, Long targetCompanyId) {
        List<FormOperates> formOperates = props.getFormPerms();
        if (CollectionUtils.isNotEmpty(formOperates)) {
            formOperates.forEach(item -> {
                item.setCompanyId(targetCompanyId);
                if (StringUtils.isNotBlank(item.getFormConfigDetail()) && formConfigDetailMap.containsKey(item.getId())) {
                    item.setFormConfigDetail(formConfigDetailMap.get(item.getId()).getFormConfigDetail());
                }
            });
            props.setFormPerms(formOperates);
        }
    }

    /**
     * 公共方法用于处理 taskConfig
     */
    private void processTaskConfig(Properties props, Map<Long, Long> taskConfigMap, DepartmentDto departmentInfo,
                                   Map<Long, FlowTaskInfoSyncDTO> taskInfoMap, Map<Long, FormConfigSyncDTO> formConfigMap) {
        FlowTaskConfigVo taskConfig = props.getTaskConfig();
        if (Objects.nonNull(taskConfig)) {
            taskConfig.setId(taskConfigMap.getOrDefault(taskConfig.getId(), null));
            taskConfig.setOrgId(Collections.singletonList(departmentInfo.getId()));
            taskConfig.setBusinessTypeId(taskConfig.getBusinessTypeId());

            List<Long> taskInfoFormIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(taskConfig.getTaskInfoList())) {
                taskConfig.getTaskInfoList().forEach(info -> processTaskInfo(info, taskInfoMap, formConfigMap, taskInfoFormIds));
            }
            taskConfig.setTaskMenuIds(taskInfoFormIds);
        }
    }

    /**
     * 公共方法用于处理 taskInfo
     */
    private void processTaskInfo(FlowTaskInfoVo taskInfo, Map<Long, FlowTaskInfoSyncDTO> taskInfoMap,
                                 Map<Long, FormConfigSyncDTO> formConfigMap, List<Long> taskInfoFormIds) {
        // 有缓存则替换，无则设置null
        FlowTaskInfoSyncDTO taskDetail = new FlowTaskInfoSyncDTO();
        if (Objects.nonNull(taskInfo.getId()) && taskInfoMap.containsKey(taskInfo.getId())) {
            taskDetail = taskInfoMap.get(taskInfo.getId());
            // 设置taskInfoFormIds
            taskInfoFormIds.add(taskDetail.getFormId());
        }
        taskInfo.setId(taskDetail.getId());
        taskInfo.setFormId(taskDetail.getFormId());
        taskInfo.setTaskId(taskDetail.getTaskId());
        taskInfo.setTaskConfigId(taskDetail.getTaskConfigId());

        FormObjectOperates formOperates = taskInfo.getFormOperates();
        if (Objects.isNull(formOperates) || !formConfigMap.containsKey(formOperates.getId())) {
            taskInfo.setFormOperates(null);
        } else {
            FormConfigSyncDTO formConfig = formConfigMap.get(formOperates.getId());
            formOperates.setId(formConfig.getId());
            formOperates.setCompanyId(formConfig.getCompanyId());
            formOperates.setTypeId(formConfig.getTypeId());
        }
    }

    /**
     * 处理节点
     */
    private void processTaskInfo(Properties props, Map<Long, CustomConfigDetailSyncDTO> customConfig) {
        String processNodeType = props.getProcessNodeType();
        if (StringUtils.isNotBlank(processNodeType)) {
            props.setProcessNodeType(customConfig.get(Long.parseLong(processNodeType)).getId() + "");
        }
    }

    /**
     * 获取企业表单缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, FormConfigSyncDTO> getFormConfig(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + FORM_CONFIG);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<FormConfigSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<FormConfigSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 公共方法用于处理 CcInfo 信息
     */
    private void processCcInfo(Properties props) {
        CCInfo ccInfo = props.getCcInfo();
        if (Objects.nonNull(ccInfo)) {
            ccInfo.setCcIds(Lists.newArrayList());
        }
    }

    /**
     * 获取表单缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, FormConfigDetailSyncDTO> getFormConfigDetail(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + FORM_CONFIG_DETAIL);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<FormConfigDetailSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<FormConfigDetailSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 获取企业任务配置缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, Long> getTaskConfig(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + TASK_CONFIG);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<FlowTaskConfigSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<FlowTaskConfigSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, e -> e.getEntity().getId()));
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 获取企业任务对象信息缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, FlowTaskInfoSyncDTO> getTaskInfo(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + FLOW_TASK_INFO);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<FlowTaskInfoSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<FlowTaskInfoSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 获取企业表单缓存数据
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, BusinessTypeSyncDTO> getBusinessType(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + BUSINESS_TYPE);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<BusinessTypeSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<BusinessTypeSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }

    /**
     * 获取常用选项->系统选项缓存
     *
     * @return Map
     * @Param targetCompanyId 企业id
     */
    private Map<Long, CustomConfigDetailSyncDTO> getCustomConfig(Long targetCompanyId) {
        String redisJson = redisUtil.get(COMPANY_INITIALIZE + targetCompanyId + ":" + CUSTOM_CONFIG_DETAIL);

        if (StringUtils.isNotBlank(redisJson)) {
            List<SyncCompanyRelationDTO<CustomConfigDetailSyncDTO>> beans = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<CustomConfigDetailSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(beans)) {
                return beans.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
            }
        }
        return Maps.newHashMap();
    }

    @Override
    public void cleanSyncData(SyncCompanyDTO dto) {
        List<TbFlowConfig> flowConfigList = flowConfigMapper.selectList(
                new LambdaQueryWrapper<TbFlowConfig>()
                        .eq(TbFlowConfig::getCompanyId, dto.getTargetCompanyId()));
        if (CollectionUtils.isNotEmpty(flowConfigList)) {
            List<Long> ids = flowConfigList.stream().map(TbFlowConfig::getId).collect(Collectors.toList());
            // 删除配置关联数据
            flowConfigOrgMapper.batchDeleteByFlowConfigIds(ids);
            // 删除流程配置
            flowConfigMapper.batchDeleteByIds(ids);
        }
    }
}
