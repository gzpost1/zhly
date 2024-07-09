package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.enums.CustomerIdentityTypeEnum;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import cn.cuiot.dmp.lease.enums.ChargeTypeEnum;
import cn.cuiot.dmp.lease.enums.SecurityDepositStatusEnum;
import cn.cuiot.dmp.lease.service.charge.ChargeHouseAndUserService;
import cn.cuiot.dmp.lease.service.charge.ChargeInfoFillService;
import cn.cuiot.dmp.lease.service.charge.TbChargeAbrogateService;
import cn.cuiot.dmp.lease.service.charge.TbSecuritydepositManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 押金管理
 *
 * @Description 押金管理
 * @Date 2024/6/14 10:26
 * @Created by libo
 */
@RestController
@RequestMapping("/securitydepositManager")
public class SecuritydepositManagerController {

    @Autowired
    private TbSecuritydepositManagerService securitydepositManagerService;
    @Autowired
    private ChargeHouseAndUserService chargeHouseAndUserService;
    @Autowired
    private ChargeInfoFillService chargeInfoFillService;
    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<SecuritydepositManagerPageDto>> queryForPage(@RequestBody SecuritydepositManagerQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<SecuritydepositManagerPageDto> chargeManagerPageDtoIPage = securitydepositManagerService.queryForPage(query);

        if (Objects.nonNull(chargeManagerPageDtoIPage) && CollectionUtils.isNotEmpty(chargeManagerPageDtoIPage.getRecords())) {

            List<Long> userIds = chargeManagerPageDtoIPage.getRecords().stream().map(SecuritydepositManagerPageDto::getCustomerUserId).distinct().collect(Collectors.toList());
            List<CustomerUserInfo> userInfoByIds = chargeHouseAndUserService.getUserInfoByIds(userIds);
            if (CollectionUtils.isNotEmpty(userInfoByIds)) {
                Map<Long, CustomerUserInfo> userInfoMap = userInfoByIds.stream().collect(Collectors.toMap(CustomerUserInfo::getCustomerUserId, Function.identity()));

                //填充客户名称
                for (SecuritydepositManagerPageDto record : chargeManagerPageDtoIPage.getRecords()) {
                    if (userInfoMap.containsKey(record.getCustomerUserId())) {
                        record.setCustomerUserName(userInfoMap.get(record.getCustomerUserId()).getCustomerUserName());
                    }
                }
            }
            List<Long> houseIds = chargeManagerPageDtoIPage.getRecords().stream().map(SecuritydepositManagerPageDto::getHouseId).distinct().collect(Collectors.toList());

            List<HouseInfoDto> houseInfoDtos = chargeHouseAndUserService.getHouseInfoByIds(Lists.newArrayList(houseIds));
            if (CollectionUtils.isNotEmpty(houseInfoDtos)) {
                Map<Long, HouseInfoDto> userInfoMap = houseInfoDtos.stream().collect(Collectors.toMap(HouseInfoDto::getHouseId, Function.identity()));

                for (SecuritydepositManagerPageDto record : chargeManagerPageDtoIPage.getRecords()) {
                    if (userInfoMap.containsKey(record.getHouseId())) {
                        record.setHouseName(userInfoMap.get(record.getHouseId()).getHouseName());
                    }
                }
            }

            chargeInfoFillService.fillinfo(chargeManagerPageDtoIPage.getRecords(),SecuritydepositManagerPageDto.class);

        }

        return IdmResDTO.success().body(chargeManagerPageDtoIPage);
    }

    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<SecuritydepositManagerDto> queryForDetail(@RequestBody @Valid IdParam idParam) {
        SecuritydepositManagerDto securitydepositManagerDto = securitydepositManagerService.queryForDetail(idParam.getId());
        if(Objects.nonNull(securitydepositManagerDto)){
            List<Long> userIds = Lists.newArrayList(securitydepositManagerDto.getCustomerUserId());
            List<CustomerUserInfo> userInfoByIds = chargeHouseAndUserService.getUserInfoByIds(userIds);
            if (CollectionUtils.isNotEmpty(userInfoByIds)) {
                securitydepositManagerDto.setCustomerUserName(userInfoByIds.get(0).getCustomerUserName());
            }

            List<Long> houseIds = Lists.newArrayList(securitydepositManagerDto.getHouseId());
            List<HouseInfoDto> houseInfoDtos = chargeHouseAndUserService.getHouseInfoByIds(Lists.newArrayList(houseIds));
            if (CollectionUtils.isNotEmpty(houseInfoDtos)) {
                securitydepositManagerDto.setHouseName(houseInfoDtos.get(0).getHouseName());
            }

            chargeInfoFillService.fillinfo(Lists.newArrayList(securitydepositManagerDto),SecuritydepositManagerDto.class);

        }
        return IdmResDTO.success().body(securitydepositManagerDto);
    }

