package cn.cuiot.dmp.system.application.service.initialize;

import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.application.service.DataSyncService;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyRelationDTO;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import cn.cuiot.dmp.system.infrastructure.entity.*;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionMapper;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionSettingMapper;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyCacheConstant.*;

/**
 * 同步企业常用选项（自定义选项、交易方式、收费项目）
 *
 * @Author: zc
 * @Date: 2024-11-06
 */
@Slf4j
@Component
public class CommonOptionSyncService extends DataSyncService<CommonOptionTypeEntity> {

    @Resource
    private CommonOptionTypeMapper commonOptionTypeMapper;
    @Resource
    private CommonOptionMapper commonOptionMapper;
    @Resource
    private CommonOptionSettingMapper commonOptionSettingMapper;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<CommonOptionTypeEntity> fetchData(Long sourceCompanyId) {
        List<CommonOptionTypeEntity> list = commonOptionTypeMapper.selectList(
                new LambdaQueryWrapper<CommonOptionTypeEntity>()
                        .eq(CommonOptionTypeEntity::getCompanyId, sourceCompanyId));
        if (CollectionUtils.isEmpty(list)) {
            log.error("企业初始化【常用选项-其他】异常，模板企业不存在数据");
        }
        return list;
    }

    @Override
    public List<SyncCompanyRelationDTO<CommonOptionTypeEntity>> preprocessData(List<CommonOptionTypeEntity> data, Long targetCompanyId) {

        Map<Long, FormConfigTypeTreeNodeVO> map = buildFormMap(data);

        return data.stream().map(item -> {
            FormConfigTypeTreeNodeVO nodeVO = map.get(item.getId());
            CommonOptionTypeEntity entity = new CommonOptionTypeEntity();
            entity.setId(Long.parseLong(nodeVO.getId()));
            entity.setParentId(Long.parseLong(nodeVO.getParentId()));
            entity.setName(item.getName());
            entity.setCategory(item.getCategory());
            entity.setLevelType(item.getLevelType());
            entity.setCompanyId(targetCompanyId);
            entity.setPathName(item.getPathName());
            entity.setDeletedFlag(0);

            return new SyncCompanyRelationDTO<>(entity, item.getId());
        }).collect(Collectors.toList());
    }

    @Override
    public void saveData(List<SyncCompanyRelationDTO<CommonOptionTypeEntity>> data, Long targetCompanyId) {
        data.forEach(item -> commonOptionTypeMapper.insert(item.getEntity()));
    }

    /**
     * 构造表单新老数据对照
     */
    public static Map<Long, FormConfigTypeTreeNodeVO> buildFormMap(List<CommonOptionTypeEntity> data) {
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList = data.stream()
                .map(parent -> new FormConfigTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<FormConfigTypeTreeNodeVO> tree = TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        Map<Long, FormConfigTypeTreeNodeVO> map = Maps.newHashMap();
        updateIds(tree, null, map);
        return map;
    }

    /**
     * 递归修改表单id
     */
    private static void updateIds(List<FormConfigTypeTreeNodeVO> nodes, String parentId, Map<Long, FormConfigTypeTreeNodeVO> map) {
        for (FormConfigTypeTreeNodeVO node : nodes) {
            // 更新当前节点的 ID 和 Parent ID
            Long oldId = Long.parseLong(node.getId());
            String newId = String.valueOf(IdWorker.getId());
            node.setId(newId);
            if (StringUtils.isNotBlank(parentId)) {
                node.setParentId(parentId);
            }

            // 添加到 map 中，并清空子节点引用
            map.put(oldId, new FormConfigTypeTreeNodeVO(newId, node.getParentId(), node.getName(), node.getLevelType(), node.getCompanyId()));

            // 递归更新子节点
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                updateIds(node.getChildren(), newId, map);
            }
        }
    }

    @Override
    public void syncAssociatedData(List<SyncCompanyRelationDTO<CommonOptionTypeEntity>> targetData, SyncCompanyDTO dto) {

        Long sourceCompanyId = dto.getSourceCompanyId();
        Long targetCompanyId = dto.getTargetCompanyId();

        List<CommonOptionEntity> formConfigEntities = commonOptionMapper.selectList(
                new LambdaQueryWrapper<CommonOptionEntity>()
                        .eq(CommonOptionEntity::getCompanyId, sourceCompanyId));

        if (CollectionUtils.isNotEmpty(formConfigEntities)) {
            // 需要缓存的数据
            List<SyncCompanyRelationDTO<CommonOptionEntity>> companyBeans = Lists.newArrayList();
            List<SyncCompanyRelationDTO<CommonOptionSettingEntity>> settingCompanyBeans = Lists.newArrayList();

            Map<Long, CommonOptionTypeEntity> map = targetData.stream().collect(Collectors.toMap(SyncCompanyRelationDTO::getOldId, SyncCompanyRelationDTO::getEntity));

            formConfigEntities.forEach(item -> {
                Long oldId = item.getId();
                CommonOptionEntity entity = new CommonOptionEntity();
                entity.setId(IdWorker.getId());
                entity.setName(item.getName());
                entity.setCompanyId(targetCompanyId);
                entity.setTypeCategory(item.getTypeCategory());
                entity.setStatus(item.getStatus());
                entity.setTypeId(map.get(item.getTypeId()).getId());
                entity.setDeletedFlag(item.getDeletedFlag());

                // 保存表单配置-常用选项表
                commonOptionMapper.insert(entity);
                companyBeans.add(new SyncCompanyRelationDTO<>(entity, oldId));

                // 保存表单配置-常用选项设置表
                List<CommonOptionSettingEntity> settingEntities = commonOptionSettingMapper.selectList(
                        new LambdaQueryWrapper<CommonOptionSettingEntity>()
                                .eq(CommonOptionSettingEntity::getCommonOptionId, item.getId()));
                if (CollectionUtils.isNotEmpty(settingEntities)) {
                    List<CommonOptionSettingEntity> collect = settingEntities.stream().map(settingItem -> {
                        Long setOldId = settingItem.getId();
                        CommonOptionSettingEntity settingEntity = new CommonOptionSettingEntity();
                        settingEntity.setId(IdWorker.getId());
                        settingEntity.setSort(settingItem.getSort());
                        settingEntity.setName(settingItem.getName());
                        settingEntity.setCommonOptionId(entity.getId());
                        settingEntity.setDeletedFlag(settingItem.getDeletedFlag());

                        settingCompanyBeans.add(new SyncCompanyRelationDTO<>(settingEntity, setOldId));
                        return settingEntity;
                    }).collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(collect)) {
                        commonOptionSettingMapper.batchSaveCommonOptionSettings(collect);
                    }
                }
            });
            if (CollectionUtils.isNotEmpty(companyBeans)) {
                redisUtil.set(COMPANY_INITIALIZE + targetCompanyId + ":" + COMMON_OPTION, JsonUtil.writeValueAsString(companyBeans), Const.ONE_DAY_SECOND);
            }
            if (CollectionUtils.isNotEmpty(settingCompanyBeans)) {
                redisUtil.set(COMPANY_INITIALIZE + targetCompanyId + ":" + COMMON_OPTION_SETTING, JsonUtil.writeValueAsString(settingCompanyBeans), Const.ONE_DAY_SECOND);
            }
        }
    }
}
