package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeBuildingDto;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionPlanBuildingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收费管理-催款计划（楼盘信息） mapper接口
 *
 * @author zc
 */
public interface ChargeCollectionPlanBuildingMapper extends BaseMapper<ChargeCollectionPlanBuildingEntity> {
    /**
     * 批量插入
     *
     * @Param list 列表
     */
    void batchInsert(@Param("list") List<ChargeCollectionPlanBuildingEntity> list);

    /**
     * 根据通知单id查询楼盘名称
     *
     * @return list 楼盘名称列表
     * @Param list 通知单ids
     */
    List<ChargeBuildingDto> getBuildingNamesByPlanId(@Param("list") List<Long> list);
}