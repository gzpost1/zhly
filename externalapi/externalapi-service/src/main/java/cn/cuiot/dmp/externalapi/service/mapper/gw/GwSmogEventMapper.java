package cn.cuiot.dmp.externalapi.service.mapper.gw;


import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogEventEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogEventQuery;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwSmogEventPageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 格物烟雾报警器事件信息 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-24
 */
public interface GwSmogEventMapper extends BaseMapper<GwSmogEventEntity> {
    IPage<GwSmogEventPageVo> queryForPage(Page page, @Param("param") GwSmogEventQuery query);
}
