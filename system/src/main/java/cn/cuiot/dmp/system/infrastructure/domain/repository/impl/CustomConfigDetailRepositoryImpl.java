package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.CustomConfigConstant;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.StreamUtil;
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
    public List<CustomConfigDetail> batchQueryCustomConfigDetails(Long customConfigId, Long companyId) {
        LambdaQueryWrapper<CustomConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigDetailEntity>()
                .eq(CustomConfigDetailEntity::getCustomConfigId, customConfigId)
                .eq(CustomConfigDetailEntity::getCompanyId, companyId)
                .orderByAsc(CustomConfigDetailEntity::getSort);
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
    public List<CustomConfigDetail> batchQueryCustomConfigDetails(List<Long> idList) {
        AssertUtil.notEmpty(idList, "自定义配置详情id列表");
        List<CustomConfigDetailEntity> customConfigDetailEntities = customConfigDetailMapper.batchQueryCustomConfigDetails(idList);
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
    public void batchSaveOrUpdateCustomConfigDetails(Long customConfigId, List<CustomConfigDetail> customConfigDetails,
                                                     Long companyId) {
        if (Objects.isNull(customConfigId) || CollectionUtils.isEmpty(customConfigDetails)) {
            return;
        }
        checkSave(customConfigDetails);
        LambdaQueryWrapper<CustomConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigDetailEntity>()
                .eq(CustomConfigDetailEntity::getCustomConfigId, customConfigId)
                .eq(CustomConfigDetailEntity::getCompanyId, companyId);
        List<CustomConfigDetailEntity> customConfigDetailEntities = customConfigDetailMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(customConfigDetailEntities)) {
            batchSaveCustomConfigDetails(customConfigId, customConfigDetails, companyId);
        } else {
            batchUpdateCustomConfigDetails(customConfigId, customConfigDetails, customConfigDetailEntities, companyId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteCustomConfigDetails(List<Long> customConfigIdList, Long companyId) {
        LambdaQueryWrapper<CustomConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigDetailEntity>()
                .in(CustomConfigDetailEntity::getCustomConfigId, customConfigIdList)
                .eq(CustomConfigDetailEntity::getCompanyId, companyId);
        List<CustomConfigDetailEntity> CustomConfigDetailEntities = customConfigDetailMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(CustomConfigDetailEntities)) {
            return;
        }
        customConfigDetailMapper.deleteBatchIds(CustomConfigDetailEntities.stream()
                .map(CustomConfigDetailEntity::getId)
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initCustomConfigDetail(Long companyId) {
        AssertUtil.notNull(companyId, "企业ID不能为空");
        LambdaQueryWrapper<CustomConfigDetailEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigDetailEntity>()
                .eq(CustomConfigDetailEntity::getCompanyId, companyId)
                .eq(CustomConfigDetailEntity::getCustomConfigId, CustomConfigConstant.FIRST_CONTRACT_CUSTOM_CONFIG_ID);
        List<CustomConfigDetailEntity> customConfigDetailEntitiesCurrent = customConfigDetailMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(customConfigDetailEntitiesCurrent)) {
            return;
        }
        List<CustomConfigDetailEntity> customConfigDetailEntities = new ArrayList<>();
        CustomConfigConstant.CONTRACT_DETAIL_MAP_INIT.forEach((k, v) -> v.forEach(o -> {
            CustomConfigDetailEntity customConfigDetailEntity = new CustomConfigDetailEntity();
            customConfigDetailEntity.setId(IdWorker.getId());
            customConfigDetailEntity.setCompanyId(companyId);
            customConfigDetailEntity.setName(o.substring(0, o.length() - 2));
            customConfigDetailEntity.setCustomConfigId(k);
            customConfigDetailEntity.setSort(Byte.valueOf(o.substring(o.length() - 1)));
            customConfigDetailEntities.add(customConfigDetailEntity);
        }));
        customConfigDetailMapper.batchSaveCustomConfigDetails(customConfigDetailEntities);
    }

    private void batchSaveCustomConfigDetails(Long customConfigId, List<CustomConfigDetail> customConfigDetails, Long companyId) {
        List<CustomConfigDetailEntity> customConfigDetailEntities = customConfigDetails.stream()
                .map(o -> {
                    CustomConfigDetailEntity customConfigDetailEntity = new CustomConfigDetailEntity();
                    BeanUtils.copyProperties(o, customConfigDetailEntity);
                    customConfigDetailEntity.setId(IdWorker.getId());
                    customConfigDetailEntity.setCustomConfigId(customConfigId);
                    customConfigDetailEntity.setCompanyId(companyId);
                    return customConfigDetailEntity;
                })
                .collect(Collectors.toList());
        customConfigDetailMapper.batchSaveCustomConfigDetails(customConfigDetailEntities);
    }

    private void batchUpdateCustomConfigDetails(Long customConfigId, List<CustomConfigDetail> customConfigDetails,
                                                List<CustomConfigDetailEntity> oldCustomConfigDetailEntities, Long companyId) {
        // id为空的数据说明为新增数据，直接新增
        List<CustomConfigDetailEntity> newCustomConfigDetailEntities = customConfigDetails.stream()
                .filter(o -> Objects.isNull(o.getId()))
                .map(o -> {
                    CustomConfigDetailEntity customConfigDetailEntity = new CustomConfigDetailEntity();
                    BeanUtils.copyProperties(o, customConfigDetailEntity);
                    customConfigDetailEntity.setId(IdWorker.getId());
                    customConfigDetailEntity.setCustomConfigId(customConfigId);
                    customConfigDetailEntity.setCompanyId(companyId);
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
                    customConfigDetailEntity.setCompanyId(companyId);
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

    private void checkSave(List<CustomConfigDetail> customConfigDetails) {
        List<CustomConfigDetail> customConfigDistinctDetails = new ArrayList<>();
        customConfigDetails.stream()
                .filter(StreamUtil.distinctByKey(CustomConfigDetail::getName))
                .forEach(customConfigDistinctDetails::add);
        AssertUtil.isTrue(customConfigDetails.size() == customConfigDistinctDetails.size(),
                "自定义选项设置存在重复选项");
    }

}
