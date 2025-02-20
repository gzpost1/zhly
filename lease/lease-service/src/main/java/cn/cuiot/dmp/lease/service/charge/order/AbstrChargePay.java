package cn.cuiot.dmp.lease.service.charge.order;

import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.charge.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Date;
import java.util.List;

/**
 * @Description 订单处理抽象类
 * @Date 2024/10/10 20:23
 * @Created by libo
 */
public abstract class AbstrChargePay {

    /**
     * 获取数据类型
     *
     * @return
     */
    public abstract Byte getDataType();

    /**
     * 修改状态为待支付
     *
     * @return
     */
    public int updateChargePayStatusToNeedPay(List<Long> chargeIds, long orderId) {
        int updateCount = doPay(chargeIds, orderId);
        AssertUtil.isFalse(updateCount != chargeIds.size(), "账单正在支付中，请勿重复操作");
        return updateCount;
    }

    protected abstract int doPay(List<Long> chargeIds, Long orderId);


    /**
     * 获取待支付账单列表的价格详情
     */
    public abstract List<ChargePayToWechatDetailDto> queryForPayToWechat(List<Long> chargeIds);


    /**
     * 修改订单状态为已取消
     */
    public void updateChargePayStatusToCancel(List<Long> chargeIds) {
        int updateCount = doCancel(chargeIds);
        AssertUtil.isFalse(updateCount != chargeIds.size(), "账单正在支付中，请勿重复操作");
    }

    protected abstract int doCancel(List<Long> chargeIds);

    /**
     * 修改订单状态为已支付
     *
     * @param chargeOrderPaySuccInsertDto
     */
    public void updateChargePayStatusToPaySuccess(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto) {
        int updateCount = doPaySuccess(chargeOrderPaySuccInsertDto);
        AssertUtil.isFalse(updateCount != chargeOrderPaySuccInsertDto.getDataIds().size(), "账单正在支付中，请勿重复操作");
    }

    protected abstract int doPaySuccess(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto);

    /**
     * 获取还在处于待支付的订单总数
     */
    public abstract Long queryNeedPayCount();

    public abstract IPage<Chargeovertimeorderdto> queryNeedPayPage(Page<Chargeovertimeorderdto> objectPage);

    public abstract PrePayAmountAndHouseId queryNeedToPayAmount(Long chargeId);

    public abstract UpdateChargePayStatusToPaySuccessBYPrePayDto updateChargePayStatusToPaySuccessBYPrePay(Long chargeId, Integer needToPayAmount, Long createUserId, Long orderId);

    public abstract List<Long> getCompanyIdByChargeIds(List<Long> chargeIds);

    public abstract IPage<Chargeovertimeorderdto> queryOverTimeOrderAndClosePage(Page<Chargeovertimeorderdto> chargeovertimeorderdtoPage, Date date );
}
