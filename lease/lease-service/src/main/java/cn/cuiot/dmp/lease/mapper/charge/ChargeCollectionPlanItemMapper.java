package cn.cuiot.dmp.lease.mapper.charge;

import cn.cuiot.dmp.lease.dto.charge.ChargeChargeItemDto;
import cn.cuiot.dmp.lease.entity.charge.ChargeCollectionPlanItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收费管理-催款计划（收费项目） mapper接口
 *
 * @author zc
 */
public interface ChargeCollectionPlanItemMapper extends BaseMapper<ChargeCollectionPlanItemEntity> {
    /**
     * 批量插入
     *
     * @Param list 列表
     */
    void batchInsert(@Param("list") List<ChargeCollectionPlanItemEntity> list);

    /**
     * 根据通知单id查询楼盘名称
     *
     * @return list 楼盘名称列表
     * @Param list 通知单ids
     */
    List<ChargeChargeItemDto> getChargeItemNamesByPlanId(@Param("list") List<Long> list);
}