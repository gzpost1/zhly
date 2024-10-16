package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.base.infrastructure.utils.MathTool;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;
import cn.cuiot.dmp.lease.entity.charge.TbChargeHangup;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.enums.*;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeManagerMapper;
import cn.cuiot.dmp.lease.dto.charge.PrePayAmountAndHouseId;
import cn.cuiot.dmp.lease.vo.ChargeCollectionManageVo;
import cn.cuiot.dmp.lease.vo.ChargeManagerCustomerStatisticsVo;
import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import cn.cuiot.dmp.pay.service.service.service.TbOrderSettlementService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TbChargeManagerService extends ServiceImpl<TbChargeManagerMapper, TbChargeManager> {
    @Autowired
    private TbChargeAbrogateService tbChargeAbrogateService;
    @Autowired
    private TbChargeHangupService tbChargeHangupService;
    @Autowired
    private TbChargeReceivedService tbChargeReceivedService;
    @Autowired
    private TbSecuritydepositManagerService securitydepositManagerService;
    @Autowired
    private TbOrderSettlementService orderSettlementService;


    /**
     * 分页查询缴费管理
     *
     * @param query
     * @return
     */
    public IPage<ChargeManagerPageDto> queryForPage(TbChargeManagerQuery query) {
        return baseMapper.queryForPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    /**
     * 作废
     *
     * @param entity
     * @param abrogateDesc
     */
    @Transactional(rollbackFor = Exception.class)
    public void abrogateStatus(TbChargeManager entity, String abrogateDesc) {
        // 作废
        entity.setAbrogateStatus(ChargeAbrogateEnum.ABROGATE.getCode());
        updateById(entity);

        // 作废记录
        tbChargeAbrogateService.saveData(entity.getId(), ChargeAbrogateTypeEnum.CHARGE.getCode(), abrogateDesc);
    }

    /**
     * 保存数据
     *
     * @param createDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveData(ChargeManagerInsertVo createDto, Byte createType, Long receivblePlanId) {

        //如果是临时收费
        if (Objects.equals(createDto.getChargeType(), ChargeReceivedTypeEnum.TEMPORARY_CHARGE.getCode())) {
            TbChargeManager entity = getTbChargeManager(createDto, createType, receivblePlanId);
            save(entity);
        } else {
            List<TbChargeManager> chargeManagers = new ArrayList<>();
            Date ownershipPeriodBegin = createDto.getOwnershipPeriodBegin();
            Date ownershipPeriodEnd = createDto.getOwnershipPeriodEnd();
            long betweenMonth = DateUtil.betweenMonth(ownershipPeriodBegin, ownershipPeriodEnd, true);

            if (betweenMonth == 0) {
                TbChargeManager tbChargeManager = getTbChargeManager(createDto, createType, receivblePlanId);
                setDueDate(createDto.getDueDateNum(), tbChargeManager);
                save(tbChargeManager);
            } else {
                betweenMonth += 1;
                for (long l = 0; l < betweenMonth; l++) {
                    TbChargeManager tbChargeManager = getTbChargeManager(createDto, createType, receivblePlanId);
                    //如果是第一个月开始时间为所属账期开始时间，结束为这个月的最后一天 如果是最后一个月开始时间为这个月的第一天，结束时间为所属账期结束时间
                    if (l == 0) {
                        tbChargeManager.setOwnershipPeriodBegin(ownershipPeriodBegin);
                        tbChargeManager.setOwnershipPeriodEnd(DateTimeUtil.getStartTime(DateUtil.endOfMonth(ownershipPeriodBegin)));
                    } else if (l == betweenMonth - 1) {
                        tbChargeManager.setOwnershipPeriodBegin(DateUtil.beginOfMonth(ownershipPeriodEnd));
                        tbChargeManager.setOwnershipPeriodEnd(ownershipPeriodEnd);
                    } else {
                        tbChargeManager.setOwnershipPeriodBegin(DateTimeUtil.getStartTime(DateUtil.beginOfMonth(DateUtil.offsetMonth(ownershipPeriodBegin, (int) l))));
                        tbChargeManager.setOwnershipPeriodEnd(DateTimeUtil.getStartTime(DateUtil.endOfMonth(DateUtil.offsetMonth(ownershipPeriodBegin, (int) l))));
                    }

                    setDueDate(createDto.getDueDateNum(), tbChargeManager);
                    tbChargeManager.setCreateUser(LoginInfoHolder.getCurrentUserId());
                    tbChargeManager.setCreateTime(new Date());
                    tbChargeManager.setDeleted(EntityConstants.NOT_DELETED);
                    chargeManagers.add(tbChargeManager);
                }
                baseMapper.insertList(chargeManagers);
            }
        }
    }

    /**
     * 获取指定日期时的收费时间
     *
     * @param dueDateNum
     * @param tbChargeManager
     */
    public static void setDueDate(Integer dueDateNum, TbChargeManager tbChargeManager) {
        int dayOfMonth = DateTimeUtil.dateToLocalDate(DateUtil.endOfMonth(tbChargeManager.getOwnershipPeriodBegin())).getDayOfMonth();
        if (dayOfMonth <= dueDateNum) {
            tbChargeManager.setDueDate(DateTimeUtil.getStartTime(DateUtil.endOfMonth(tbChargeManager.getOwnershipPeriodBegin())));
        } else {
            LocalDate localDate = DateTimeUtil.dateToLocalDate(tbChargeManager.getOwnershipPeriodBegin());
            tbChargeManager.setDueDate(DateTimeUtil.localDateToDate(LocalDate.of(localDate.getYear(), localDate.getMonth(), dueDateNum)));
        }
    }

    /**
     * 获取TbChargeManager
     *
     * @param createDto
     * @param createType
     * @param receivblePlanId
     * @return
     */
    public TbChargeManager getTbChargeManager(ChargeManagerInsertVo createDto, Byte createType, Long receivblePlanId) {
        TbChargeManager entity = new TbChargeManager();
        BeanUtils.copyProperties(createDto, entity);
        entity.setId(IdWorker.getId());
        entity.setCompanyId(createDto.getCompanyId());
        entity.setCreateType(createType);
        entity.setReceivblePlanId(receivblePlanId);
        entity.setReceivbleStatus(ChargeReceivbleEnum.UNPAID.getCode());
        entity.setHangUpStatus(ChargeHangUpEnum.UNHANG_UP.getCode());
        entity.setAbrogateStatus(ChargeAbrogateEnum.NORMAL.getCode());
        entity.setPayStatus(ChargePayStatusEnum.PAY_SUCCESS.getCode());
        return entity;
    }

    /**
     * 挂起/解挂
     *
     * @param entity
     * @param abrogateDesc
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateHangUpStatus(TbChargeManager entity, String abrogateDesc) {
        //挂起-》解挂 解挂-》挂起
        if (Objects.equals(ChargeHangUpEnum.HANG_UP.getCode(), entity.getHangUpStatus())) {
            entity.setHangUpStatus(ChargeHangUpEnum.UNHANG_UP.getCode());
        } else {
            entity.setHangUpStatus(ChargeHangUpEnum.HANG_UP.getCode());
        }
        updateById(entity);

        //保存挂起流水
        tbChargeHangupService.saveData(entity.getId(), entity.getHangUpStatus(), abrogateDesc);
    }

    /**
     * 获取房屋欠费等相关信息
     *
     * @param id
     * @return
     */
    public ChargeHouseDetailDto queryForHouseDetail(Long id) {
        ChargeHouseDetailDto chargeHouseDetailDto = new ChargeHouseDetailDto();
        ChargeHouseDetailDto amountDetail = Optional.ofNullable(baseMapper.queryForHouseDetail(id)).orElse(new ChargeHouseDetailDto());
        BeanUtils.copyProperties(amountDetail, chargeHouseDetailDto);

        chargeHouseDetailDto.setDepositRefundable(securitydepositManagerService.getHouseReundableAmount(chargeHouseDetailDto.getHouseId()));

        //todo 统计当前房屋的预缴余额，预缴余额=充值总金额-扣缴总金额
        chargeHouseDetailDto.setAdvanceBalance(0);
        return chargeHouseDetailDto;
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public ChargeManagerDetailDto queryForDetail(Long id) {
        ChargeManagerDetailDto chargeManagerDetailDto = new ChargeManagerDetailDto();
        TbChargeManager entity = Optional.ofNullable(this.getById(id)).orElse(new TbChargeManager());
        BeanUtils.copyProperties(entity, chargeManagerDetailDto);
        return chargeManagerDetailDto;
    }

    /**
     * 获取挂解明细分页
     *
     * @param queryDto
     * @return
     */
    public IPage<TbChargeHangup> queryForHangupPage(ChargeHangupQueryDto queryDto) {
        return tbChargeHangupService.queryForPage(queryDto);
    }

    /**
     * 收款
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    public void receivedAmount(ChargeReceiptsReceivedDto dto) {
        List<TbChargeReceived> receiveds = getTbChargeReceiveds(dto);

        //更新收款表相关
        for (TbChargeReceived received : receiveds) {
            int updateNum = baseMapper.receivedAmount(received);
            AssertUtil.isTrue(updateNum > 0, "锁定账单收款失败");
        }
        //插入收款明细
        tbChargeReceivedService.insertList(receiveds);
        //3 插入账单
        this.insertSettleMent(receiveds,EntityConstants.NO,EntityConstants.YES,null, null);
    }

    public List<TbChargeReceived> getTbChargeReceiveds(ChargeReceiptsReceivedDto dto) {
        List<TbChargeReceived> receiveds = dto.getReceivedList().stream().map(e -> {
            TbChargeReceived tbChargeReceived = new TbChargeReceived();
            tbChargeReceived.setId(IdWorker.getId());
            tbChargeReceived.setChargeId(e.getChargeId());
            tbChargeReceived.setOnlyPrincipal(dto.getOnlyPrincipal());
            tbChargeReceived.setTransactionMode(dto.getTransactionMode());
            tbChargeReceived.setAccountBank(dto.getAccountBank());
            tbChargeReceived.setAccountNumber(dto.getAccountNumber());
            tbChargeReceived.setRemark(dto.getRemark());
            tbChargeReceived.setCustomerUserId(dto.getCustomerUserId());
            tbChargeReceived.setCreateTime(new Date());
            tbChargeReceived.setCreateUser(LoginInfoHolder.getCurrentUserId());
            tbChargeReceived.setPaymentMode(EntityConstants.YES);
            tbChargeReceived.setChargeStandard(this.getById(e.getChargeId()).getChargeStandard());
            //违约金相关
            tbChargeReceived.setLiquidatedDamagesNeed(e.getLiquidatedDamagesNeed());
            tbChargeReceived.setLiquidatedDamagesRate(e.getLiquidatedDamagesRate());
            //违约金税额 违约金*税率=违约金税额
            tbChargeReceived.setLiquidatedDamagesTax(MathTool.rounding(tbChargeReceived.getLiquidatedDamagesNeed(), tbChargeReceived.getLiquidatedDamagesRate()));
            //违约金额不含税 单次：违约金-违约金*税率=违约金额不含税
            tbChargeReceived.setLiquidatedDamagesNotTax(tbChargeReceived.getLiquidatedDamagesNeed() - tbChargeReceived.getLiquidatedDamagesTax());
            //违约金实收 如果不收则为0 如果收则为应收
            //是否只收本金 0否 1是
            if (Objects.equals(EntityConstants.YES, tbChargeReceived.getOnlyPrincipal())) {
                tbChargeReceived.setLiquidatedDamagesReceived(0);
            } else {
                tbChargeReceived.setLiquidatedDamagesReceived(tbChargeReceived.getLiquidatedDamagesNeed());
            }

            //实收相关
            //实收合计
            tbChargeReceived.setTotalReceived(e.getTotalReceived());
            //本金实收 = 实收合计- 违约金实收
            tbChargeReceived.setReceivableAmountReceived(tbChargeReceived.getTotalReceived() - tbChargeReceived.getLiquidatedDamagesReceived());
            tbChargeReceived.setTotalOwe(e.getTotalOwe());

            TbChargeManager chargeManager = this.getById(e.getChargeId());
            tbChargeReceived.setCompanyId(chargeManager.getCompanyId());
            tbChargeReceived.setHouseId(chargeManager.getHouseId());
            tbChargeReceived.setChargeItemId(chargeManager.getChargeItemId());
            tbChargeReceived.setOwnershipPeriodBegin(chargeManager.getOwnershipPeriodBegin());
            tbChargeReceived.setOwnershipPeriodEnd(chargeManager.getOwnershipPeriodEnd());

            return tbChargeReceived;

        }).collect(Collectors.toList());
        return receiveds;
    }

    /**
     * 获取收款明细
     *
     * @param queryDto
     * @return
     */
    public IPage<TbChargeAbrogate> queryForAbrogatePage(ChargeHangupQueryDto queryDto) {
        return tbChargeAbrogateService.queryForPage(queryDto);
    }

    /**
     * 获取收款明细
     *
     * @param queryDto
     * @return
     */
    public IPage<TbChargeReceived> queryForReceivedPage(ChargeHangupQueryDto queryDto) {
        return tbChargeReceivedService.queryForPage(queryDto);
    }

    /**
     * 查询房屋下的所有客户
     *
     * @param query
     * @return
     */
    public IPage<CustomerUserInfo> queryHouseCustmerPage(HouseCustomerQuery query) {
        return baseMapper.queryHouseCustmerPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    public void insertList(List<TbChargeManager> saveChargeMangeList) {
        baseMapper.insertList(saveChargeMangeList);
    }

    /**
     * 催款管理分页
     */
    public IPage<ChargeCollectionManageVo> queryCollectionManagePage(ChargeCollectionManageQuery query) {
        return baseMapper.queryCollectionManagePage(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    /**
     * 用户应收统计
     */
    public ChargeManagerCustomerStatisticsVo customerStatistics(TbChargeManagerQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        //获取前一天23:59:59
        Date date = DateTimeUtil.localDateTimeToDate(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MAX)
                .withNano(999999000));
        query.setDueDateEnd(date);
        query.setReceivbleStatusList(Arrays.asList(ChargeReceivbleEnum.UNPAID.getCode(), ChargeReceivbleEnum.PAID.getCode()));
        query.setAbrogateStatus(EntityConstants.DISABLED);
        query.setHangUpStatus(EntityConstants.DISABLED);
        return baseMapper.customerStatistics(query);
    }

    /**
     * 查询用户欠费统计用于发送消息
     */
    public IPage<ChargeCollectionManageSendDto> queryUserArrearsStatistics(Page<?> page, ChargeCollectionManageSendQuery query) {
        return baseMapper.queryUserArrearsStatistics(page, query);
    }

    public IPage<AppChargeManagerDto> appChargeManager(AppChargemanagerQuery query) {
        return baseMapper.appChargeManager(new Page(query.getPageNo(), query.getPageSize()), query);
    }

    public int updateChargePayStatus(List<Long> chargeIds, Long orderId) {
        return baseMapper.updateChargePayStatus(chargeIds, orderId);
    }

    public List<ChargePayToWechatDetailDto> queryForPayToWechat(List<Long> chargeIds) {
        return baseMapper.queryForPayToWechat(chargeIds);
    }

    public IPage<Chargeovertimeorderdto> queryNeedPayPage(Page<Chargeovertimeorderdto> page) {
        return baseMapper.queryNeedPayPage(page);
    }

    public int updateChargePayStatusToSuccsess(List<Long> chargeIds) {
        return baseMapper.updateChargePayStatusToSuccsess(chargeIds);
    }

    public int updateChargePayStatusToCancel(List<Long> chargeIds) {
        return baseMapper.updateChargePayStatusToCancel(chargeIds);
    }

    public PrePayAmountAndHouseId queryNeedToPayAmount(Long chargeId) {
        return baseMapper.queryNeedToPayAmount(chargeId);
    }

    public int updateChargePayStatusToPaySuccessBYPrePay(Long chargeId, Integer needToPayAmount) {
        return baseMapper.updateChargePayStatusToPaySuccessBYPrePay(chargeId, needToPayAmount);
    }

    /**
     * 插入结算报表
     *
     * @param receiveds  收款记录
     * @param incomeType   收支类型 0收入 1支出
     * @param paymentMode  收款方式 0平台 1人工
     * @param orderId   微信支付订单id
     * @param charge  应收账款id
     */
    public void insertSettleMent(List<TbChargeReceived> receiveds, Byte incomeType, Byte paymentMode, Long orderId, TbChargeManager charge) {
        List<TbOrderSettlement> orderSettlements = Lists.newArrayList();
        for (TbChargeReceived received : receiveds) {
            if (Objects.isNull(charge)) {
                charge = this.getById(received.getChargeId());
            }
            TbOrderSettlement tbOrderSettlement = new TbOrderSettlement();
            tbOrderSettlement.setId(IdWorker.getId());
            tbOrderSettlement.setReceivableId(received.getChargeId());
            tbOrderSettlement.setPaidUpId(received.getId());
            tbOrderSettlement.setCreateTime(new Date());
            tbOrderSettlement.setLoupanId(charge.getLoupanId());
            tbOrderSettlement.setHouseId(received.getHouseId());
            tbOrderSettlement.setCompanyId(charge.getCompanyId());
            tbOrderSettlement.setPaymentMode(paymentMode);
            tbOrderSettlement.setTransactionNo(received.getTransactionNo());
            tbOrderSettlement.setOrderId(orderId);
            tbOrderSettlement.setIncomeType(incomeType);
            tbOrderSettlement.setChargeItemId(received.getChargeItemId());
            tbOrderSettlement.setTransactionMode(received.getTransactionMode());
            tbOrderSettlement.setPayAmount(received.getTotalReceived());
            tbOrderSettlement.setSettlementTime(new Date());
            orderSettlements.add(tbOrderSettlement);
        }
        orderSettlementService.insertList(orderSettlements);
    }

    public IPage<Chargeovertimeorderdto> queryOverTimeOrderAndClosePage(Page<Chargeovertimeorderdto> chargeovertimeorderdtoPage) {
        return baseMapper.queryOverTimeOrderAndClosePage(chargeovertimeorderdtoPage);
    }
}
