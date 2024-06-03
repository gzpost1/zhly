package cn.cuiot.dmp.content.dal.mapper;


import cn.cuiot.dmp.content.dal.entity.ContentImgTextEntity;
import cn.cuiot.dmp.content.param.query.ContentImgTextPageQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 图文管理 Mapper 接口
 * </p>
 *
 * @author hantingyao
 * @since 2024-05-28
 */
public interface ContentImgTextMapper extends BaseMapper<ContentImgTextEntity> {

    List<ContentImgTextEntity> queryForList(@Param("param") ContentImgTextPageQuery pageQuery, @Param("dataType") Byte dataType);

    IPage<ContentImgTextEntity> queryForPage(IPage<ContentImgTextEntity> page, @Param("param") ContentImgTextPageQuery pageQuery, @Param("dataType") Byte dataType);
}
