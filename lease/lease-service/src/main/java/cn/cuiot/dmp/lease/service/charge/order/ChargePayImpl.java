package cn.cuiot.dmp.lease.service.charge.order;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
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
import java.util.Date;
import java.util.List;

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
    @Autowired
    private TbOrderSettlementService orderSettlementService;

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

        //2 插入收款记录
        ChargeReceiptsReceivedDto chargeReceiptsReceivedDto = new ChargeReceiptsReceivedDto();
        chargeReceiptsReceivedDto.setOnlyPrincipal(EntityConstants.YES);
        chargeReceiptsReceivedDto.setTransactionMode(0L);
        chargeReceiptsReceivedDto.setRemark("微信支付");
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
        List<TbOrderSettlement> orderSettlements = Lists.newArrayList();
        for (TbChargeReceived received : receiveds) {
            TbChargeManager charge =  chargeManager.getById(received.getChargeId());
            TbOrderSettlement tbOrderSettlement = new TbOrderSettlement();
            tbOrderSettlement.setId(IdWorker.getId());
            tbOrderSettlement.setReceivableId(received.getChargeId());
            tbOrderSettlement.setPaidUpId(received.getId());
            tbOrderSettlement.setCreateTime(new Date());
            tbOrderSettlement.setLoupanId(charge.getLoupanId());
            tbOrderSettlement.setHouseId(received.getHouseId());
            tbOrderSettlement.setCompanyId(charge.getCompanyId());
            tbOrderSettlement.setPaymentMode(EntityConstants.NO);
            tbOrderSettlement.setTransactionNo(received.getTransactionNo());
            tbOrderSettlement.setOrderId(chargeOrderPaySuccInsertDto.getOrderId());
            tbOrderSettlement.setIncomeType(EntityConstants.NO);
            tbOrderSettlement.setChargeItemId(received.getChargeItemId());
            tbOrderSettlement.setTransactionMode(received.getTransactionMode());
            tbOrderSettlement.setPayAmount(received.getTotalReceived());
            tbOrderSettlement.setSettlementTime(new Date());
            orderSettlements.add(tbOrderSettlement);
        }
        orderSettlementService.insertList(orderSettlements);
        return updateCount;
    }


    @Override
    public Long queryNeedPayCount() {
        return chargeManager.count(getNeedPayWrapper());
    }

    @Override
    public IPage<Chargeovertimeorderdto> queryNeedPayPage(Page<Chargeovertimeorderdto> page) {
        return chargeManager.queryNeedPayPage(page);
    }

    private LambdaQueryWrapper<TbChargeManager> getNeedPayWrapper() {
        return new LambdaQueryWrapper<TbChargeManager>()
                .in(TbChargeManager::getReceivbleStatus, Lists.newArrayList("0", "1"))
                .eq(TbChargeManager::getPayStatus, ChargePayStatusEnum.WAIT_PAY.getCode());
    }
}
