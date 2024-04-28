package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.system.domain.aggregate.SystemInfo;
import cn.cuiot.dmp.system.domain.repository.SystemInfoRepository;
import cn.cuiot.dmp.system.infrastructure.entity.SystemInfoEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.SystemInfoMapper;
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
public class SystemInfoRepositoryImpl implements SystemInfoRepository {

    @Autowired
    private SystemInfoMapper systemInfoMapper;

    @Override
    public SystemInfo queryForDetail(Long id) {
        SystemInfoEntity systemInfoEntity = systemInfoMapper.selectById(id);
        SystemInfo systemInfo = new SystemInfo();
        BeanUtils.copyProperties(systemInfoEntity, systemInfo);
        return systemInfo;
    }
}
