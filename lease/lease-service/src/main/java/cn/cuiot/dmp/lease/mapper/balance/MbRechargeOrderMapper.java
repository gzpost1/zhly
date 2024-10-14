package cn.cuiot.dmp.lease.mapper.balance;

import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 充值订单 Mapper 接口
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-30
 */
public interface MbRechargeOrderMapper extends BaseMapper<MbRechargeOrder> {
    int updateRechargeOrderByCondition(@Param("param") MbRechargeOrder update);

    Long quertOrgIdByHouse(@Param("houseId") Long houseId);

    List<Long> queryHouseIdsByUser(@Param("userId") Long userId);
}
