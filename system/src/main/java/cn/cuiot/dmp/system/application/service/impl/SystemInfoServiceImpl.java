package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.system.application.param.dto.SystemInfoCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.SystemInfoQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.SystemInfoVO;
import cn.cuiot.dmp.system.application.service.SystemInfoService;
import cn.cuiot.dmp.system.domain.aggregate.SystemInfo;
import cn.cuiot.dmp.system.domain.repository.SystemInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Service
public class SystemInfoServiceImpl implements SystemInfoService {

    @Autowired
    private SystemInfoRepository systemInfoRepository;

    @Override
    public SystemInfoVO queryForDetail(Long id) {
        SystemInfo systemInfo = systemInfoRepository.queryForDetail(id);
        SystemInfoVO systemInfoVO = new SystemInfoVO();
        BeanUtils.copyProperties(systemInfo, systemInfoVO);
        return systemInfoVO;
    }

    @Override
    public SystemInfoVO queryBySource(SystemInfoQueryDTO systemInfoQueryDTO) {
        SystemInfo systemInfoReq = new SystemInfo();
        BeanUtils.copyProperties(systemInfoQueryDTO, systemInfoReq);
        SystemInfo systemInfo = systemInfoRepository.queryBySource(systemInfoReq);
        if (Objects.isNull(systemInfo.getId())) {
            return new SystemInfoVO();
        }
        SystemInfoVO systemInfoVO = new SystemInfoVO();
        BeanUtils.copyProperties(systemInfo, systemInfoVO);
        return systemInfoVO;
    }

    @Override
    public int saveOrUpdateSystemInfo(SystemInfoCreateDTO systemInfoCreateDTO) {
        SystemInfo systemInfoReq = new SystemInfo();
        BeanUtils.copyProperties(systemInfoCreateDTO, systemInfoReq);
        SystemInfo systemInfo = systemInfoRepository.queryBySource(systemInfoReq);
        BeanUtils.copyProperties(systemInfoCreateDTO, systemInfo);
        if (Objects.isNull(systemInfo.getId())) {
            return systemInfoRepository.saveSystemInfo(systemInfo);
        } else {
            return systemInfoRepository.updateSystemInfo(systemInfo);
        }
    }

}
