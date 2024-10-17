package cn.cuiot.dmp.content.dal.mapper;


import cn.cuiot.dmp.content.dal.entity.ContentNoticeEntity;
import cn.cuiot.dmp.content.param.query.NoticPageQuery;
import cn.cuiot.dmp.content.param.req.NoticeStatisInfoReqVo;
import cn.cuiot.dmp.content.param.vo.ContentNoticeVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 公告管理 Mapper 接口
 * </p>
 *
 * @author hantingyao
 * @since 2024-05-28
 */
public interface ContentNoticeMapper extends BaseMapper<ContentNoticeEntity> {

    IPage<ContentNoticeEntity> queryForPage(IPage<ContentNoticeEntity> page, @Param("param") NoticPageQuery pageQuery, @Param("dataType") Byte dataType);

    List<ContentNoticeEntity> queryForList(@Param("param") NoticPageQuery pageQuery, @Param("dataType") Byte dataType);

    IPage<ContentNoticeVo> queryContentAdminNoticeStatistic(Page<Object> objectPage, @Param("params") NoticeStatisInfoReqVo dto);

    IPage<ContentNoticeVo> queryContentAppNoticeStatistic(Page<Object> objectPage, @Param("params") NoticeStatisInfoReqVo dto);

}
