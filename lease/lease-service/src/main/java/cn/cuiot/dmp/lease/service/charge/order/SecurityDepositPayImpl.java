package cn.cuiot.dmp.lease.service.charge.order;

import cn.cuiot.dmp.base.infrastructure.utils.MathTool;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import cn.cuiot.dmp.lease.enums.ChargePayDataTypeEnum;
import cn.cuiot.dmp.lease.enums.ChargePayStatusEnum;
import cn.cuiot.dmp.lease.service.charge.TbSecuritydepositManagerService;
import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import cn.cuiot.dmp.pay.service.service.service.TbOrderSettlementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Slf4j
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

        Long receivedId = IdWorker.getId();
        chargeOrderPaySuccInsertDto.setReceivedId(receivedId);
        int updateCount = securitydepositManagerService.updateChargePayStatusToSuccsess(chargeOrderPaySuccInsertDto);

        //3 插入账单
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.NO);
        List<TbOrderSettlement> tbSecuritydepositManagers = securitydepositManagerService.saveReceivedAndSettlement(chargeOrderPaySuccInsertDto);
        //4 插入微信支付手续费
        if (CollectionUtils.isNotEmpty(tbSecuritydepositManagers)) {

            List<TbOrderSettlement> insertPayComssion = new ArrayList<>();
            for (TbOrderSettlement tbSecuritydepositManager : tbSecuritydepositManagers) {
                int i = MathTool.percentCalculate(tbSecuritydepositManager.getPayAmount(), chargeOrderPaySuccInsertDto.getPayRate());
                if (i > 0) {
                    TbOrderSettlement tbOrderSettlement = new TbOrderSettlement();
                    BeanUtils.copyProperties(tbSecuritydepositManager, tbOrderSettlement);
                    tbOrderSettlement.setId(IdWorker.getId());
                    tbOrderSettlement.setIncomeType(EntityConstants.YES);
                    tbOrderSettlement.setPayAmount(i);
                    insertPayComssion.add(tbOrderSettlement);
                }
            }

            if (CollectionUtils.isNotEmpty(insertPayComssion)) {
                orderSettlementService.insertList(insertPayComssion);
            }
        }

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
    public PrePayAmountAndHouseId queryNeedToPayAmount(Long chargeId) {
        return securitydepositManagerService.queryNeedToPayAmount(chargeId);
    }

    @Override
    public UpdateChargePayStatusToPaySuccessBYPrePayDto updateChargePayStatusToPaySuccessBYPrePay(Long chargeId, Integer needToPayAmount, Long createUserId, Long orderId) {
        UpdateChargePayStatusToPaySuccessBYPrePayDto prePayDto = new UpdateChargePayStatusToPaySuccessBYPrePayDto();
        //1 更新订单状态为已支付
        int updateCount = securitydepositManagerService.updateChargePayStatusToPaySuccessBYPrePay(chargeId, needToPayAmount, orderId);
        prePayDto.setUpdateCount(updateCount);
        AssertUtil.isTrue(updateCount > 0, "锁定账单收款失败");

        //3 插入账单
        ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto = new ChargeOrderPaySuccInsertDto();
        chargeOrderPaySuccInsertDto.setDataIds(Lists.newArrayList(chargeId));
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.NO);
        chargeOrderPaySuccInsertDto.setOrderId(orderId);
        securitydepositManagerService.saveReceivedAndSettlement(chargeOrderPaySuccInsertDto);
        return prePayDto;
    }

    private LambdaQueryWrapper<TbSecuritydepositManager> getNeedPayWrapper() {
        return new LambdaQueryWrapper<TbSecuritydepositManager>()
                .eq(TbSecuritydepositManager::getStatus, EntityConstants.NO)
                .eq(TbSecuritydepositManager::getPayStatus, ChargePayStatusEnum.WAIT_PAY.getCode());
    }

    @Override
    public List<Long> getCompanyIdByChargeIds(List<Long> chargeIds) {
        return Optional.ofNullable(securitydepositManagerService.listByIds(chargeIds)).orElse(new ArrayList<>()).stream().map(TbSecuritydepositManager::getCompanyId).distinct().collect(Collectors.toList());
    }

    @Override
    public IPage<Chargeovertimeorderdto> queryOverTimeOrderAndClosePage(Page<Chargeovertimeorderdto> chargeovertimeorderdtoPage, Date date) {
        return securitydepositManagerService.queryOverTimeOrderAndClosePage(chargeovertimeorderdtoPage,date);
    }
}
