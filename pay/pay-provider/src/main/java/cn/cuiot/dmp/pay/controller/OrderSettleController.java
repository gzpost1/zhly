package cn.cuiot.dmp.pay.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.pay.service.service.dto.OrderSettlementQuery;
import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import cn.cuiot.dmp.pay.service.service.service.TbOrderSettlementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description 结算报表
 * @Date 2024/10/11 16:46
 * @Created by libo
 */
@Slf4j
@RestController
@RequestMapping("/orderSettlement")
public class OrderSettleController {
    @Autowired
    private TbOrderSettlementService orderSettlementService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<TbOrderSettlement>> queryForPaidinPage(@RequestBody OrderSettlementQuery query) {

        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        LambdaQueryWrapper<TbOrderSettlement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(query.getReceivableId()), TbOrderSettlement::getReceivableId, query.getReceivableId());
        queryWrapper.like(StringUtils.isNotBlank(query.getPaidUpId()), TbOrderSettlement::getPaidUpId, query.getPaidUpId());
        queryWrapper.like(StringUtils.isNotBlank(query.getTransactionNo()), TbOrderSettlement::getTransactionNo, query.getTransactionNo());
        queryWrapper.eq(Objects.nonNull(query.getIncomeType()), TbOrderSettlement::getIncomeType, query.getIncomeType());
        queryWrapper.eq(Objects.nonNull(query.getChargeItemId()), TbOrderSettlement::getChargeItemId, query.getChargeItemId());
        queryWrapper.eq(Objects.nonNull(query.getTransactionMode()), TbOrderSettlement::getTransactionMode, query.getTransactionMode());
        queryWrapper.eq(Objects.nonNull(query.getExpenditureType()), TbOrderSettlement::getExpenditureType, query.getExpenditureType());
        queryWrapper.eq(Objects.nonNull(query.getLoupanId()), TbOrderSettlement::getLoupanId, query.getLoupanId());
        queryWrapper.eq(Objects.nonNull(query.getHouseId()), TbOrderSettlement::getHouseId, query.getHouseId());
        queryWrapper.eq(Objects.nonNull(query.getCompanyId()), TbOrderSettlement::getCompanyId, query.getCompanyId());
        queryWrapper.lt(Objects.nonNull(query.getStartTime()), TbOrderSettlement::getSettlementTime, query.getStartTime());
        queryWrapper.gt(Objects.nonNull(query.getEndTime()), TbOrderSettlement::getSettlementTime, query.getEndTime());
        queryWrapper.orderByDesc(TbOrderSettlement::getSettlementTime);
        IPage<TbOrderSettlement> page = orderSettlementService.page(new Page<>(query.getPageNo(), query.getPageSize()), queryWrapper);

        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<Long> houseIds = page.getRecords().stream().map(TbOrderSettlement::getHouseId).collect(Collectors.toList());
            List<Long> loanIds = page.getRecords().stream().map(TbOrderSettlement::getLoupanId).collect(Collectors.toList());
            List<Long> chargeItemIds = page.getRecords().stream().map(TbOrderSettlement::getChargeItemId).collect(Collectors.toList());
            List<Long> transactionItemIds = page.getRecords().stream().map(TbOrderSettlement::getTransactionMode).collect(Collectors.toList());

            //填充名称
        }
        return IdmResDTO.success().body(page);
    }
}
