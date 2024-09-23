package cn.cuiot.dmp.externalapi.service.mapper.park;


import cn.cuiot.dmp.externalapi.service.entity.park.GateManagementEntity;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.GateManagementQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.GageManagePageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 道闸管理 Mapper 接口
 * @author pengjian
 * @since 2024-09-09
 */
public interface GateManagementMapper extends BaseMapper<GateManagementEntity> {

    void insertOrUpdateBatch(@Param("list") List<GateManagementEntity> list);

    IPage<GageManagePageVO> queryForPage(Page<GageManagePageVO> page,@Param("query") GateManagementQuery query);
}
