package cn.cuiot.dmp.digitaltwin.service.mapper;

import cn.cuiot.dmp.digitaltwin.service.entity.GwFirefightAlarmConfirmationEntity;
import cn.cuiot.dmp.digitaltwin.service.entity.query.GwFirefightDeviceQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 格物消防-报警确认 mapper接口
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public interface GwFirefightAlarmConfirmationMapper extends BaseMapper<GwFirefightAlarmConfirmationEntity> {

    /**
     * 查询列表
     *
     * @Param beginDate 开始日期
     * @Param endDate 结束日期
     * @return List<GwFirefightAlarmConfirmationVo>
     */
    List<GwFirefightAlarmConfirmationEntity> queryAlarmConfirmation(@Param("params") GwFirefightDeviceQuery query);
}
