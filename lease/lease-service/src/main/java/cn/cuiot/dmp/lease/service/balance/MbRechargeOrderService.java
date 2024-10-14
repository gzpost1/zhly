package cn.cuiot.dmp.lease.service.balance;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrder;
import cn.cuiot.dmp.lease.mapper.balance.MbRechargeOrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 充值订单 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-30
 */
@Service
public class MbRechargeOrderService extends ServiceImpl<MbRechargeOrderMapper, MbRechargeOrder> {

    /**
     * 变更订单
     *
     * @param updateOrder
     */
    public void updateOrder(MbRechargeOrder updateOrder) {
        int count = baseMapper.updateRechargeOrderByCondition(updateOrder);
        if (count <= 0) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,  "数据库入库失败,等待重试");
        }
    }

    public Long quertOrgIdByHouse(Long houseId){
        return baseMapper.quertOrgIdByHouse(houseId);
    }
}
