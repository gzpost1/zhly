package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.BeanMapper;
import cn.cuiot.dmp.common.utils.StreamUtil;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionSetting;
import cn.cuiot.dmp.system.domain.repository.CommonOptionSettingRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionSettingEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionSettingMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Slf4j
@Repository
public class CommonOptionSettingRepositoryImpl implements CommonOptionSettingRepository {

    @Autowired
    private CommonOptionSettingMapper commonOptionSettingMapper;

    @Override
    public List<CommonOptionSetting> batchQueryCommonOptionSettings(Long commonOptionId) {
        LambdaQueryWrapper<CommonOptionSettingEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionSettingEntity>()
                .eq(CommonOptionSettingEntity::getCommonOptionId, commonOptionId)
                .orderByAsc(CommonOptionSettingEntity::getSort);
        List<CommonOptionSettingEntity> commonOptionSettingEntities = commonOptionSettingMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(commonOptionSettingEntities)) {
            return new ArrayList<>();
        }
        return commonOptionSettingEntities.stream()
                .map(o -> {
                    CommonOptionSetting commonOptionSetting = new CommonOptionSetting();
                    BeanUtils.copyProperties(o, commonOptionSetting);
                    return commonOptionSetting;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveOrUpdateCommonOptionSettings(Long commonOptionId, List<CommonOptionSetting> commonOptionSettings) {
        if (Objects.isNull(commonOptionId) || CollectionUtils.isEmpty(commonOptionSettings)) {
            return;
        }
        checkSave(commonOptionSettings);
        LambdaQueryWrapper<CommonOptionSettingEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionSettingEntity>()
                .eq(CommonOptionSettingEntity::getCommonOptionId, commonOptionId);
        List<CommonOptionSettingEntity> commonOptionSettingEntities = commonOptionSettingMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(commonOptionSettingEntities)) {
            batchSaveCommonOptionSettings(commonOptionId, commonOptionSettings);
        } else {
            batchUpdateCommonOptionSettings(commonOptionId, commonOptionSettings, commonOptionSettingEntities);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteCommonOptionSettings(List<Long> commonOptionIdList) {
        LambdaQueryWrapper<CommonOptionSettingEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionSettingEntity>()
                .in(CommonOptionSettingEntity::getCommonOptionId, commonOptionIdList);
        List<CommonOptionSettingEntity> commonOptionSettingEntities = commonOptionSettingMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(commonOptionSettingEntities)) {
            return;
        }
        commonOptionSettingMapper.deleteBatchIds(commonOptionSettingEntities.stream()
                .map(CommonOptionSettingEntity::getId)
                .collect(Collectors.toList()));
    }

    @Override
    public List<CommonOptionSetting> batchQueryCommonOptionSettingsByIds(List<Long> ids) {
        List<CommonOptionSettingEntity> entityList = commonOptionSettingMapper.selectBatchIds(ids);
        return BeanMapper.mapList(entityList, CommonOptionSetting.class);
    }

    private void batchSaveCommonOptionSettings(Long commonOptionId, List<CommonOptionSetting> commonOptionSettings) {
        List<CommonOptionSettingEntity> commonOptionSettingEntities = commonOptionSettings.stream()
                .map(o -> {
                    CommonOptionSettingEntity commonOptionSettingEntity = new CommonOptionSettingEntity();
                    BeanUtils.copyProperties(o, commonOptionSettingEntity);
                    commonOptionSettingEntity.setId(IdWorker.getId());
                    commonOptionSettingEntity.setCommonOptionId(commonOptionId);
                    return commonOptionSettingEntity;
                })
                .collect(Collectors.toList());
        commonOptionSettingMapper.batchSaveCommonOptionSettings(commonOptionSettingEntities);
    }

    private void batchUpdateCommonOptionSettings(Long commonOptionId, List<CommonOptionSetting> commonOptionSettings,
                                                 List<CommonOptionSettingEntity> oldCommonOptionSettingEntities) {
        // id为空的数据说明为新增数据，直接新增
        List<CommonOptionSettingEntity> newCommonOptionSettingEntities = commonOptionSettings.stream()
                .filter(o -> Objects.isNull(o.getId()))
                .map(o -> {
                    CommonOptionSettingEntity commonOptionSettingEntity = new CommonOptionSettingEntity();
                    BeanUtils.copyProperties(o, commonOptionSettingEntity);
                    commonOptionSettingEntity.setId(IdWorker.getId());
                    commonOptionSettingEntity.setCommonOptionId(commonOptionId);
                    return commonOptionSettingEntity;
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newCommonOptionSettingEntities)) {
            commonOptionSettingMapper.batchSaveCommonOptionSettings(newCommonOptionSettingEntities);
        }
        // id不为空说明为更新数据，直接更新
        List<CommonOptionSettingEntity> updateCommonOptionSettingEntities = commonOptionSettings.stream()
                .filter(o -> Objects.nonNull(o.getId()))
                .map(o -> {
                    CommonOptionSettingEntity commonOptionSettingEntity = new CommonOptionSettingEntity();
                    BeanUtils.copyProperties(o, commonOptionSettingEntity);
                    commonOptionSettingEntity.setCommonOptionId(commonOptionId);
                    return commonOptionSettingEntity;
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(updateCommonOptionSettingEntities)) {
            updateCommonOptionSettingEntities.forEach(o -> commonOptionSettingMapper.updateById(o));
        }
        // 数据库存在但传入参数不存在则说明为删除数据，软删除
        List<CommonOptionSettingEntity> deleteCommonOptionSettingEntities = oldCommonOptionSettingEntities.stream()
                .filter(old -> !commonOptionSettings.stream()
                        .map(CommonOptionSetting::getId)
                        .collect(Collectors.toList())
                        .contains(old.getId()))
                .peek(o -> o.setDeletedFlag(Integer.valueOf(EntityConstants.DELETED)))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deleteCommonOptionSettingEntities)) {
            commonOptionSettingMapper.deleteBatchIds(deleteCommonOptionSettingEntities.stream()
                    .map(CommonOptionSettingEntity::getId)
                    .collect(Collectors.toList()));
        }
    }

    private void checkSave(List<CommonOptionSetting> commonOptionSettings) {
        List<CommonOptionSetting> commonOptionDistinctSettings = new ArrayList<>();
        commonOptionSettings.stream()
                .filter(StreamUtil.distinctByKey(CommonOptionSetting::getName))
                .forEach(commonOptionDistinctSettings::add);
        AssertUtil.isTrue(commonOptionSettings.size() == commonOptionDistinctSettings.size(),
                "常用选项存在重复选项");
    }

}
