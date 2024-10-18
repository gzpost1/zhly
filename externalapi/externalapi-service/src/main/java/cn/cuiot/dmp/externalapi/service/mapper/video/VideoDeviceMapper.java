package cn.cuiot.dmp.externalapi.service.mapper.video;

import cn.cuiot.dmp.externalapi.service.entity.video.VideoDeviceEntity;
import cn.cuiot.dmp.externalapi.service.query.video.VideoStatisInfoReqDTO;
import cn.cuiot.dmp.externalapi.service.vo.video.VideoPageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


/**
 * 监控-设备信息 mapper接口
 *
 * @Author: zc
 */
public interface VideoDeviceMapper extends BaseMapper<VideoDeviceEntity> {

    IPage<VideoPageVo> queryVideoInfoPage(Page<VideoStatisInfoReqDTO> page, @Param("params") VideoStatisInfoReqDTO params);
}
