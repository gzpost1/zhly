package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.domain.aggregate.SystemOptionType;
import cn.cuiot.dmp.system.domain.repository.SystemOptionTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.SystemOptionTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SystemOptionTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Slf4j
@Repository
public class SystemOptionTypeRepositoryImpl implements SystemOptionTypeRepository {

    @Autowired
    private SystemOptionTypeMapper systemOptionTypeMapper;

    @Override
    public List<SystemOptionType> queryForList(SystemOptionType systemOptionType) {
        LambdaQueryWrapper<SystemOptionTypeEntity> queryWrapper = new LambdaQueryWrapper<SystemOptionTypeEntity>()
                .eq(Objects.nonNull(systemOptionType.getSystemOptionType()), SystemOptionTypeEntity::getSystemOptionType,
                        systemOptionType.getSystemOptionType())
                .like(StringUtils.isNotBlank(systemOptionType.getName()), SystemOptionTypeEntity::getName, systemOptionType.getName());
        List<SystemOptionTypeEntity> systemOptionTypeEntityList = systemOptionTypeMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(systemOptionTypeEntityList)) {
            return new ArrayList<>();
        }
        return systemOptionTypeEntityList.stream()
                .map(o -> {
                    SystemOptionType systemOptionTypeResult = new SystemOptionType();
                    BeanUtils.copyProperties(o, systemOptionTypeResult);
                    return systemOptionTypeResult;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SystemOptionType queryForDetail(Long id) {
        SystemOptionTypeEntity systemOptionTypeEntity = Optional.ofNullable(systemOptionTypeMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        SystemOptionType systemOptionType = new SystemOptionType();
        BeanUtils.copyProperties(systemOptionTypeEntity, systemOptionType);
        return systemOptionType;
    }

}
