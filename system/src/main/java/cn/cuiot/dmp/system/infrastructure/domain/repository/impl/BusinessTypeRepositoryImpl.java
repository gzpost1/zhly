package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.constant.BusinessTypeConstant;
import cn.cuiot.dmp.system.domain.aggregate.BusinessType;
import cn.cuiot.dmp.system.domain.repository.BusinessTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.BusinessTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.BusinessTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Repository
public class BusinessTypeRepositoryImpl implements BusinessTypeRepository {

    @Autowired
    private BusinessTypeMapper businessTypeMapper;

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
        return null;
    }

    @Override
    public int saveBusinessType(BusinessType businessType) {
        BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
        BeanUtils.copyProperties(businessType, businessTypeEntity);
        return businessTypeMapper.insert(businessTypeEntity);
    }

    @Override
    public int updateBusinessType(BusinessType businessType) {
        AssertUtil.notNull(businessType.getId(), "id不能为空");
        BusinessTypeEntity businessTypeEntity = Optional.ofNullable(businessTypeMapper.selectById(businessType.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(businessType, businessTypeEntity);
        return businessTypeMapper.updateById(businessTypeEntity);
    }

    private BusinessTypeEntity initRootNode(Long companyId) {
        BusinessTypeEntity businessTypeEntity = new BusinessTypeEntity();
        businessTypeEntity.setId(BusinessTypeConstant.ROOT_ID);
        businessTypeEntity.setCompanyId(companyId);
        // todo 根据企业id获取企业名称
        businessTypeEntity.setName("企业名称");
        businessTypeEntity.setLevelType(BusinessTypeConstant.ROOT_LEVEL_TYPE);
        businessTypeEntity.setParentId(BusinessTypeConstant.DEFAULT_PARENT_ID);
        businessTypeMapper.insert(businessTypeEntity);
        return businessTypeEntity;
    }

}
