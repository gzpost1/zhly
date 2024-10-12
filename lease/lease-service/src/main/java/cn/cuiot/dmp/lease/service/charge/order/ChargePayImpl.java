package cn.cuiot.dmp.lease.service.charge.order;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeOrder;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.enums.ChargePayDataTypeEnum;
import cn.cuiot.dmp.lease.enums.ChargePayStatusEnum;
import cn.cuiot.dmp.lease.service.charge.TbChargeManagerService;
import cn.cuiot.dmp.lease.service.charge.TbChargeReceivedService;
import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import cn.cuiot.dmp.pay.service.service.service.TbOrderSettlementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 房屋账单支付实现类
 * @Date 2024/10/10 20:31
 * @Created by libo
 */
@Data
@Service
public class ChargePayImpl extends AbstrChargePay {
    @Autowired
    private TbChargeManagerService chargeManager;
    @Autowired
    private TbChargeReceivedService tbChargeReceivedService;
    @Override
    public Byte getDataType() {
        return ChargePayDataTypeEnum.HOUSE_BILL.getCode();
    }

    /**
     * 修改为待支付
     *
     * @param chargeIds
     * @return
     */
    @Override
    public int doPay(List<Long> chargeIds, Long orderId) {
        return chargeManager.updateChargePayStatus(chargeIds, orderId);
    }

    @Override
    public List<ChargePayToWechatDetailDto> queryForPayToWechat(List<Long> chargeIds) {
        return chargeManager.queryForPayToWechat(chargeIds);
    }

    /**
     * 修改为已取消
     *
     * @param chargeIds
     * @return
     */
    @Override
    protected int doCancel(List<Long> chargeIds) {
        return chargeManager.updateChargePayStatusToCancel(chargeIds);
    }

    @Override
    protected int doPaySuccess(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto) {
        //1 更新订单状态为已支付
        int updateCount =  chargeManager.updateChargePayStatusToSuccsess(chargeOrderPaySuccInsertDto.getDataIds());

        chargeOrderPaySuccInsertDto.setTransactionMode(0L);
        chargeOrderPaySuccInsertDto.setRemark("微信支付");
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.NO);
        //2 插入收款记录
        insertReceivedAndSettlement(chargeOrderPaySuccInsertDto);
        return updateCount;
    }

    private void insertReceivedAndSettlement(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto) {
        ChargeReceiptsReceivedDto chargeReceiptsReceivedDto = new ChargeReceiptsReceivedDto();
        chargeReceiptsReceivedDto.setOnlyPrincipal(EntityConstants.YES);
        chargeReceiptsReceivedDto.setTransactionMode(chargeOrderPaySuccInsertDto.getTransactionMode());
        chargeReceiptsReceivedDto.setRemark(chargeOrderPaySuccInsertDto.getRemark());
        chargeReceiptsReceivedDto.setCustomerUserId(chargeOrderPaySuccInsertDto.getOrder().getCreateUser());

        List<ChargeReceiptsReceivedInsertDetailDto> receivedList = Lists.newArrayList();
        for (ChargePayToWechatDetailDto chargePayToWechatDetailDto : chargeOrderPaySuccInsertDto.getOrder().getOrderDetail()) {
            ChargeReceiptsReceivedInsertDetailDto chargeReceiptsReceivedInsertDetailDto = new ChargeReceiptsReceivedInsertDetailDto();
            chargeReceiptsReceivedInsertDetailDto.setChargeId(chargePayToWechatDetailDto.getChargeId());
            chargeReceiptsReceivedInsertDetailDto.setTotalReceived(chargePayToWechatDetailDto.getChargeAmount());
            chargeReceiptsReceivedInsertDetailDto.setLiquidatedDamagesNeed(0);
            chargeReceiptsReceivedInsertDetailDto.setLiquidatedDamagesRate(BigDecimal.ZERO);
            chargeReceiptsReceivedInsertDetailDto.setTotalOwe(chargePayToWechatDetailDto.getChargeAmount());
            receivedList.add(chargeReceiptsReceivedInsertDetailDto);
        }
        chargeReceiptsReceivedDto.setReceivedList(receivedList);

        List<TbChargeReceived> receiveds = chargeManager.getTbChargeReceiveds(chargeReceiptsReceivedDto);
        receiveds.forEach(k -> {
            k.setCreateUser(chargeOrderPaySuccInsertDto.getOrder().getCreateUser());
            k.setCustomerUserId(chargeOrderPaySuccInsertDto.getOrder().getCreateUser());
            k.setPaymentMode(EntityConstants.NO);
            k.setTransactionMode(0L);
            k.setTransactionNo(chargeOrderPaySuccInsertDto.getTransactionNo());
        });

        tbChargeReceivedService.insertList(receiveds);

        //3 插入账单
        chargeManager.insertSettleMent(receiveds,EntityConstants.NO,EntityConstants.NO,chargeOrderPaySuccInsertDto.getOrderId(), null);
    }


    @Override
    public Long queryNeedPayCount() {
        return chargeManager.count(getNeedPayWrapper());
    }

    @Override
    public IPage<Chargeovertimeorderdto> queryNeedPayPage(Page<Chargeovertimeorderdto> page) {
        return chargeManager.queryNeedPayPage(page);
    }

    @Override
    public Integer queryNeedToPayAmount(Long chargeId) {
        return chargeManager.queryNeedToPayAmount(chargeId);
    }

    @Override
    public int updateChargePayStatusToPaySuccessBYPrePay(Long chargeId, Integer needToPayAmount,Long createUserId) {
        int updateNum = chargeManager.updateChargePayStatusToPaySuccessBYPrePay(chargeId, needToPayAmount);
        AssertUtil.isTrue(updateNum > 0, "锁定账单收款失败");


        //插入收款记录和账单
        ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto = new ChargeOrderPaySuccInsertDto();
        chargeOrderPaySuccInsertDto.setDataIds(Lists.newArrayList(chargeId));
        chargeOrderPaySuccInsertDto.setTransactionMode(1L);
        chargeOrderPaySuccInsertDto.setRemark("用户微信调用预缴代扣");
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.NO);

        TbChargeOrder order = new  TbChargeOrder();
        order.setCreateTime(new Date());
        order.setCreateUser(createUserId);
        order.setDataType(getDataType());
        chargeOrderPaySuccInsertDto.setOrder(order);

        List<ChargePayToWechatDetailDto> orderDetail = Lists.newArrayList();
        orderDetail.add(new ChargePayToWechatDetailDto(chargeId, needToPayAmount));
        order.setOrderDetail(orderDetail);

        insertReceivedAndSettlement(chargeOrderPaySuccInsertDto);

        return updateNum;
    }

    @Override
    public List<Long> getCompanyIdByChargeIds(List<Long> chargeIds) {
        return Optional.ofNullable(chargeManager.listByIds(chargeIds)).orElse(new ArrayList<>()).stream().map(TbChargeManager::getCompanyId).collect(Collectors.toList());
    }

    private LambdaQueryWrapper<TbChargeManager> getNeedPayWrapper() {
        return new LambdaQueryWrapper<TbChargeManager>()
                .in(TbChargeManager::getReceivbleStatus, Lists.newArrayList("0", "1"))
                .eq(TbChargeManager::getPayStatus, ChargePayStatusEnum.WAIT_PAY.getCode());
    }
}
