package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.param.dto.SystemInfoQueryDTO;
import cn.cuiot.dmp.system.domain.aggregate.SystemInfo;
import cn.cuiot.dmp.system.domain.repository.SystemInfoRepository;
import cn.cuiot.dmp.system.infrastructure.entity.SystemInfoEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SystemInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Repository
public class SystemInfoRepositoryImpl implements SystemInfoRepository {

    @Autowired
    private SystemInfoMapper systemInfoMapper;

    @Override
    public SystemInfo queryForDetail(Long id) {
        SystemInfoEntity systemInfoEntity = Optional.ofNullable(systemInfoMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        SystemInfo systemInfo = new SystemInfo();
        BeanUtils.copyProperties(systemInfoEntity, systemInfo);
        return systemInfo;
    }

    @Override
    public SystemInfo queryBySource(SystemInfoQueryDTO systemInfoQueryDTO) {
        LambdaQueryWrapper<SystemInfoEntity> queryWrapper = new LambdaQueryWrapper<SystemInfoEntity>()
                .eq(SystemInfoEntity::getSourceId, systemInfoQueryDTO.getSourceId())
                .eq(SystemInfoEntity::getSourceType, systemInfoQueryDTO.getSourceType());
        SystemInfoEntity systemInfoEntity = systemInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(systemInfoEntity)) {
            return new SystemInfo();
        }
        SystemInfo systemInfo = new SystemInfo();
        BeanUtils.copyProperties(systemInfoEntity, systemInfo);
        return systemInfo;
    }

    @Override
    public int saveSystemInfo(SystemInfo systemInfo) {
        SystemInfoEntity systemInfoEntity = new SystemInfoEntity();
        BeanUtils.copyProperties(systemInfo, systemInfoEntity);
        return systemInfoMapper.insert(systemInfoEntity);
    }

    @Override
    public int updateSystemInfo(SystemInfo systemInfo) {
        AssertUtil.notNull(systemInfo.getId(), "id不能为空");
        SystemInfoEntity systemInfoEntity = Optional.ofNullable(systemInfoMapper.selectById(systemInfo.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(systemInfo, systemInfoEntity);
        return systemInfoMapper.updateById(systemInfoEntity);
    }

}
