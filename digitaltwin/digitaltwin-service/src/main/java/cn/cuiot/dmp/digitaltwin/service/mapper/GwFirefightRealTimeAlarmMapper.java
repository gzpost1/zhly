package cn.cuiot.dmp.digitaltwin.service.mapper;

import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightRealTimeAlarmEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.query.GwFirefightDeviceQuery;
import cn.cuiot.dmp.digitaltwin.service.entity.vo.GwFirefightRealTimeAlarmVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 格物消防-实时报警 mapper接口
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public interface GwFirefightRealTimeAlarmMapper extends BaseMapper<GwFirefightRealTimeAlarmEntity> {

    /**
     * 查询列表
     *
     * @return List<GwFirefightRealTimeAlarmVo>
     * @Param beginDate 开始日期
     * @Param endDate 结束日期
     */
    List<GwFirefightRealTimeAlarmVo> queryAlarm(@Param("params") GwFirefightDeviceQuery query);
}
