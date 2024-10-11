package cn.cuiot.dmp.pay.service.service.service;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.pay.service.service.entity.PayOrderEntity;
import cn.cuiot.dmp.pay.service.service.mapper.PayOrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 支付订单表 服务类
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-10
 */
@Service
public class PayOrderService extends ServiceImpl<PayOrderMapper, PayOrderEntity> {


    /**
     * 按渠道查询订单
     *
     * @param outOrderId
     * @return
     */
    public PayOrderEntity queryForDetailByOutOrderId(String outOrderId, Long orderId,List<Byte> status) {
        if (StringUtils.isBlank(outOrderId) && Objects.isNull(orderId)) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR, "渠道订单号与平台订单号至少需要一个");
        }
        LambdaQueryWrapper<PayOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(outOrderId),PayOrderEntity::getOutOrderId,outOrderId);
        queryWrapper.eq(!Objects.isNull(orderId),PayOrderEntity::getOrderId,orderId);
        queryWrapper.eq(CollectionUtils.isNotEmpty(status),PayOrderEntity::getStatus,status);
        List<PayOrderEntity> list = this.list(queryWrapper);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
