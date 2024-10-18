package cn.cuiot.dmp.externalapi.service.mapper.water;


import cn.cuiot.dmp.externalapi.service.entity.water.WaterManagementEntity;
import cn.cuiot.dmp.externalapi.service.query.StatisInfoReqDTO;
import cn.cuiot.dmp.externalapi.service.vo.watermeter.WaterMeterQueryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 水表管理 Mapper 接口
 * @author pengjian
 * @since 2024-09-06
 */
public interface WaterManagementMapper extends BaseMapper<WaterManagementEntity> {


     void insertOrUpdateBatch (@Param("waterList") List<WaterManagementEntity> waterManagementEntities);

     IPage<WaterManagementEntity> queryForPage(Page<WaterManagementEntity> objectPage, @Param("query") WaterMeterQueryVO vo);

    Long queryWaterMeterCount(@Param("params") StatisInfoReqDTO params);
}
