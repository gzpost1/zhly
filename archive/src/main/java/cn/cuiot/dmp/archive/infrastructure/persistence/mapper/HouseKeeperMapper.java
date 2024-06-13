package cn.cuiot.dmp.archive.infrastructure.persistence.mapper;

import cn.cuiot.dmp.archive.infrastructure.entity.HouseKeeperEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 管家管理 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-07
 */
public interface HouseKeeperMapper extends BaseMapper<HouseKeeperEntity> {

    /**
     * 分页查询
     */
    IPage<HouseKeeperEntity> queryForList(Page<HouseKeeperEntity> page,
            @Param("companyId") Long companyId,
            @Param("deptPath") String deptPath, @Param("communityId") Long communityId,
            @Param("communityIdList") List<Long> communityIdList, @Param("status") Byte status);

    /**
     * 列表查询
     */
    List<HouseKeeperEntity> queryForList(@Param("companyId") Long companyId,
            @Param("deptPath") String deptPath, @Param("communityId") Long communityId,
            @Param("communityIdList") List<Long> communityIdList, @Param("status") Byte status);

}
