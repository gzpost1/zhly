package cn.cuiot.dmp.lease.service.charge.order;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.NO);
        securitydepositManagerService.saveReceivedAndSettlement(chargeOrderPaySuccInsertDto);
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

    @Override
    public Integer queryNeedToPayAmount(Long chargeId) {
        return securitydepositManagerService.queryNeedToPayAmount(chargeId);
    }

    @Override
    public int updateChargePayStatusToPaySuccessBYPrePay(Long chargeId, Integer needToPayAmount, Long createUserId) {
        //1 更新订单状态为已支付
        int updateCount =  securitydepositManagerService.updateChargePayStatusToPaySuccessBYPrePay(chargeId,needToPayAmount);
        AssertUtil.isTrue(updateCount > 0, "锁定账单收款失败");

        //3 插入账单
        ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto = new ChargeOrderPaySuccInsertDto();
        chargeOrderPaySuccInsertDto.setDataIds(Lists.newArrayList(chargeId));
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.NO);
        securitydepositManagerService.saveReceivedAndSettlement(chargeOrderPaySuccInsertDto);
        return updateCount;
    }

    private LambdaQueryWrapper<TbSecuritydepositManager> getNeedPayWrapper() {
        return new LambdaQueryWrapper<TbSecuritydepositManager>()
                .eq(TbSecuritydepositManager::getStatus, EntityConstants.NO)
                .eq(TbSecuritydepositManager::getPayStatus, ChargePayStatusEnum.WAIT_PAY.getCode());
    }

    @Override
    public List<Long> getCompanyIdByChargeIds(List<Long> chargeIds) {
        return Optional.ofNullable(securitydepositManagerService.listByIds(chargeIds)).orElse(new ArrayList<>()).stream().map(TbSecuritydepositManager::getCompanyId).collect(Collectors.toList());
    }
}
