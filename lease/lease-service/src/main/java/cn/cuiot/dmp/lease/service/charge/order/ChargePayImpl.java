package cn.cuiot.dmp.lease.service.charge.order;

import cn.cuiot.dmp.base.infrastructure.utils.MathTool;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeOrder;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.enums.ChargePayDataTypeEnum;
import cn.cuiot.dmp.lease.enums.ChargePayStatusEnum;
import cn.cuiot.dmp.lease.service.charge.TbChargeManagerService;
import cn.cuiot.dmp.lease.service.charge.TbChargeReceivedService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
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
        Date now = new Date();
        chargeOrderPaySuccInsertDto.setPayTime(now);
        int updateCount = chargeManager.updateChargePayStatusToSuccsess(chargeOrderPaySuccInsertDto.getDataIds(), now);

        chargeOrderPaySuccInsertDto.setTransactionMode(0L);
        chargeOrderPaySuccInsertDto.setRemark("微信支付");
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.NO);
        //2 插入收款记录
        insertReceivedAndSettlement(chargeOrderPaySuccInsertDto);
        return updateCount;
    }

    private List<Long> insertReceivedAndSettlement(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto) {
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
            k.setTransactionNo(chargeOrderPaySuccInsertDto.getTransactionNo());
            k.setOrderId(chargeOrderPaySuccInsertDto.getOrderId());
            k.setCreateTime(chargeOrderPaySuccInsertDto.getPayTime());
        });

        tbChargeReceivedService.insertList(receiveds);

        //3 插入账单
        chargeManager.insertSettleMent(receiveds, EntityConstants.NO, EntityConstants.NO, chargeOrderPaySuccInsertDto.getOrderId(), null);

        //4 插入支付手续费记录
        if (Objects.equals(chargeOrderPaySuccInsertDto.getTransactionMode(), Long.valueOf(0L))) {
            List<TbChargeReceived> platformCommissions = JSONObject.parseObject(JsonUtil.writeValueAsString(receiveds), new com.alibaba.fastjson.TypeReference<List<TbChargeReceived>>() {
            });
            List<TbChargeReceived> insertPayCOmissions = new ArrayList<>();

            for (TbChargeReceived received : platformCommissions) {
                int platformCommission = MathTool.percentCalculate(received.getTotalReceived(), chargeOrderPaySuccInsertDto.getPayRate());
                if (platformCommission > 0) {
                    received.setTotalReceived(platformCommission);
                    insertPayCOmissions.add(received);
                }
            }
            if (CollectionUtils.isNotEmpty(insertPayCOmissions)) {
                chargeManager.insertSettleMent(insertPayCOmissions, EntityConstants.YES, EntityConstants.NO, chargeOrderPaySuccInsertDto.getOrderId(), null);
            }
        }

        return receiveds.stream().map(TbChargeReceived::getId).collect(Collectors.toList());
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
    public PrePayAmountAndHouseId queryNeedToPayAmount(Long chargeId) {
        return chargeManager.queryNeedToPayAmount(chargeId);
    }

    @Override
    public UpdateChargePayStatusToPaySuccessBYPrePayDto updateChargePayStatusToPaySuccessBYPrePay(Long chargeId, Integer needToPayAmount, Long createUserId, Long orderId) {
        UpdateChargePayStatusToPaySuccessBYPrePayDto prePayDto = new UpdateChargePayStatusToPaySuccessBYPrePayDto();

        int updateNum = chargeManager.updateChargePayStatusToPaySuccessBYPrePay(chargeId, needToPayAmount, orderId);
        prePayDto.setUpdateCount(updateNum);
        AssertUtil.isTrue(updateNum > 0, "锁定账单收款失败");


        //插入收款记录和账单
        ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto = new ChargeOrderPaySuccInsertDto();
        chargeOrderPaySuccInsertDto.setDataIds(Lists.newArrayList(chargeId));
        chargeOrderPaySuccInsertDto.setTransactionMode(1L);
        chargeOrderPaySuccInsertDto.setRemark("用户微信调用预缴代扣");
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.NO);
        chargeOrderPaySuccInsertDto.setOrderId(orderId);

        TbChargeOrder order = new TbChargeOrder();
        order.setCreateTime(new Date());
        order.setCreateUser(createUserId);
        order.setDataType(getDataType());
        chargeOrderPaySuccInsertDto.setOrder(order);

        List<ChargePayToWechatDetailDto> orderDetail = Lists.newArrayList();
        orderDetail.add(new ChargePayToWechatDetailDto(chargeId, needToPayAmount, null));
        order.setOrderDetail(orderDetail);

        List<Long> receiptIds = insertReceivedAndSettlement(chargeOrderPaySuccInsertDto);
        prePayDto.setChargeReceivedId(receiptIds.get(0));
        return prePayDto;
    }

    @Override
    public List<Long> getCompanyIdByChargeIds(List<Long> chargeIds) {
        return Optional.ofNullable(chargeManager.listByIds(chargeIds)).orElse(new ArrayList<>()).stream().map(TbChargeManager::getCompanyId).distinct().collect(Collectors.toList());
    }

    @Override
    public IPage<Chargeovertimeorderdto> queryOverTimeOrderAndClosePage(Page<Chargeovertimeorderdto> chargeovertimeorderdtoPage, Date date) {
        return chargeManager.queryOverTimeOrderAndClosePage(chargeovertimeorderdtoPage, date);
    }

    private LambdaQueryWrapper<TbChargeManager> getNeedPayWrapper() {
        return new LambdaQueryWrapper<TbChargeManager>()
                .in(TbChargeManager::getReceivbleStatus, Lists.newArrayList("0", "1"))
                .ne(TbChargeManager::getPayStatus, ChargePayStatusEnum.WAIT_PAY.getCode());
    }
}
