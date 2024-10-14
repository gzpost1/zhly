package cn.cuiot.dmp.lease.service.balance;

import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrder;
import cn.cuiot.dmp.lease.entity.balance.MbRechargeOrderChange;
import cn.cuiot.dmp.lease.enums.MbRechargeOrderStatus;
import cn.cuiot.dmp.lease.mapper.balance.MbRechargeOrderChangeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 充值订单变更记录 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2023-11-30
 */
@Service
public class MbRechargeOrderChangeService extends ServiceImpl<MbRechargeOrderChangeMapper, MbRechargeOrderChange> {


    public void orderChange(MbRechargeOrder updateOrder, String changeUser){
        MbRechargeOrderChange orderChange = MbRechargeOrderChange.builder()
                .orderId(updateOrder.getOrderId())
                .changeMsg(MbRechargeOrderStatus.parseMessage(updateOrder.getStatus()))
                .changeTime(new Date())
                .changeUser(changeUser)
                .status(updateOrder.getStatus())
                .build();
        this.save(orderChange);
    }
}
