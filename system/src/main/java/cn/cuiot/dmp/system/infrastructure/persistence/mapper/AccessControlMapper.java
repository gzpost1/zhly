package cn.cuiot.dmp.system.infrastructure.persistence.mapper;


import cn.cuiot.dmp.system.infrastructure.entity.AccessControlEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.AccessCommunityDto;
import cn.cuiot.dmp.system.infrastructure.entity.vo.QueryAccessCommunity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    Page<AccessCommunityDto> queryForPage(Page<AccessCommunityDto> page,@Param("query") QueryAccessCommunity queryAccessCommunity);
}
