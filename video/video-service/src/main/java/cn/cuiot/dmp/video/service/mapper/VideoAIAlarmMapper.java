package cn.cuiot.dmp.video.service.mapper;

import cn.cuiot.dmp.video.service.entity.VideoAIAlarmEntity;
import cn.cuiot.dmp.video.service.entity.query.VideoScreenQuery;
import cn.cuiot.dmp.video.service.entity.vo.VideoScreenVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
