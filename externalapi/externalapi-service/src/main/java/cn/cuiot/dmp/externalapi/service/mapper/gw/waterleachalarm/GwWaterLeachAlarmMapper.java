package cn.cuiot.dmp.externalapi.service.mapper.gw.waterleachalarm;

import cn.cuiot.dmp.externalapi.service.entity.gw.waterleachalarm.GwWaterLeachAlarmEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 格物-水浸报警器 mapper 接口
 *
 * @Author: zc
 * @Date: 2024-10-22
 */
public interface GwWaterLeachAlarmMapper extends BaseMapper<GwWaterLeachAlarmEntity> {

    /**
     * 更新设备状态
     *
     * @Param entity 参数
     */
    void syncUpdateStatus(@Param("params") GwWaterLeachAlarmEntity entity);
}