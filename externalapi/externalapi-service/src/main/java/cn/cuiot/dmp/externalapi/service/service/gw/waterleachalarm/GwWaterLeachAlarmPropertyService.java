package cn.cuiot.dmp.externalapi.service.service.gw.waterleachalarm;

import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmPropertyEntity;
import cn.cuiot.dmp.externalapi.service.mapper.gw.waterleachalarm.GwWaterLeachAlarmPropertyMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 格物-水浸报警器属性 业务层
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@Service
public class GwWaterLeachAlarmPropertyService extends ServiceImpl<GwWaterLeachAlarmPropertyMapper, GwWaterLeachAlarmPropertyEntity> {

    /**
     * 更新设备属性
     *
     * @Param entity 参数
     */
    public void createOrUpdate(GwWaterLeachAlarmPropertyEntity property) {
        GwWaterLeachAlarmPropertyEntity entity = getOne(
                new LambdaQueryWrapper<GwWaterLeachAlarmPropertyEntity>()
                        .eq(GwWaterLeachAlarmPropertyEntity::getDeviceId, property.getDeviceId())
                        .last(" LIMIT 1 "));
        entity.setDeviceData(property.getDeviceData());

        saveOrUpdate(entity);
    }
}
