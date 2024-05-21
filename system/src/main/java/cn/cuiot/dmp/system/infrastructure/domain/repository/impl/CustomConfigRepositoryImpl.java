package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
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
    public void initCustomConfig(Long companyId, Long typeId) {

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
