package cn.cuiot.dmp.baseconfig.custommenu.service;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.BusinessTypeSyncDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.FormConfigSyncDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.base.application.service.impl.ApiSystemServiceImpl;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskConfig;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskInfo;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskOrg;
import cn.cuiot.dmp.baseconfig.custommenu.entity.TbFlowTaskOrgMapper;
import cn.cuiot.dmp.baseconfig.custommenu.mapper.TbFlowTaskConfigMapper;
import cn.cuiot.dmp.baseconfig.custommenu.mapper.TbFlowTaskInfoMapper;
import cn.cuiot.dmp.baseconfig.custommenu.vo.FlowTaskInfoVo;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.JsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
public class FlowTaskConfigSyncService extends DataSyncService<TbFlowTaskConfig> {

    @Resource
    private TbFlowTaskConfigMapper flowTaskConfigMapper;
    @Resource
    private TbFlowTaskInfoMapper flowTaskInfoMapper;
    @Resource
    private TbFlowTaskOrgMapper flowTaskOrgMapper;
    @Autowired
    private ApiSystemServiceImpl apiSystemService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<TbFlowTaskConfig> fetchData(Long sourceCompanyId) {
        List<TbFlowTaskConfig> list = flowTaskConfigMapper.selectList(
                new LambdaQueryWrapper<TbFlowTaskConfig>()
                        .eq(TbFlowTaskConfig::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【任务配置】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<TbFlowTaskConfig>> preprocessData(List<TbFlowTaskConfig> data, Long targetCompanyId) {
        Map<Long, BusinessTypeSyncDTO> businessTypeMap = getBusinessType(targetCompanyId);
        List<SyncCompanyRelationDTO<TbFlowTaskConfig>> collect = data.stream().map(item -> {
            Long oldId = item.getId();
            TbFlowTaskConfig entity = new TbFlowTaskConfig();
            entity.setId(IdWorker.getId());
            entity.setName(item.getName());

            // 设置业务类型
            Long businessTypeId = item.getBusinessTypeId();
            if (Objects.nonNull(businessTypeId)) {
                entity.setBusinessTypeId(
                        Optional.ofNullable(businessTypeMap.get(businessTypeId))
                                .map(BusinessTypeSyncDTO::getId)
                                .orElse(null)
                );
            }

            entity.setRemark(item.getRemark());
            entity.setStatus(item.getStatus());
            entity.setCompanyId(targetCompanyId);
            return new SyncCompanyRelationDTO<>(entity, oldId);
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(collect)) {
            redisUtil.set(COMPANY_INITIALIZE + targetCompanyId + ":" + TASK_CONFIG, JsonUtil.writeValueAsString(collect), Const.ONE_DAY_SECOND);
        }

        return collect;
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<TbFlowTaskConfig>> data, Long targetCompanyId) {
        data.forEach(item -> flowTaskConfigMapper.insert(item.getEntity()));
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<TbFlowTaskConfig>> targetData, SyncCompanyDTO dto) {
        Long targetCompanyId = dto.getTargetCompanyId();
        // 获取orgId
        DepartmentDto departmentInfo = apiSystemService.lookUpDepartmentInfo(null, null, targetCompanyId);

        // 保存任务配置组织机构关联数据
        List<TbFlowTaskOrg> collect = targetData.stream().map(e -> {
            TbFlowTaskOrg entity = new TbFlowTaskOrg();
            entity.setOrgId(departmentInfo.getId());
            entity.setFlowTaskId(e.getEntity().getId());
            return entity;
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(collect)) {
            flowTaskOrgMapper.insertList(collect);
        }

        // 需要缓存的数据
        List<SyncCompanyRelationDTO<TbFlowTaskInfo>> flowTaskInfoList = Lists.newArrayList();

        // 缓存获取表单数据
        Map<Long, FormConfigSyncDTO> formConfigMap = getFormConfig(targetCompanyId);
        // 保存
        targetData.forEach(item -> {
            List<FlowTaskInfoVo> list = flowTaskInfoMapper.queryByTaskConfigId(item.getOldId());

            if (CollectionUtils.isNotEmpty(list)) {
                List<TbFlowTaskInfo> collect1 = list.stream().map(e -> {
                    Long oldId = e.getId();
                    TbFlowTaskInfo entity = new TbFlowTaskInfo();
                    entity.setId(IdWorker.getId());
                    entity.setName(e.getName());
                    entity.setEquipmentType(e.getEquipmentType());

                    // 设置表单id
                    Long formId = e.getFormId();
                    if (Objects.nonNull(formId)) {
                        entity.setFormId(
                                Optional.ofNullable(formConfigMap.get(formId))
                                        .map(FormConfigSyncDTO::getId)
                                        .orElse(null));
                    }

                    entity.setTaskConfigId(item.getEntity().getId());
                    entity.setTaskId(e.getTaskId());
                    entity.setSort(e.getSort());

                    flowTaskInfoList.add(new SyncCompanyRelationDTO<>(entity, oldId));
                    return entity;
                }).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(collect1)) {
                    flowTaskInfoMapper.insertList(collect1);
                }
            }
        });

        if (CollectionUtils.isNotEmpty(flowTaskInfoList)) {
            redisUtil.set(COMPANY_INITIALIZE + targetCompanyId + ":" + FLOW_TASK_INFO, JsonUtil.writeValueAsString(flowTaskInfoList), Const.ONE_DAY_SECOND);
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
            List<SyncCompanyRelationDTO<FormConfigSyncDTO>> syncCompanyRelationDtos = JsonUtil.readValue(redisJson,
                    new TypeReference<List<SyncCompanyRelationDTO<FormConfigSyncDTO>>>() {
                    });
            if (CollectionUtils.isNotEmpty(syncCompanyRelationDtos)) {
                return syncCompanyRelationDtos.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));
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

    @Override
    public void cleanSyncData(SyncCompanyDTO dto){
        List<TbFlowTaskConfig> taskConfigList = flowTaskConfigMapper.selectList(
                new LambdaQueryWrapper<TbFlowTaskConfig>()
                        .eq(TbFlowTaskConfig::getCompanyId, dto.getTargetCompanyId()));
        if (CollectionUtils.isNotEmpty(taskConfigList)) {
            List<Long> ids = taskConfigList.stream().map(TbFlowTaskConfig::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ids)) {
                // 删除任务详情
                flowTaskInfoMapper.batchDeleteByTaskConfigIds(ids);
                // 删除关联表
                flowTaskOrgMapper.batchDeleteByTaskConfigIds(ids);
            }
            // 删除任务配置
            flowTaskConfigMapper.batchDeleteByIds(ids);
        }
    }

}
