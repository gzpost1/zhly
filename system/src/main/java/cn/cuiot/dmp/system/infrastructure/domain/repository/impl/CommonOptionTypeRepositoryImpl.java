package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.constant.CommonOptionConstant;
import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionType;
import cn.cuiot.dmp.system.domain.repository.CommonOptionRepository;
import cn.cuiot.dmp.system.domain.repository.CommonOptionTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
 * @date 2024/4/26
 */
@Slf4j
@Repository
public class CommonOptionTypeRepositoryImpl implements CommonOptionTypeRepository {

    @Autowired
    private CommonOptionTypeMapper commonOptionTypeMapper;

    @Autowired
    private CommonOptionRepository commonOptionRepository;

    @Override
    public CommonOptionType queryForDetail(Long id) {
        CommonOptionTypeEntity commonOptionTypeEntity = Optional.ofNullable(commonOptionTypeMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        CommonOptionType commonOptionType = new CommonOptionType();
        BeanUtils.copyProperties(commonOptionTypeEntity, commonOptionType);
        return commonOptionType;
    }

    @Override
    public List<CommonOptionType> queryByCompany(Long companyId) {
        LambdaQueryWrapper<CommonOptionTypeEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionTypeEntity>()
                .eq(CommonOptionTypeEntity::getCompanyId, companyId);
        List<CommonOptionTypeEntity> commonOptionTypeEntityList = commonOptionTypeMapper.selectList(queryWrapper);
        List<CommonOptionType> commonOptionTypeList = new ArrayList<>();
        // 如果该企业下没有数据，默认创建一条"全部"作为根节点的数据
        if (CollectionUtils.isEmpty(commonOptionTypeEntityList)) {
            CommonOptionTypeEntity commonOptionTypeEntity = initRootNode(companyId);
            CommonOptionType commonOptionType = new CommonOptionType();
            BeanUtils.copyProperties(commonOptionTypeEntity, commonOptionType);
            commonOptionTypeList.add(commonOptionType);
            return commonOptionTypeList;
        }
        commonOptionTypeList = commonOptionTypeEntityList.stream()
                .map(o -> {
                    CommonOptionType commonOptionType = new CommonOptionType();
                    BeanUtils.copyProperties(o, commonOptionType);
                    return commonOptionType;
                })
                .collect(Collectors.toList());
        return commonOptionTypeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCommonOptionType(CommonOptionType commonOptionType) {
        checkCommonOptionTypeNode(commonOptionType);
        CommonOptionTypeEntity commonOptionTypeEntity = new CommonOptionTypeEntity();
        BeanUtils.copyProperties(commonOptionType, commonOptionTypeEntity);
        return commonOptionTypeMapper.insert(commonOptionTypeEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCommonOptionType(CommonOptionType commonOptionType) {
        checkCommonOptionTypeNode(commonOptionType);
        CommonOptionTypeEntity commonOptionTypeEntity = Optional.ofNullable(commonOptionTypeMapper.selectById(commonOptionType.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(commonOptionType, commonOptionTypeEntity);
        return commonOptionTypeMapper.updateById(commonOptionTypeEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCommonOptionType(List<String> idList) {
        return commonOptionTypeMapper.deleteBatchIds(idList);
    }

    @Override
    public PageResult<CommonOption> queryCommonOptionByType(CommonOptionPageQuery pageQuery) {
        return commonOptionRepository.queryCommonOptionByType(pageQuery);
    }

    private CommonOptionTypeEntity initRootNode(Long companyId) {
        CommonOptionTypeEntity commonOptionTypeEntity = new CommonOptionTypeEntity();
        commonOptionTypeEntity.setId(CommonOptionConstant.ROOT_ID);
        commonOptionTypeEntity.setCompanyId(companyId);
        commonOptionTypeEntity.setName(CommonOptionConstant.ROOT_NAME);
        commonOptionTypeEntity.setLevelType(CommonOptionConstant.ROOT_LEVEL_TYPE);
        commonOptionTypeEntity.setParentId(CommonOptionConstant.DEFAULT_PARENT_ID);
        commonOptionTypeEntity.setPathName(CommonOptionConstant.ROOT_NAME);
        commonOptionTypeMapper.insert(commonOptionTypeEntity);
        return commonOptionTypeEntity;
    }

    private void checkCommonOptionTypeNode(CommonOptionType commonOptionType) {
        LambdaQueryWrapper<CommonOptionTypeEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionTypeEntity>()
                .eq(CommonOptionTypeEntity::getLevelType, commonOptionType.getLevelType())
                .eq(CommonOptionTypeEntity::getParentId, commonOptionType.getParentId())
                .ne(Objects.nonNull(commonOptionType.getId()), CommonOptionTypeEntity::getId, commonOptionType.getId());
        List<CommonOptionTypeEntity> commonOptionTypeEntityList = commonOptionTypeMapper.selectList(queryWrapper);
        for (CommonOptionTypeEntity commonOptionTypeEntity : commonOptionTypeEntityList) {
            AssertUtil.isFalse(commonOptionTypeEntity.getName().equals(commonOptionType.getName()),
                    "分类名称已存在");
        }
    }
    
}
