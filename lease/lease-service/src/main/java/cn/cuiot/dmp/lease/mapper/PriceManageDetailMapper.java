package cn.cuiot.dmp.lease.mapper;

import cn.cuiot.dmp.lease.entity.PriceManageDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/6/24
 */
public interface PriceManageDetailMapper extends BaseMapper<PriceManageDetailEntity> {

    /**
     * 根据房屋id查询对应的最新定价
     */
    List<PriceManageDetailEntity> batchQueryHousePrice(@Param("ids") List<Long> ids);

}
