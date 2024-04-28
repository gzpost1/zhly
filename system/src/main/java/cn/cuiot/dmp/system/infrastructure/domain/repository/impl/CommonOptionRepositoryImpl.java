package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.repository.CommonOptionRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionMapper;
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
public class CommonOptionRepositoryImpl implements CommonOptionRepository {

    @Autowired
    private CommonOptionMapper commonOptionMapper;

    @Override
    public CommonOption queryForDetail(Long id) {
        CommonOptionEntity commonOptionEntity = commonOptionMapper.selectById(id);
        CommonOption commonOption = new CommonOption();
        BeanUtils.copyProperties(commonOptionEntity, commonOption);
        return commonOption;
    }
}
