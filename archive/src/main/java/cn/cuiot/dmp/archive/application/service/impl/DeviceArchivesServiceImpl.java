package cn.cuiot.dmp.archive.application.service.impl;

import cn.cuiot.dmp.archive.application.service.DeviceArchivesService;
import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.persistence.mapper.DeviceArchivesMapper;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 设备档案表 服务实现类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
@Service
public class DeviceArchivesServiceImpl extends ServiceImpl<DeviceArchivesMapper, DeviceArchivesEntity> implements DeviceArchivesService {

    /**
     * 参数校验
     */
    @Override
    public void checkParams(DeviceArchivesEntity entity) {
        // 必填判断
        if (StringUtils.isBlank(entity.getDeviceName())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"设备名称不可为空");
        }
        if (Objects.isNull(entity.getLoupanId())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"所属楼盘不可为空");
        }
        if (Objects.isNull(entity.getDeviceCategory())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"设备类别不可为空");
        }
        if (StringUtils.isBlank(entity.getInstallationLocation())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"安装位置不可为空");
        }
        if (Objects.isNull(entity.getInstallationDate())){
            throw new BusinessException(ResultCode.PARAM_NOT_NULL,"安装日期不可为空");
        }
    }
}
