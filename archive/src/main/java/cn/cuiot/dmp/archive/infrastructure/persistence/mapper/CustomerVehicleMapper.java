package cn.cuiot.dmp.archive.infrastructure.persistence.mapper;

import cn.cuiot.dmp.archive.infrastructure.entity.CustomerVehicleEntity;
import cn.cuiot.dmp.archive.infrastructure.vo.CustomerVehicleVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户车辆信息 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
public interface CustomerVehicleMapper extends BaseMapper<CustomerVehicleEntity> {

    List<CustomerVehicleVo> selectByCustomerId(@Param("customerIdList") List<Long> customerIdList);
}
