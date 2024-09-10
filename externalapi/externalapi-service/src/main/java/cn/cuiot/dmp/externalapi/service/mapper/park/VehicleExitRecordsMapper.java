package cn.cuiot.dmp.externalapi.service.mapper.park;


import cn.cuiot.dmp.externalapi.service.entity.park.VehicleExitRecordsEntity;
import cn.cuiot.dmp.externalapi.service.vendor.park.query.VehicleExitRecordsQuery;
import cn.cuiot.dmp.externalapi.service.vendor.park.vo.VehicleExitVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author pengjian
 * @since 2024-09-09
 */
public interface VehicleExitRecordsMapper extends BaseMapper<VehicleExitRecordsEntity> {

    void insertOrUpdateBatch(@Param("carList") List<VehicleExitRecordsEntity> carList);

    IPage<VehicleExitVO> queryForPage(Page<VehicleExitVO> objectPage, VehicleExitRecordsQuery query);
}
