package cn.cuiot.dmp.lease.service.charge;

import cn.cuiot.dmp.base.infrastructure.utils.MathTool;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;
import cn.cuiot.dmp.lease.entity.charge.TbChargeHangup;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.enums.*;
import cn.cuiot.dmp.lease.mapper.charge.TbChargeManagerMapper;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                tbChargeManager.setDueDate(DateUtil.offsetDay(DateUtil.beginOfMonth(ownershipPeriodBegin), createDto.getDueDateNum() - 1));
                //如果不等于则超过了这个月的最后一天，取这个月的最后一天
                if (DateUtil.month(tbChargeManager.getDueDate()) != DateUtil.month(ownershipPeriodBegin)) {
                    tbChargeManager.setDueDate(DateUtil.endOfMonth(ownershipPeriodBegin));
                }

            } else {
                betweenMonth += 1;
                for (long l = 0; l < betweenMonth; l++) {
                    TbChargeManager tbChargeManager = getTbChargeManager(createDto, createType, receivblePlanId);
                    //如果是第一个月开始时间为所属账期开始时间，结束为这个月的最后一天 如果是最后一个月开始时间为这个月的第一天，结束时间为所属账期结束时间
                    if (l == 0) {
                        tbChargeManager.setOwnershipPeriodBegin(ownershipPeriodBegin);
                        tbChargeManager.setOwnershipPeriodEnd(DateUtil.endOfMonth(ownershipPeriodBegin));
                    } else if (l == betweenMonth - 1) {
                        tbChargeManager.setOwnershipPeriodBegin(DateUtil.beginOfMonth(ownershipPeriodEnd));
                        tbChargeManager.setOwnershipPeriodEnd(ownershipPeriodEnd);
                    } else {
                        tbChargeManager.setOwnershipPeriodBegin(DateUtil.offsetMonth(ownershipPeriodBegin, (int) l));
                        tbChargeManager.setOwnershipPeriodEnd(DateUtil.endOfMonth(DateUtil.offsetMonth(ownershipPeriodBegin, (int) l)));
                    }

                    tbChargeManager.setDueDate(DateUtil.offsetDay(DateUtil.beginOfMonth(tbChargeManager.getOwnershipPeriodBegin()), createDto.getDueDateNum() - 1));
                    //如果不等于则超过了这个月的最后一天，取这个月的最后一天
                    if (DateUtil.month(tbChargeManager.getDueDate()) != DateUtil.month(tbChargeManager.getOwnershipPeriodBegin())) {
                        tbChargeManager.setDueDate(DateUtil.endOfMonth(tbChargeManager.getOwnershipPeriodBegin()));
                    }

                    chargeManagers.add(tbChargeManager);
                }

                saveBatch(chargeManagers);
            }

        }
    }

    /**
     * 获取TbChargeManager
     * @param createDto
     * @param createType
     * @param receivblePlanId
     * @return
     */
    private static TbChargeManager getTbChargeManager(ChargeManagerInsertVo createDto, Byte createType, Long receivblePlanId) {
        TbChargeManager entity = new TbChargeManager();
        BeanUtils.copyProperties(createDto, entity);
        entity.setId(IdWorker.getId());
        entity.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        entity.setCreateType(createType);
        entity.setReceivblePlanId(receivblePlanId);
        entity.setReceivbleStatus(ChargeReceivbleEnum.UNPAID.getCode());
        entity.setHangUpStatus(ChargeHangUpEnum.UNHANG_UP.getCode());
        entity.setAbrogateStatus(ChargeAbrogateEnum.NORMAL.getCode());
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

        //更新收款表相关
        for (TbChargeReceived received : receiveds) {
            int updateNum = baseMapper.receivedAmount(received);
            AssertUtil.isTrue(updateNum > 0, "收款失败");
        }
        //插入收款明细
        tbChargeReceivedService.insertList(receiveds);
    }

    /**
     * 获取收款明细
     * @param queryDto
     * @return
     */
    public IPage<TbChargeAbrogate> queryForAbrogatePage(ChargeHangupQueryDto queryDto) {
        return tbChargeAbrogateService.queryForPage(queryDto);
    }

    /**
     * 获取收款明细
     * @param queryDto
     * @return
     */
    public IPage<TbChargeReceived> queryForReceivedPage(ChargeHangupQueryDto queryDto) {
        return tbChargeReceivedService.queryForPage(queryDto);
    }


    public IPage<CustomerUserInfo> queryHouseCustmerPage(HouseCustomerQuery query) {
        return baseMapper.queryHouseCustmerPage(new Page(query.getPageNo(), query.getPageSize()), query);
    }
}
