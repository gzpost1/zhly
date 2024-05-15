package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.infrastructure.entity.DeviceArchivesEntity;
import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 设备档案表 服务类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
public interface DeviceArchivesService extends IService<DeviceArchivesEntity> {

    /**
     * 参数校验
     */
    void checkParams(DeviceArchivesEntity entity);
}
