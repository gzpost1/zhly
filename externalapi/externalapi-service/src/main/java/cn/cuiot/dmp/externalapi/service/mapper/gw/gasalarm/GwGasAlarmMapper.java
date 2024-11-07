package cn.cuiot.dmp.externalapi.service.mapper.gw.gasalarm;

import cn.cuiot.dmp.externalapi.service.entity.gw.gasalarm.GwGasAlarmEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 格物燃气报警器 mapper 接口
 *
 * @Author: zc
 * @Date: 2024-10-28
 */
public interface GwGasAlarmMapper extends BaseMapper<GwGasAlarmEntity> {

    /**
     * 更新设备状态
     *
     * @Param entity 参数
     */
    void syncUpdateStatus(@Param("params") GwGasAlarmEntity entity);
}
