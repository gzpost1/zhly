package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.system.domain.aggregate.BusinessType;
import cn.cuiot.dmp.system.domain.repository.BusinessTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.BusinessTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.BusinessTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        BusinessTypeEntity businessTypeEntity = businessTypeMapper.selectById(id);
        BusinessType businessType = new BusinessType();
        BeanUtils.copyProperties(businessTypeEntity, businessType);
        return businessType;
    }

}
