package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.constant.BusinessTypeConstant;
import cn.cuiot.dmp.system.domain.aggregate.BusinessType;
import cn.cuiot.dmp.system.domain.repository.BusinessTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.BusinessTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.BusinessTypeMapper;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.OrganizationEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.OrganizationEntityMapper;
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
public class BusinessTypeRepositoryImpl implements BusinessTypeRepository {

    @Autowired
    private BusinessTypeMapper businessTypeMapper;

    @Autowired
    private OrganizationEntityMapper organizationEntityMapper;

    @Override
    public BusinessType queryForDetail(Long id) {
        BusinessTypeEntity businessTypeEntity = Optional.ofNullable(businessTypeMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BusinessType businessType = new BusinessType();
        BeanUtils.copyProperties(businessTypeEntity, businessType);
        return businessType;
    }

    @Override
    public List<BusinessType> queryByCompany(Long companyId) {
        LambdaQueryWrapper<BusinessTypeEntity> queryWrapper = new LambdaQueryWrapper<BusinessTypeEntity>()
                .eq(BusinessTypeEntity::getCompanyId, companyId);
        List<BusinessTypeEntity> businessTypeEntityList = businessTypeMapper.selectList(queryWrapper);
        List<BusinessType> businessTypeList = new ArrayList<>();
        // 如果该企业下没有数据，默认创建一条以企业名称作为根节点的数据
        if (CollectionUtils.isEmpty(businessTypeEntityList)) {
            BusinessTypeEntity businessTypeEntity = initRootNode(companyId);
            BusinessType businessType = new BusinessType();
            BeanUtils.copyProperties(businessTypeEntity, businessType);
            businessTypeList.add(businessType);
            return businessTypeList;
        }
        businessTypeList = businessTypeEntityList.stream()
                .map(o -> {
                    BusinessType businessType = new BusinessType();
                    BeanUtils.copyProperties(o, businessType);
                    return businessType;
                })
                .collect(Collectors.toList());
        return businessTypeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveBusinessType(BusinessType businessType) {
        checkBusinessTypeNode(businessType);
        BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
        BeanUtils.copyProperties(businessType, businessTypeEntity);
        return businessTypeMapper.insert(businessTypeEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBusinessType(BusinessType businessType) {
        checkBusinessTypeNode(businessType);
        BusinessTypeEntity businessTypeEntity = Optional.ofNullable(businessTypeMapper.selectById(businessType.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(businessType, businessTypeEntity);
        return businessTypeMapper.updateById(businessTypeEntity);
    }

    @Override
    public void checkDeleteStatus(Long id) {
        checkDelete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBusinessType(List<String> idList) {
        return businessTypeMapper.deleteBatchIds(idList);
    }

    private BusinessTypeEntity initRootNode(Long companyId) {
        BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
        businessTypeEntity.setId(BusinessTypeConstant.ROOT_ID);
        businessTypeEntity.setCompanyId(companyId);
        OrganizationEntity organizationEntity = organizationEntityMapper.selectById(companyId);
        AssertUtil.notNull(organizationEntity, "企业不存在");
        businessTypeEntity.setName(organizationEntity.getCompanyName());
        businessTypeEntity.setLevelType(BusinessTypeConstant.ROOT_LEVEL_TYPE);
        businessTypeEntity.setParentId(BusinessTypeConstant.DEFAULT_PARENT_ID);
        businessTypeMapper.insert(businessTypeEntity);
        return businessTypeEntity;
    }

    private void checkBusinessTypeNode(BusinessType businessType) {
        LambdaQueryWrapper<BusinessTypeEntity> queryWrapper = new LambdaQueryWrapper<BusinessTypeEntity>()
                .eq(BusinessTypeEntity::getLevelType, businessType.getLevelType())
                .eq(BusinessTypeEntity::getParentId, businessType.getParentId())
                .ne(Objects.nonNull(businessType.getId()), BusinessTypeEntity::getId, businessType.getId());
        List<BusinessTypeEntity> businessTypeEntityList = businessTypeMapper.selectList(queryWrapper);
        for (BusinessTypeEntity businessTypeEntity : businessTypeEntityList) {
            AssertUtil.isFalse(businessTypeEntity.getName().equals(businessType.getName()),
                    "分类名称已存在");
        }
    }

    private void checkDelete(Long id) {
        // 该类型存在下级类型，不可删除
        LambdaQueryWrapper<BusinessTypeEntity> queryWrapper = new LambdaQueryWrapper<BusinessTypeEntity>()
                .eq(BusinessTypeEntity::getParentId, id);
        List<BusinessTypeEntity> businessTypeEntityList = businessTypeMapper.selectList(queryWrapper);
        AssertUtil.isTrue(CollectionUtils.isEmpty(businessTypeEntityList), "该类型存在下级类型，不可删除");
    }

}
