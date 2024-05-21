package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.system.domain.aggregate.ArchiveType;
import cn.cuiot.dmp.system.domain.repository.ArchiveTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.ArchiveTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.ArchiveTypeMapper;
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
public class ArchiveTypeRepositoryImpl implements ArchiveTypeRepository {

    @Autowired
    private ArchiveTypeMapper archiveTypeMapper;

    @Override
    public List<ArchiveType> queryForList(ArchiveType archiveType) {
        LambdaQueryWrapper<ArchiveTypeEntity> queryWrapper = new LambdaQueryWrapper<ArchiveTypeEntity>()
                .eq(Objects.nonNull(archiveType.getArchiveType()), ArchiveTypeEntity::getArchiveType, archiveType.getArchiveType())
                .like(StringUtils.isNotBlank(archiveType.getName()), ArchiveTypeEntity::getName, archiveType.getName());
        List<ArchiveTypeEntity> archiveTypeEntityList = archiveTypeMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(archiveTypeEntityList)) {
            return new ArrayList<>();
        }
        return archiveTypeEntityList.stream()
                .map(o -> {
                    ArchiveType archiveTypeResult = new ArchiveType();
                    BeanUtils.copyProperties(o, archiveTypeResult);
                    return archiveTypeResult;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ArchiveType queryForDetail(Long id) {
        ArchiveTypeEntity archiveTypeEntity = Optional.ofNullable(archiveTypeMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        ArchiveType archiveType = new ArchiveType();
        BeanUtils.copyProperties(archiveTypeEntity, archiveType);
        return archiveType;
    }

}
