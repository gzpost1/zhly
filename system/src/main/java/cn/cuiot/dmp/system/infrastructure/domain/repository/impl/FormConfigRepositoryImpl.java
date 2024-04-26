package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.FormConfigMapper;
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
public class FormConfigRepositoryImpl implements FormConfigRepository {

    @Autowired
    private FormConfigMapper formConfigMapper;

    @Override
    public FormConfig queryForDetail(Long id) {
        FormConfigEntity formConfigEntity = formConfigMapper.selectById(id);
        FormConfig formConfig = new FormConfig();
        BeanUtils.copyProperties(formConfigEntity, formConfig);
        return formConfig;
    }
}
