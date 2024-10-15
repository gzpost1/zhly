package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionSettingReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionSettingRspDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.HouseInfoDto;
import cn.cuiot.dmp.lease.feign.SystemToFlowService;
import cn.cuiot.dmp.lease.service.charge.ChargeHouseAndUserService;
import cn.cuiot.dmp.pay.service.service.dto.OrderSettlementQuery;
import cn.cuiot.dmp.pay.service.service.dto.OrderSettlementStatics;
import cn.cuiot.dmp.pay.service.service.entity.TbOrderSettlement;
import cn.cuiot.dmp.pay.service.service.service.TbOrderSettlementService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 结算报表
 *
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
    @Autowired
    private ChargeHouseAndUserService chargeHouseAndUserService;
    @Autowired
    private SystemToFlowService systemToFlowService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<TbOrderSettlement>> queryForPaidinPage(@RequestBody OrderSettlementQuery query) {

        LambdaQueryWrapper<TbOrderSettlement> queryWrapper = getQueryWrapper(query);
        queryWrapper.orderByDesc(TbOrderSettlement::getSettlementTime);
        IPage<TbOrderSettlement> page = orderSettlementService.page(new Page<>(query.getPageNo(), query.getPageSize()), queryWrapper);

        if (Objects.nonNull(page) && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<Long> houseIds = page.getRecords().stream().map(TbOrderSettlement::getHouseId).collect(Collectors.toList());
            List<Long> chargeItemIds = page.getRecords().stream().map(TbOrderSettlement::getChargeItemId).collect(Collectors.toList());
            List<Long> transactionItemIds = page.getRecords().stream().map(TbOrderSettlement::getTransactionMode).collect(Collectors.toList());
            //收费项目名称
            CommonOptionSettingReqDTO commonOptionSettingReqDTO = new CommonOptionSettingReqDTO();
            ArrayList<Long> selectList = Lists.newArrayList();
            selectList.addAll(chargeItemIds);
            selectList.addAll(transactionItemIds);
            commonOptionSettingReqDTO.setIdList(selectList);
            List<CommonOptionSettingRspDTO> commonOptionSettingRspDTOS = Optional.ofNullable(systemToFlowService.batchQueryCommonOptionSetting(commonOptionSettingReqDTO)).orElse(new ArrayList<>());
            commonOptionSettingRspDTOS.add(new CommonOptionSettingRspDTO(0L, "微信支付", (byte) 1));
            commonOptionSettingRspDTOS.add(new CommonOptionSettingRspDTO(1L, "预缴代扣", (byte) 2));
            Map<Long, CommonOptionSettingRspDTO> changeItemMap = commonOptionSettingRspDTOS.stream().collect(Collectors.toMap(CommonOptionSettingRspDTO::getId, Function.identity()));

            //填充名称
            List<HouseInfoDto> houseInfoDtos = chargeHouseAndUserService.getHouseInfoByIds(houseIds);
            Map<Long, HouseInfoDto> userInfoMap = null;
            if (CollectionUtils.isNotEmpty(houseInfoDtos)) {
                userInfoMap = houseInfoDtos.stream().collect(Collectors.toMap(HouseInfoDto::getHouseId, Function.identity()));
            }

            for (TbOrderSettlement record : page.getRecords()) {
                if (userInfoMap.containsKey(record.getHouseId())) {
                    record.setHouseName(userInfoMap.get(record.getHouseId()).getHouseName());
                    record.setLoupanName(userInfoMap.get(record.getHouseId()).getLoupanName());
                }

                if (changeItemMap.containsKey(record.getChargeItemId())) {
                    record.setChargeItemName(changeItemMap.get(record.getChargeItemId()).getName());
                }

                if (changeItemMap.containsKey(record.getTransactionMode())) {
                    record.setTransactionModeName(changeItemMap.get(record.getTransactionMode()).getName());
                }
            }
        }
        return IdmResDTO.success().body(page);
    }


    /**
     * 查询总计
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForStatics")
    public IdmResDTO<OrderSettlementStatics> queryForStatics(@RequestBody OrderSettlementQuery query) {

        LambdaQueryWrapper<TbOrderSettlement> queryWrapper = getQueryWrapper(query);
        return IdmResDTO.success().body(orderSettlementService.queryForStatics(queryWrapper));
    }

    @NotNull
    private static LambdaQueryWrapper<TbOrderSettlement> getQueryWrapper(OrderSettlementQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());

        LambdaQueryWrapper<TbOrderSettlement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(query.getReceivableId()), TbOrderSettlement::getReceivableId, query.getReceivableId());
        queryWrapper.like(StringUtils.isNotBlank(query.getPaidUpId()), TbOrderSettlement::getPaidUpId, query.getPaidUpId());
        queryWrapper.like(StringUtils.isNotBlank(query.getTransactionNo()), TbOrderSettlement::getTransactionNo, query.getTransactionNo());
        queryWrapper.eq(Objects.nonNull(query.getIncomeType()), TbOrderSettlement::getIncomeType, query.getIncomeType());
        queryWrapper.eq(Objects.nonNull(query.getChargeItemId()), TbOrderSettlement::getChargeItemId, query.getChargeItemId());
        queryWrapper.eq(Objects.nonNull(query.getTransactionMode()), TbOrderSettlement::getTransactionMode, query.getTransactionMode());
        queryWrapper.eq(Objects.nonNull(query.getExpenditureType()), TbOrderSettlement::getExpenditureType, query.getExpenditureType());
        queryWrapper.in(CollectionUtils.isNotEmpty(query.getLoupanIds()), TbOrderSettlement::getLoupanId, query.getLoupanIds());
        queryWrapper.in(CollectionUtils.isNotEmpty(query.getHouseIds()), TbOrderSettlement::getHouseId, query.getHouseIds());
        queryWrapper.eq(Objects.nonNull(query.getCompanyId()), TbOrderSettlement::getCompanyId, query.getCompanyId());
        queryWrapper.lt(Objects.nonNull(query.getStartTime()), TbOrderSettlement::getSettlementTime, query.getStartTime());
        queryWrapper.gt(Objects.nonNull(query.getEndTime()), TbOrderSettlement::getSettlementTime, query.getEndTime());
        return queryWrapper;
    }
}
