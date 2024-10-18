package cn.cuiot.dmp.externalapi.service.mapper.park;


import cn.cuiot.dmp.externalapi.service.entity.park.AccessControlEntity;

import cn.cuiot.dmp.externalapi.service.query.AccessCommunityDto;
import cn.cuiot.dmp.externalapi.service.query.QueryAccessCommunity;
import cn.cuiot.dmp.externalapi.service.query.StatisInfoReqDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 门禁管理 Mapper 接口
 * @author pengjian
 * @since 2024-09-04
 */
public interface AccessControlMapper extends BaseMapper<AccessControlEntity> {

    /**
     * 批量更新或者保存
     * @param accessList
     */
    public void insertOrUpdateBatch(@Param("accessList") List<AccessControlEntity> accessList);

    Page<AccessCommunityDto> queryForPage(Page<AccessCommunityDto> page, @Param("query") QueryAccessCommunity queryAccessCommunity);

    /**
     * 查询宇泛 门禁 设备统计
     * @param params
     * @return
     */
    Long queryAccessCommunityCount(@Param("params") StatisInfoReqDTO params);
}
