package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.system.domain.aggregate.CustomConfigDetail;
import cn.cuiot.dmp.system.domain.repository.CustomConfigDetailRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CustomConfigDetailEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CustomConfigDetailMapper;
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
 * @date 2024/5/19
 */
@Slf4j
@Repository
public class CustomConfigDetailRepositoryImpl implements CustomConfigDetailRepository {

    @Autowired
    private CustomConfigDetailMapper customConfigDetailMapper;

    @Override
    public List<CustomConfigDetail> batchQueryCustomConfigDetails(Long customConfigId) {
        LambdaQueryWrapper<CustomConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigDetailEntity>()
                .eq(CustomConfigDetailEntity::getCustomConfigId, customConfigId);
        List<CustomConfigDetailEntity> customConfigDetailEntities = customConfigDetailMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(customConfigDetailEntities)) {
            return new ArrayList<>();
        }
        return customConfigDetailEntities.stream()
                .map(o -> {
                    CustomConfigDetail customConfigDetail = new CustomConfigDetail();
                    BeanUtils.copyProperties(o, customConfigDetail);
                    return customConfigDetail;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveOrUpdateCustomConfigDetails(Long customConfigId, List<CustomConfigDetail> customConfigDetails) {
        if (Objects.isNull(customConfigId) || CollectionUtils.isEmpty(customConfigDetails)) {
            return;
        }
        LambdaQueryWrapper<CustomConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigDetailEntity>()
                .eq(CustomConfigDetailEntity::getCustomConfigId, customConfigId);
        List<CustomConfigDetailEntity> customConfigDetailEntities = customConfigDetailMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(customConfigDetailEntities)) {
            batchSaveCustomConfigDetails(customConfigId, customConfigDetails);
        } else {
            batchUpdateCustomConfigDetails(customConfigId, customConfigDetails, customConfigDetailEntities);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteCustomConfigDetails(List<Long> customConfigIdList) {
        LambdaQueryWrapper<CustomConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigDetailEntity>()
                .in(CustomConfigDetailEntity::getCustomConfigId, customConfigIdList);
        List<CustomConfigDetailEntity> CustomConfigDetailEntities = customConfigDetailMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(CustomConfigDetailEntities)) {
            return;
        }
        customConfigDetailMapper.deleteBatchIds(CustomConfigDetailEntities.stream()
                .map(CustomConfigDetailEntity::getId)
                .collect(Collectors.toList()));
    }

    private void batchSaveCustomConfigDetails(Long customConfigId, List<CustomConfigDetail> customConfigDetails) {
        List<CustomConfigDetailEntity> CustomConfigDetailEntities = customConfigDetails.stream()
                .map(o -> {
                    CustomConfigDetailEntity customConfigDetailEntity = new CustomConfigDetailEntity();
                    BeanUtils.copyProperties(o, customConfigDetailEntity);
                    customConfigDetailEntity.setId(IdWorker.getId());
                    customConfigDetailEntity.setCustomConfigId(customConfigId);
                    return customConfigDetailEntity;
                })
                .collect(Collectors.toList());
        customConfigDetailMapper.batchSaveCustomConfigDetails(CustomConfigDetailEntities);
    }

    private void batchUpdateCustomConfigDetails(Long customConfigId, List<CustomConfigDetail> customConfigDetails,
                                                List<CustomConfigDetailEntity> oldCustomConfigDetailEntities) {
        // id为空的数据说明为新增数据，直接新增
        List<CustomConfigDetailEntity> newCustomConfigDetailEntities = customConfigDetails.stream()
                .filter(o -> Objects.isNull(o.getId()))
                .map(o -> {
                    CustomConfigDetailEntity customConfigDetailEntity = new CustomConfigDetailEntity();
                    BeanUtils.copyProperties(o, customConfigDetailEntity);
                    customConfigDetailEntity.setId(IdWorker.getId());
                    customConfigDetailEntity.setCustomConfigId(customConfigId);
                    return customConfigDetailEntity;
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newCustomConfigDetailEntities)) {
            customConfigDetailMapper.batchSaveCustomConfigDetails(newCustomConfigDetailEntities);
        }
        // id不为空说明为更新数据，直接更新
        List<CustomConfigDetailEntity> updateCustomConfigDetailEntities = customConfigDetails.stream()
                .filter(o -> Objects.nonNull(o.getId()))
                .map(o -> {
                    CustomConfigDetailEntity customConfigDetailEntity = new CustomConfigDetailEntity();
                    BeanUtils.copyProperties(o, customConfigDetailEntity);
                    customConfigDetailEntity.setCustomConfigId(customConfigId);
                    return customConfigDetailEntity;
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(updateCustomConfigDetailEntities)) {
            updateCustomConfigDetailEntities.forEach(o -> customConfigDetailMapper.updateById(o));
        }
        // 数据库存在但传入参数不存在则说明为删除数据，软删除
        List<CustomConfigDetailEntity> deleteCustomConfigDetailEntities = oldCustomConfigDetailEntities.stream()
                .filter(old -> !customConfigDetails.stream()
                        .map(CustomConfigDetail::getId)
                        .collect(Collectors.toList())
                        .contains(old.getId()))
                .peek(o -> o.setDeletedFlag(Integer.valueOf(EntityConstants.DELETED)))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deleteCustomConfigDetailEntities)) {
            customConfigDetailMapper.deleteBatchIds(deleteCustomConfigDetailEntities.stream()
                    .map(CustomConfigDetailEntity::getId)
                    .collect(Collectors.toList()));
        }
    }

}
