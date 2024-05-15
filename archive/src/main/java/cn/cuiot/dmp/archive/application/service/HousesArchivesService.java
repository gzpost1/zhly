package cn.cuiot.dmp.archive.application.service;

import cn.cuiot.dmp.archive.infrastructure.entity.HousesArchivesEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * <p>
 * 房屋档案表 服务类
 * </p>
 *
 * @author liujianyu
 * @since 2024-05-15
 */
public interface HousesArchivesService extends IService<HousesArchivesEntity> {

    /**
     * 参数校验
     */
    void checkParams(HousesArchivesEntity entity);

}