    /**
     * 退款
     *
     * @param dto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/refund")
    @LogRecord(operationCode = "refund", operationName = "押金管理-退款", serviceType = ServiceTypeConst.SECURITYDEPOSITMANAGER)
    public IdmResDTO refund(@RequestBody @Valid SecuritydepositRefundDto dto) {
        TbSecuritydepositManager entity = securitydepositManagerService.getById(dto.getDepositId());
        AssertUtil.notNull(entity, "数据不存在");
        AssertUtil.isTrue(Lists.newArrayList(SecurityDepositStatusEnum.PAID_OFF.getCode(), SecurityDepositStatusEnum.NOT_REFUNDED.getCode()).contains(entity.getStatus()), "只有已交清或者未退完方可退款");

        securitydepositManagerService.refund(dto);
        return IdmResDTO.success();
    }


    /**
     * 作废
     *
     * @param idParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/abrogateStatus")
    @LogRecord(operationCode = "abrogateStatus", operationName = "押金管理-作废", serviceType = ServiceTypeConst.SECURITYDEPOSITMANAGER)
    public IdmResDTO abrogateStatus(@RequestBody @Valid ChargeAbrogateInsertDto idParam) {
        TbSecuritydepositManager entity = securitydepositManagerService.getById(idParam.getDataId());
        AssertUtil.notNull(entity, "数据不存在");
        AssertUtil.isTrue(Objects.equals(SecurityDepositStatusEnum.UNPAID.getCode(), entity.getStatus()), "未开交应收押金才可作废");

        securitydepositManagerService.abrogateStatus(entity, idParam.getAbrogateDesc());
        return IdmResDTO.success();
    }

    /**
     * 收款
     *
     * @param
     * @return
     */
    @RequiresPermissions
    @PostMapping("/receivedAmount")
    @LogRecord(operationCode = "receivedAmount", operationName = "押金管理-收款", serviceType = ServiceTypeConst.SECURITYDEPOSITMANAGER)
    public IdmResDTO receivedAmount(@RequestBody @Valid DepositReceiptsReceivedDto dto) {
        TbSecuritydepositManager entity = securitydepositManagerService.getById(dto.getChargeId());
        AssertUtil.notNull(entity, "数据不存在");

        entity.setTransactionMode(dto.getTransactionMode());
        entity.setAccountBank(dto.getAccountBank());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setReceivedDate(new Date());
        entity.setReceivableAmountReceived(entity.getReceivableAmount());
        entity.setReceivedDate(new Date());
        entity.setStatus(SecurityDepositStatusEnum.PAID_OFF.getCode());
        securitydepositManagerService.updateById(entity);
        return IdmResDTO.success();
    }

    /**
     * 创建
     *
     * @param createDto
     * @return
     */
//    @RequiresPermissions
    @PostMapping("/create")
    @LogRecord(operationCode = "create", operationName = "押金管理-创建", serviceType = ServiceTypeConst.SECURITYDEPOSITMANAGER)
    public IdmResDTO create(@RequestBody @Valid SecuritydepositManagerInsertDto createDto) {
        createDto.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        securitydepositManagerService.saveData(createDto);
        return IdmResDTO.success();
    }
}