package cn.cuiot.dmp.externalapi.service.mapper.video;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.cuiot.dmp.externalapi.service.entity.video.VideoAIAlarmEntity;
import cn.cuiot.dmp.externalapi.service.entity.video.query.VideoScreenQuery;
import cn.cuiot.dmp.externalapi.service.entity.video.vo.VideoScreenVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 监控-报警分析 mapper 接口
 *
 * @Author: zc
 * @Date: 2024-08-13
 */
public interface VideoAIAlarmMapper extends BaseMapper<VideoAIAlarmEntity> {

    List<VideoScreenVo> queryVideoAIList(@Param("params") VideoScreenQuery query);
}
