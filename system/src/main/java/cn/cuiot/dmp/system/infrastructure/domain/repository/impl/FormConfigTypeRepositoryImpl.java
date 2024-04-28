package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.system.domain.aggregate.FormConfigType;
import cn.cuiot.dmp.system.domain.repository.FormConfigTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.FormConfigTypeMapper;
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
public class FormConfigTypeRepositoryImpl implements FormConfigTypeRepository {

    @Autowired
    private FormConfigTypeMapper formConfigTypeMapper;

    @Override
    public FormConfigType queryForDetail(Long id) {
        FormConfigTypeEntity formConfigTypeEntity = formConfigTypeMapper.selectById(id);
        FormConfigType formConfigType = new FormConfigType();
        BeanUtils.copyProperties(formConfigTypeEntity, formConfigType);
        return formConfigType;
    }

}
