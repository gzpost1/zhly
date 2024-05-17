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
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionEntity;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionSettingEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CommonOptionDetailQueryDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionMapper;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionSettingMapper;
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

    @Autowired
    private CommonOptionSettingMapper commonOptionSettingMapper;

    @Autowired
    private FormConfigRepository formConfigRepository;

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
    public CommonOption queryForDetailByName(CommonOption commonOption) {
        AssertUtil.notBlank(commonOption.getName(),"常用选项名称不能为空");
        AssertUtil.notNull(commonOption.getCompanyId(), "企业Id不能为空");
        // 获取常用选项
        LambdaQueryWrapper<CommonOptionEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionEntity>()
                .eq(CommonOptionEntity::getName, commonOption.getName())
                .eq(CommonOptionEntity::getCompanyId, commonOption.getCompanyId());
        List<CommonOptionEntity> commonOptionEntityList = commonOptionMapper.selectList(queryWrapper);
        AssertUtil.notEmpty(commonOptionEntityList, "常用选项不存在");
        CommonOptionEntity commonOptionEntity = commonOptionEntityList.get(0);
        CommonOption commonOptionResult = new CommonOption();
        BeanUtils.copyProperties(commonOptionEntity, commonOptionResult);
        // 查询常用选项详情
        List<CommonOptionSetting> commonOptionSettings = commonOptionSettingRepository
                .batchQueryCommonOptionSettings(commonOptionEntity.getId());
        commonOptionResult.setCommonOptionSettings(commonOptionSettings);
        return commonOptionResult;
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

    @Override
    public void initCommonOption(Long companyId, Long typeId) {
        List<CommonOptionSettingEntity> commonOptionSettingEntities = new ArrayList<>();
        // 挂起原因
        CommonOptionEntity commonOptionSuspendEntity = new CommonOptionEntity();
        commonOptionSuspendEntity.setId(IdWorker.getId());
        commonOptionSuspendEntity.setName(CommonOptionConstant.COMMON_OPTION_SUSPEND);
        commonOptionSuspendEntity.setCompanyId(companyId);
        commonOptionSuspendEntity.setTypeId(typeId);
        commonOptionMapper.insert(commonOptionSuspendEntity);
        CommonOptionSettingEntity commonOptionSettingSuspend1 = new CommonOptionSettingEntity();
        commonOptionSettingSuspend1.setId(IdWorker.getId());
        commonOptionSettingSuspend1.setName(CommonOptionConstant.COMMON_OPTION_SETTING1);
        commonOptionSettingSuspend1.setCommonOptionId(commonOptionSuspendEntity.getId());
        CommonOptionSettingEntity commonOptionSettingSuspend2 = new CommonOptionSettingEntity();
        commonOptionSettingSuspend2.setId(IdWorker.getId());
        commonOptionSettingSuspend2.setName(CommonOptionConstant.COMMON_OPTION_SETTING2);
        commonOptionSettingSuspend2.setCommonOptionId(commonOptionSuspendEntity.getId());
        CommonOptionSettingEntity commonOptionSettingSuspend3 = new CommonOptionSettingEntity();
        commonOptionSettingSuspend3.setId(IdWorker.getId());
        commonOptionSettingSuspend3.setName(CommonOptionConstant.COMMON_OPTION_SETTING3);
        commonOptionSettingSuspend3.setCommonOptionId(commonOptionSuspendEntity.getId());
        // 终止原因
        CommonOptionEntity commonOptionStopEntity = new CommonOptionEntity();
        commonOptionStopEntity.setId(IdWorker.getId());
        commonOptionStopEntity.setName(CommonOptionConstant.COMMON_OPTION_STOP);
        commonOptionStopEntity.setCompanyId(companyId);
        commonOptionStopEntity.setTypeId(typeId);
        commonOptionMapper.insert(commonOptionStopEntity);
        CommonOptionSettingEntity commonOptionSettingStop1 = new CommonOptionSettingEntity();
        commonOptionSettingStop1.setId(IdWorker.getId());
        commonOptionSettingStop1.setName(CommonOptionConstant.COMMON_OPTION_SETTING1);
        commonOptionSettingStop1.setCommonOptionId(commonOptionStopEntity.getId());
        CommonOptionSettingEntity commonOptionSettingStop2 = new CommonOptionSettingEntity();
        commonOptionSettingStop2.setId(IdWorker.getId());
        commonOptionSettingStop2.setName(CommonOptionConstant.COMMON_OPTION_SETTING2);
        commonOptionSettingStop2.setCommonOptionId(commonOptionStopEntity.getId());
        CommonOptionSettingEntity commonOptionSettingStop3 = new CommonOptionSettingEntity();
        commonOptionSettingStop3.setId(IdWorker.getId());
        commonOptionSettingStop3.setName(CommonOptionConstant.COMMON_OPTION_SETTING3);
        commonOptionSettingStop3.setCommonOptionId(commonOptionStopEntity.getId());
        // 退回原因
        CommonOptionEntity commonOptionBackEntity = new CommonOptionEntity();
        commonOptionBackEntity.setId(IdWorker.getId());
        commonOptionBackEntity.setName(CommonOptionConstant.COMMON_OPTION_BACK);
        commonOptionBackEntity.setCompanyId(companyId);
        commonOptionBackEntity.setTypeId(typeId);
        commonOptionMapper.insert(commonOptionBackEntity);
        CommonOptionSettingEntity commonOptionSettingBack1 = new CommonOptionSettingEntity();
        commonOptionSettingBack1.setId(IdWorker.getId());
        commonOptionSettingBack1.setName(CommonOptionConstant.COMMON_OPTION_SETTING1);
        commonOptionSettingBack1.setCommonOptionId(commonOptionBackEntity.getId());
        CommonOptionSettingEntity commonOptionSettingBack2 = new CommonOptionSettingEntity();
        commonOptionSettingBack2.setId(IdWorker.getId());
        commonOptionSettingBack2.setName(CommonOptionConstant.COMMON_OPTION_SETTING2);
        commonOptionSettingBack2.setCommonOptionId(commonOptionBackEntity.getId());
        CommonOptionSettingEntity commonOptionSettingBack3 = new CommonOptionSettingEntity();
        commonOptionSettingBack3.setId(IdWorker.getId());
        commonOptionSettingBack3.setName(CommonOptionConstant.COMMON_OPTION_SETTING3);
        commonOptionSettingBack3.setCommonOptionId(commonOptionBackEntity.getId());
        // 初始化常用选项设置
        commonOptionSettingEntities.add(commonOptionSettingSuspend1);
        commonOptionSettingEntities.add(commonOptionSettingSuspend2);
        commonOptionSettingEntities.add(commonOptionSettingSuspend3);
        commonOptionSettingEntities.add(commonOptionSettingStop1);
        commonOptionSettingEntities.add(commonOptionSettingStop2);
        commonOptionSettingEntities.add(commonOptionSettingStop3);
        commonOptionSettingEntities.add(commonOptionSettingBack1);
        commonOptionSettingEntities.add(commonOptionSettingBack2);
        commonOptionSettingEntities.add(commonOptionSettingBack3);
        commonOptionSettingMapper.batchSaveCommonOptionSettings(commonOptionSettingEntities);
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
        // 该选项已被表单引用，不可删除
        AssertUtil.isFalse(formConfigRepository.useCommonOptionByFormConfig(id), "该选项已被表单引用，不可删除");
    }

}
