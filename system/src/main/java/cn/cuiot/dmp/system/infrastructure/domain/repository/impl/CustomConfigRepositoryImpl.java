package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.constant.CustomConfigConstant;
import cn.cuiot.dmp.common.enums.ArchiveTypeEnum;
import cn.cuiot.dmp.system.domain.aggregate.*;
import cn.cuiot.dmp.system.domain.repository.CustomConfigDetailRepository;
import cn.cuiot.dmp.system.domain.repository.CustomConfigRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CustomConfigEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CustomConfigMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/17
 */
@Slf4j
@Repository
public class CustomConfigRepositoryImpl implements CustomConfigRepository {

    @Autowired
    private CustomConfigMapper customConfigMapper;

    @Autowired
    private CustomConfigDetailRepository customConfigDetailRepository;

    @Override
    public CustomConfig queryForDetail(Long id) {
        CustomConfigEntity customConfigEntity = Optional.ofNullable(customConfigMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        CustomConfig customConfig = new CustomConfig();
        BeanUtils.copyProperties(customConfigEntity, customConfig);
        // 查询自定义配置详情
        List<CustomConfigDetail> customConfigDetails = customConfigDetailRepository.batchQueryCustomConfigDetails(id);
        customConfig.setCustomConfigDetailList(customConfigDetails);
        return customConfig;
    }

    @Override
    public CustomConfig queryForDetailByName(CustomConfig customConfig) {
        AssertUtil.notBlank(customConfig.getName(), "自定义配置名称不能为空");
        AssertUtil.notNull(customConfig.getCompanyId(), "企业Id不能为空");
        LambdaQueryWrapper<CustomConfigEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigEntity>()
                .eq(CustomConfigEntity::getCompanyId, customConfig.getCompanyId())
                .eq(CustomConfigEntity::getName, customConfig.getName());
        List<CustomConfigEntity> customConfigEntityList = customConfigMapper.selectList(queryWrapper);
        AssertUtil.notEmpty(customConfigEntityList, "自定义配置不存在");
        CustomConfigEntity customConfigEntity = customConfigEntityList.get(0);
        CustomConfig customConfigResult = new CustomConfig();
        BeanUtils.copyProperties(customConfigEntity, customConfigResult);
        // 查询自定义配置详情
        List<CustomConfigDetail> customConfigDetails = customConfigDetailRepository
                .batchQueryCustomConfigDetails(customConfigEntity.getId());
        customConfig.setCustomConfigDetailList(customConfigDetails);
        return customConfig;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCustomConfig(CustomConfig customConfig) {
        customConfig.setId(IdWorker.getId());
        CustomConfigEntity customConfigEntity = new CustomConfigEntity();
        BeanUtils.copyProperties(customConfig, customConfigEntity);
        // 保存自定义配置详情
        if (CollectionUtils.isNotEmpty(customConfig.getCustomConfigDetailList())) {
            customConfigDetailRepository.batchSaveOrUpdateCustomConfigDetails(customConfig.getId(),
                    customConfig.getCustomConfigDetailList());
        }
        return customConfigMapper.insert(customConfigEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomConfig(CustomConfig customConfig) {
        CustomConfigEntity customConfigEntity = Optional.ofNullable(customConfigMapper.selectById(customConfig.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(customConfig, customConfigEntity);
        // 如果传入的自定义配置设置不为空，则执行保存更新操作
        if (CollectionUtils.isNotEmpty(customConfig.getCustomConfigDetailList())) {
            customConfigDetailRepository.batchSaveOrUpdateCustomConfigDetails(customConfig.getId(),
                    customConfig.getCustomConfigDetailList());
        } else {
            // 如果为空，则删除原有数据
            List<Long> customConfigIdList = new ArrayList<>();
            customConfigIdList.add(customConfig.getId());
            customConfigDetailRepository.batchDeleteCustomConfigDetails(customConfigIdList);
        }
        return customConfigMapper.updateById(customConfigEntity);
    }

    @Override
    public int updateCustomConfigStatus(CustomConfig customConfig) {
        CustomConfigEntity customConfigEntity = Optional.ofNullable(customConfigMapper.selectById(customConfig.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        customConfigEntity.setStatus(customConfig.getStatus());
        return customConfigMapper.updateById(customConfigEntity);
    }

    @Override
    public int deleteCustomConfig(Long id) {
        // 先删除自定义配置详情，后删除自定义配置
        List<Long> customConfigIdList = new ArrayList<>();
        customConfigIdList.add(id);
        customConfigDetailRepository.batchDeleteCustomConfigDetails(customConfigIdList);
        return customConfigMapper.deleteById(id);
    }

    @Override
    public PageResult<CustomConfig> queryCustomConfigByType(CustomConfigPageQuery pageQuery) {
        LambdaQueryWrapper<CustomConfigEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigEntity>()
                .eq(Objects.nonNull(pageQuery.getCompanyId()), CustomConfigEntity::getCompanyId, pageQuery.getCompanyId())
                .eq(Objects.nonNull(pageQuery.getArchiveType()), CustomConfigEntity::getArchiveType, pageQuery.getArchiveType())
                .like(StringUtils.isNotBlank(pageQuery.getName()), CustomConfigEntity::getName, pageQuery.getName())
                .eq(Objects.nonNull(pageQuery.getStatus()), CustomConfigEntity::getStatus, pageQuery.getStatus());
        IPage<CustomConfigEntity> customConfigEntityPage = customConfigMapper.selectPage(
                new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
        if (Objects.isNull(customConfigEntityPage) || CollectionUtils.isEmpty(customConfigEntityPage.getRecords())) {
            return new PageResult<>();
        }
        return customConfigEntity2CustomConfig(customConfigEntityPage);
    }

    @Override
    public List<CustomConfig> queryForList(CustomConfigPageQuery pageQuery) {
        LambdaQueryWrapper<CustomConfigEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigEntity>()
                .eq(Objects.nonNull(pageQuery.getCompanyId()), CustomConfigEntity::getCompanyId, pageQuery.getCompanyId())
                .eq(Objects.nonNull(pageQuery.getArchiveType()), CustomConfigEntity::getArchiveType, pageQuery.getArchiveType())
                .like(StringUtils.isNotBlank(pageQuery.getName()), CustomConfigEntity::getName, pageQuery.getName())
                .eq(Objects.nonNull(pageQuery.getStatus()), CustomConfigEntity::getStatus, pageQuery.getStatus());
        List<CustomConfigEntity> customConfigEntityList = customConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(customConfigEntityList)) {
            return new ArrayList<>();
        }
        return customConfigEntityList.stream()
                .map(o -> {
                    CustomConfig customConfig = new CustomConfig();
                    BeanUtils.copyProperties(o, customConfig);
                    List<CustomConfigDetail> CustomConfigDetails = customConfigDetailRepository
                            .batchQueryCustomConfigDetails(o.getId());
                    customConfig.setCustomConfigDetailList(CustomConfigDetails);
                    return customConfig;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void initCustomConfig(Long companyId, String userId) {
        AssertUtil.notNull(companyId, "企业ID不能为空");
        LambdaQueryWrapper<CustomConfigEntity> queryWrapper = new LambdaQueryWrapper<CustomConfigEntity>()
                .eq(CustomConfigEntity::getCompanyId, companyId);
        List<CustomConfigEntity> customConfigEntityListCurrent = customConfigMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(customConfigEntityListCurrent)) {
            return;
        }
        AssertUtil.notBlank(userId, "用户ID不能为空");
        List<CustomConfigEntity> customConfigEntityList = new ArrayList<>();
        // 房屋档案
        CustomConfigConstant.HOUSES_ARCHIVES_INIT.forEach(o -> {
            CustomConfigEntity customConfigEntity = new CustomConfigEntity();
            customConfigEntity.setId(IdWorker.getId());
            customConfigEntity.setCompanyId(companyId);
            customConfigEntity.setName(o);
            customConfigEntity.setArchiveType(ArchiveTypeEnum.HOUSE_ARCHIVE.getCode());
            customConfigEntity.setCreatedBy(userId);
            customConfigEntityList.add(customConfigEntity);
        });
        // 空间档案
        CustomConfigConstant.ROOM_ARCHIVES_INIT.forEach(o -> {
            CustomConfigEntity customConfigEntity = new CustomConfigEntity();
            customConfigEntity.setId(IdWorker.getId());
            customConfigEntity.setCompanyId(companyId);
            customConfigEntity.setName(o);
            customConfigEntity.setArchiveType(ArchiveTypeEnum.ROOM_ARCHIVE.getCode());
            customConfigEntity.setCreatedBy(userId);
            customConfigEntityList.add(customConfigEntity);
        });
        // 设备档案
        CustomConfigConstant.DEVICE_ARCHIVES_INIT.forEach(o -> {
            CustomConfigEntity customConfigEntity = new CustomConfigEntity();
            customConfigEntity.setId(IdWorker.getId());
            customConfigEntity.setCompanyId(companyId);
            customConfigEntity.setName(o);
            customConfigEntity.setArchiveType(ArchiveTypeEnum.DEVICE_ARCHIVE.getCode());
            customConfigEntity.setCreatedBy(userId);
            customConfigEntityList.add(customConfigEntity);
        });
        // 车位档案
        CustomConfigConstant.PARKING_ARCHIVES_INIT.forEach(o -> {
            CustomConfigEntity customConfigEntity = new CustomConfigEntity();
            customConfigEntity.setId(IdWorker.getId());
            customConfigEntity.setCompanyId(companyId);
            customConfigEntity.setName(o);
            customConfigEntity.setArchiveType(ArchiveTypeEnum.PARK_ARCHIVE.getCode());
            customConfigEntity.setCreatedBy(userId);
            customConfigEntityList.add(customConfigEntity);
        });
        customConfigMapper.batchSaveCustomConfig(customConfigEntityList);
    }

    private PageResult<CustomConfig> customConfigEntity2CustomConfig(IPage<CustomConfigEntity> customConfigEntityPage) {
        PageResult<CustomConfig> customConfigPageResult = new PageResult<>();
        List<CustomConfig> customConfigList = customConfigEntityPage.getRecords().stream()
                .map(o -> {
                    CustomConfig customConfig = new CustomConfig();
                    BeanUtils.copyProperties(o, customConfig);
                    List<CustomConfigDetail> CustomConfigDetails = customConfigDetailRepository
                            .batchQueryCustomConfigDetails(o.getId());
                    customConfig.setCustomConfigDetailList(CustomConfigDetails);
                    return customConfig;
                })
                .collect(Collectors.toList());
        customConfigPageResult.setList(customConfigList);
        customConfigPageResult.setCurrentPage((int) customConfigEntityPage.getCurrent());
        customConfigPageResult.setPageSize((int) customConfigEntityPage.getSize());
        customConfigPageResult.setTotal(customConfigEntityPage.getTotal());
        return customConfigPageResult;
    }

}
