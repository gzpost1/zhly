package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.system.domain.aggregate.CommonOptionType;
import cn.cuiot.dmp.system.domain.repository.CommonOptionTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionTypeMapper;
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
public class CommonOptionTypeRepositoryImpl implements CommonOptionTypeRepository {

    @Autowired
    private CommonOptionTypeMapper commonOptionTypeMapper;

    @Override
    public CommonOptionType queryForDetail(Long id) {
        CommonOptionTypeEntity commonOptionTypeEntity = commonOptionTypeMapper.selectById(id);
        CommonOptionType commonOptionType = new CommonOptionType();
        BeanUtils.copyProperties(commonOptionTypeEntity, commonOptionType);
        return commonOptionType;
    }
}
