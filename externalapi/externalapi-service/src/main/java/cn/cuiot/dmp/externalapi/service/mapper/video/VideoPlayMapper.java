package cn.cuiot.dmp.externalapi.service.mapper.video;

import cn.cuiot.dmp.externalapi.service.entity.video.VideoPlayEntity;
import cn.cuiot.dmp.externalapi.service.entity.video.query.VideoPageQuery;
import cn.cuiot.dmp.externalapi.service.entity.video.vo.VideoPageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 监控-播放信息 mapper接口
 *
 * @author zc
 */
public interface VideoPlayMapper extends BaseMapper<VideoPlayEntity> {

    IPage<VideoPageVo> queryForPage(Page page, @Param("params") VideoPageQuery query);

}
