package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.constant.CommonOptionConstant;
import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionSetting;
import cn.cuiot.dmp.system.domain.repository.CommonOptionRepository;
import cn.cuiot.dmp.system.domain.repository.CommonOptionSettingRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CommonOptionDetailQueryDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Repository
public class CommonOptionRepositoryImpl implements CommonOptionRepository {

    @Autowired
    private CommonOptionMapper commonOptionMapper;

    @Autowired
    private CommonOptionSettingRepository commonOptionSettingRepository;

    @Override
    public CommonOption queryForDetail(Long id) {
        CommonOptionEntity commonOptionEntity = Optional.ofNullable(commonOptionMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        CommonOption commonOption = new CommonOption();
        BeanUtils.copyProperties(commonOptionEntity, commonOption);
        // 查询常用选项详情
        List<CommonOptionSetting> commonOptionSettings = commonOptionSettingRepository.batchQueryCommonOptionSettings(id);
        commonOption.setCommonOptionSettings(commonOptionSettings);
        return commonOption;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCommonOption(CommonOption commonOption) {
        commonOption.setId(IdWorker.getId());
        CommonOptionEntity commonOptionEntity = new CommonOptionEntity();
        BeanUtils.copyProperties(commonOption, commonOptionEntity);
        // 保存常用选项内容
        if (CollectionUtils.isNotEmpty(commonOption.getCommonOptionSettings())) {
            commonOptionSettingRepository.batchSaveOrUpdateCommonOptionSettings(commonOption.getId(),
                    commonOption.getCommonOptionSettings());
        }
        return commonOptionMapper.insert(commonOptionEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCommonOption(CommonOption commonOption) {
        CommonOptionEntity commonOptionEntity = Optional.ofNullable(commonOptionMapper.selectById(commonOption.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(commonOption, commonOptionEntity);
        // 如果传入的常用选项设置不为空，则执行保存更新操作
        if (CollectionUtils.isNotEmpty(commonOption.getCommonOptionSettings())) {
            commonOptionSettingRepository.batchSaveOrUpdateCommonOptionSettings(commonOption.getId(),
                    commonOption.getCommonOptionSettings());
        } else {
            // 如果为空，则删除原有数据
            List<Long> commonOptionIdList = new ArrayList<>();
            commonOptionIdList.add(commonOption.getId());
            commonOptionSettingRepository.batchDeleteCommonOptionSettings(commonOptionIdList);
        }
        return commonOptionMapper.updateById(commonOptionEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCommonOptionStatus(CommonOption commonOption) {
        CommonOptionEntity commonOptionEntity = Optional.ofNullable(commonOptionMapper.selectById(commonOption.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        commonOptionEntity.setStatus(commonOption.getStatus());
        return commonOptionMapper.updateById(commonOptionEntity);
    }

    @Override
    public void checkDeleteStatus(Long id) {
        checkDelete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCommonOption(Long id) {
        // 先删除常用选项详情，后删除常用选项
        List<Long> commonOptionIdList = new ArrayList<>();
        commonOptionIdList.add(id);
        commonOptionSettingRepository.batchDeleteCommonOptionSettings(commonOptionIdList);
        return commonOptionMapper.deleteById(id);
    }

    @Override
    public List<CommonOption> batchQueryCommonOption(Byte status, List<Long> idList) {
        AssertUtil.notEmpty(idList, "常用选项ID列表不能为空");
        // 获取常用选项
        LambdaQueryWrapper<CommonOptionEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionEntity>()
                .in(CommonOptionEntity::getId, idList)
                .eq(Objects.nonNull(status), CommonOptionEntity::getStatus, status);
        List<CommonOptionEntity> commonOptionEntityList = commonOptionMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(commonOptionEntityList)) {
            return new ArrayList<>();
        }
        return commonOptionEntityList.stream()
                .map(o -> {
                    CommonOption commonOption = new CommonOption();
                    BeanUtils.copyProperties(o, commonOption);
                    List<CommonOptionSetting> commonOptionSettings = commonOptionSettingRepository
                            .batchQueryCommonOptionSettings(o.getId());
                    commonOption.setCommonOptionSettings(commonOptionSettings);
                    return commonOption;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchMoveCommonOption(Long typeId, List<Long> idList) {
        return commonOptionMapper.batchUpdateCommonOptionById(typeId, null, idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchMoveCommonOptionDefault(List<String> typeIdList) {
        return commonOptionMapper.batchMoveCommonOptionDefault(typeIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateCommonOptionStatus(Byte status, List<Long> idList) {
        return commonOptionMapper.batchUpdateCommonOptionById(null, status, idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteCommonOption(List<Long> idList) {
        // 先删除常用选项详情，后删除常用选项
        commonOptionSettingRepository.batchDeleteCommonOptionSettings(idList);
        return commonOptionMapper.deleteBatchIds(idList);
    }

    @Override
    public PageResult<CommonOption> queryCommonOptionByType(CommonOptionPageQuery pageQuery) {
        LambdaQueryWrapper<CommonOptionEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionEntity>()
                .eq(Objects.nonNull(pageQuery.getTypeId()), CommonOptionEntity::getTypeId, pageQuery.getTypeId())
                .like(StringUtils.isNotBlank(pageQuery.getName()), CommonOptionEntity::getName, pageQuery.getName())
                .eq(Objects.nonNull(pageQuery.getStatus()), CommonOptionEntity::getStatus, pageQuery.getStatus());
        IPage<CommonOptionEntity> commonOptionEntityPage = commonOptionMapper.selectPage(
                new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
        if (Objects.isNull(commonOptionEntityPage) || CollectionUtils.isEmpty(commonOptionEntityPage.getRecords())) {
            return new PageResult<>();
        }
        return commonOptionEntity2CommonOption(commonOptionEntityPage);
    }

    private Query getQuery(CommonOptionDetailQueryDTO commonOptionDetailQueryDTO) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (Objects.nonNull(commonOptionDetailQueryDTO.getId())) {
            criteria.and(CommonOptionConstant.COMMON_OPTION_COLLECTION_PK).is(commonOptionDetailQueryDTO.getId());
        }
        if (CollectionUtils.isNotEmpty(commonOptionDetailQueryDTO.getIdList())) {
            criteria.and(CommonOptionConstant.COMMON_OPTION_COLLECTION_PK).in(commonOptionDetailQueryDTO.getIdList());
        }
        query.addCriteria(criteria);
        return query;
    }

    private PageResult<CommonOption> commonOptionEntity2CommonOption(IPage<CommonOptionEntity> commonOptionEntityPage) {
        PageResult<CommonOption> commonOptionPageResult = new PageResult<>();
        List<CommonOption> commonOptionList = commonOptionEntityPage.getRecords().stream()
                .map(o -> {
                    CommonOption commonOption = new CommonOption();
                    BeanUtils.copyProperties(o, commonOption);
                    List<CommonOptionSetting> commonOptionSettings = commonOptionSettingRepository
                            .batchQueryCommonOptionSettings(o.getId());
                    commonOption.setCommonOptionSettings(commonOptionSettings);
                    return commonOption;
                })
                .collect(Collectors.toList());
        commonOptionPageResult.setList(commonOptionList);
        commonOptionPageResult.setCurrentPage((int) commonOptionEntityPage.getCurrent());
        commonOptionPageResult.setPageSize((int) commonOptionEntityPage.getSize());
        commonOptionPageResult.setTotal(commonOptionEntityPage.getTotal());
        return commonOptionPageResult;
    }

    private void checkDelete(Long id) {
        // 该类型已被表单引用，不可删除
        return;
    }

}
