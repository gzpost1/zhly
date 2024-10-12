package cn.cuiot.dmp.lease.service.charge.order;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import cn.cuiot.dmp.lease.enums.ChargePayDataTypeEnum;
import cn.cuiot.dmp.lease.enums.ChargePayStatusEnum;
import cn.cuiot.dmp.lease.service.charge.TbChargeManagerService;
import cn.cuiot.dmp.lease.service.charge.TbChargeReceivedService;
import cn.cuiot.dmp.lease.service.charge.TbSecuritydepositManagerService;
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
 * @Description 押金支付实现类
 * @Date 2024/10/10 20:31
 * @Created by libo
 */
@Data
@Service
public class SecurityDepositPayImpl extends AbstrChargePay {
    @Autowired
    private TbSecuritydepositManagerService securitydepositManagerService;
    @Autowired
    private TbOrderSettlementService orderSettlementService;

    @Override
    public Byte getDataType() {
        return ChargePayDataTypeEnum.HOUSE_DEPOSIT.getCode();
    }

    /**
     * 修改为待支付
     *
     * @param chargeIds
     * @return
     */
    @Override
    public int doPay(List<Long> chargeIds, Long orderId) {
        return securitydepositManagerService.updateChargePayStatus(chargeIds, orderId);
    }

    @Override
    public List<ChargePayToWechatDetailDto> queryForPayToWechat(List<Long> chargeIds) {
        return securitydepositManagerService.queryForPayToWechat(chargeIds);
    }

    /**
     * 修改为已取消
     *
     * @param chargeIds
     * @return
     */
    @Override
    protected int doCancel(List<Long> chargeIds) {
        return securitydepositManagerService.updateChargePayStatusToCancel(chargeIds);
    }

    @Override
    protected int doPaySuccess(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto) {
        //1 更新订单状态为已支付
        int updateCount =  securitydepositManagerService.updateChargePayStatusToSuccsess(chargeOrderPaySuccInsertDto);

        //3 插入账单
        List<TbSecuritydepositManager> tbSecuritydepositManagers = securitydepositManagerService.listByIds(chargeOrderPaySuccInsertDto.getDataIds());
        List<TbOrderSettlement> orderSettlements = Lists.newArrayList();

        for (TbSecuritydepositManager received : tbSecuritydepositManagers) {
            TbOrderSettlement tbOrderSettlement = new TbOrderSettlement();
            tbOrderSettlement.setId(IdWorker.getId());
            tbOrderSettlement.setReceivableId(received.getId());
            tbOrderSettlement.setPaidUpId(received.getId());
            tbOrderSettlement.setCreateTime(new Date());
            tbOrderSettlement.setLoupanId(received.getLoupanId());
            tbOrderSettlement.setHouseId(received.getHouseId());
            tbOrderSettlement.setCompanyId(received.getCompanyId());
            tbOrderSettlement.setPaymentMode(EntityConstants.NO);
            tbOrderSettlement.setTransactionNo(received.getTransactionNo());
            tbOrderSettlement.setOrderId(chargeOrderPaySuccInsertDto.getOrderId());
            tbOrderSettlement.setIncomeType(EntityConstants.NO);
            tbOrderSettlement.setChargeItemId(received.getChargeItemId());
            tbOrderSettlement.setTransactionMode(received.getTransactionMode());
            tbOrderSettlement.setPayAmount(received.getReceivableAmount());
            tbOrderSettlement.setSettlementTime(new Date());
            orderSettlements.add(tbOrderSettlement);
        }
        orderSettlementService.insertList(orderSettlements);
        return updateCount;
    }


    @Override
    public Long queryNeedPayCount() {
        return securitydepositManagerService.count(getNeedPayWrapper());
    }

    @Override
    public IPage<Chargeovertimeorderdto> queryNeedPayPage(Page<Chargeovertimeorderdto> page) {
        return securitydepositManagerService.queryNeedPayPage(page);
    }

    private LambdaQueryWrapper<TbSecuritydepositManager> getNeedPayWrapper() {
        return new LambdaQueryWrapper<TbSecuritydepositManager>()
                .eq(TbSecuritydepositManager::getStatus, EntityConstants.NO)
                .eq(TbSecuritydepositManager::getPayStatus, ChargePayStatusEnum.WAIT_PAY.getCode());
    }
}
