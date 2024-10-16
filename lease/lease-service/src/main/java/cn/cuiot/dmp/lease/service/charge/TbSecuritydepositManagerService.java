package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositRefund;
import cn.cuiot.dmp.lease.enums.ChargeAbrogateTypeEnum;
import cn.cuiot.dmp.lease.enums.SecurityDepositStatusEnum;
import cn.cuiot.dmp.lease.mapper.charge.TbSecuritydepositManagerMapper;
import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import cn.cuiot.dmp.pay.service.service.service.TbOrderSettlementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TbSecuritydepositManagerService extends ServiceImpl<TbSecuritydepositManagerMapper, TbSecuritydepositManager> {
    @Autowired
    private TbChargeAbrogateService tbChargeAbrogateService;
    @Autowired
    private TbSecuritydepositRefundService securitydepositRefundService;
    @Autowired
    private TbOrderSettlementService orderSettlementService;


    public IPage<SecuritydepositManagerPageDto> queryForPage(SecuritydepositManagerQuery query) {
        return baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    /**
     * 作废
     *
     * @param entity
     * @param abrogateDesc
     */
    @Transactional(rollbackFor = Exception.class)
    public void abrogateStatus(TbSecuritydepositManager entity, String abrogateDesc) {
        // 作废
        entity.setStatus(SecurityDepositStatusEnum.CANCELLED.getCode());
        updateById(entity);

        // 作废记录
        tbChargeAbrogateService.saveData(entity.getId(), ChargeAbrogateTypeEnum.DEPOSIT.getCode(), abrogateDesc);
    }

    /**
     * 退款
     *
     * @param dto
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void refund(SecuritydepositRefundDto dto) {
        // 退款
        int updateNum = baseMapper.refund(dto);
        AssertUtil.isTrue(updateNum == 1, "账单正在支付中，请勿重复操作");

        //保存退款记录
        TbSecuritydepositRefund refund = new TbSecuritydepositRefund();
        BeanUtils.copyProperties(dto, refund);
        refund.setId(IdWorker.getId());
        refund.setRefundTime(new Date());
        securitydepositRefundService.save(refund);
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public SecuritydepositManagerDto queryForDetail(Long id) {
        TbSecuritydepositManager entity = this.getById(id);
        AssertUtil.notNull(entity, "数据不存在");
        SecuritydepositManagerDto dto = new SecuritydepositManagerDto();
        BeanUtils.copyProperties(entity, dto);

        //查询作废明细
        LambdaQueryWrapper<TbChargeAbrogate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbChargeAbrogate::getDataId, id);
        wrapper.eq(TbChargeAbrogate::getDataType, ChargeAbrogateTypeEnum.DEPOSIT.getCode());
        wrapper.orderByDesc(TbChargeAbrogate::getAbrogateTime);
        dto.setAbrogateList(tbChargeAbrogateService.list(wrapper));

        //查询退款明细
        LambdaQueryWrapper<TbSecuritydepositRefund> refundWrapper = new LambdaQueryWrapper<>();
        refundWrapper.eq(TbSecuritydepositRefund::getDepositId, id);
        refundWrapper.orderByDesc(TbSecuritydepositRefund::getRefundTime);
        dto.setSecuritydepositRefundList(securitydepositRefundService.list(refundWrapper));
        return dto;
    }

    public Integer getHouseReundableAmount(Long houseId) {
        return Optional.ofNullable(baseMapper.getHouseReundableAmount(houseId)).orElse(0);
    }

    /**
     * 押金保存
     *
     * @param createDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveData(SecuritydepositManagerInsertDto createDto) {
        TbSecuritydepositManager entity = new TbSecuritydepositManager();
        BeanUtils.copyProperties(createDto, entity);
        entity.setId(IdWorker.getId());
        entity.setStatus(SecurityDepositStatusEnum.UNPAID.getCode());
        entity.setReceivableAmountReceived(0);
        entity.setReturnedAmount(0);
        this.save(entity);
    }

    public int receivedAmount(TbSecuritydepositManager dto) {
        return baseMapper.receivedAmount(dto);
    }

    public int updateChargePayStatus(List<Long> chargeIds, Long orderId) {
        return baseMapper.updateChargePayStatus(chargeIds, orderId);
    }

    public List<ChargePayToWechatDetailDto> queryForPayToWechat(List<Long> chargeIds) {
        return baseMapper.queryForPayToWechat(chargeIds);
    }

    public int updateChargePayStatusToCancel(List<Long> chargeIds) {
        return baseMapper.updateChargePayStatusToCancel(chargeIds);
    }

    public int updateChargePayStatusToSuccsess(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto) {
        return baseMapper.updateChargePayStatusToPaySuccess(chargeOrderPaySuccInsertDto);
    }

    public IPage<Chargeovertimeorderdto> queryNeedPayPage(Page<Chargeovertimeorderdto> page) {
        return baseMapper.queryNeedPayPage(page);
    }

    public PrePayAmountAndHouseId queryNeedToPayAmount(Long chargeId) {
        return baseMapper.queryNeedToPayAmount(chargeId);
    }

    public int updateChargePayStatusToPaySuccessBYPrePay(Long chargeId, Integer needToPayAmount,Long orderId) {
        return baseMapper.updateChargePayStatusToPaySuccessBYPrePay(chargeId, needToPayAmount,orderId);
    }

    public List<TbOrderSettlement> saveReceivedAndSettlement(ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto) {
        List<TbSecuritydepositManager> tbSecuritydepositManagers = this.listByIds(chargeOrderPaySuccInsertDto.getDataIds());
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
            tbOrderSettlement.setPaymentMode(chargeOrderPaySuccInsertDto.getPaymentMode());
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
        return orderSettlements;
    }

    /**
     * 后台人工收费
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void receivedAmountByPlatform(@Valid DepositReceiptsReceivedDto dto) {
        TbSecuritydepositManager entity = this.getById(dto.getChargeId());
        AssertUtil.notNull(entity, "数据不存在");

        entity.setTransactionMode(dto.getTransactionMode());
        entity.setAccountBank(dto.getAccountBank());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setReceivedDate(new Date());
        entity.setReceivableAmountReceived(entity.getReceivableAmount());
        entity.setStatus(SecurityDepositStatusEnum.PAID_OFF.getCode());
        entity.setReceivedId(IdWorker.getId());
        entity.setPaymentMode(EntityConstants.YES);

        int count = this.receivedAmount(entity);
        AssertUtil.isTrue(count > 0, "账单正在支付中，请勿重复操作");

        //3 插入账单
        ChargeOrderPaySuccInsertDto chargeOrderPaySuccInsertDto = new ChargeOrderPaySuccInsertDto();
        chargeOrderPaySuccInsertDto.setDataIds(Lists.newArrayList(dto.getChargeId()));
        chargeOrderPaySuccInsertDto.setPaymentMode(EntityConstants.YES);
        this.saveReceivedAndSettlement(chargeOrderPaySuccInsertDto);
    }

    public IPage<Chargeovertimeorderdto> queryOverTimeOrderAndClosePage(Page<Chargeovertimeorderdto> chargeovertimeorderdtoPage) {
        return baseMapper.queryOverTimeOrderAndClosePage(chargeovertimeorderdtoPage);
    }
}
