package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbSecuritydepositManager;
import cn.cuiot.dmp.lease.enums.SecurityDepositStatusEnum;
import cn.cuiot.dmp.lease.service.charge.TbChargeAbrogateService;
import cn.cuiot.dmp.lease.service.charge.TbSecuritydepositManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

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
        //todo 房屋信息 客户信息
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
        return IdmResDTO.success().body(securitydepositManagerService.queryForDetail(idParam.getId()));
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
        AssertUtil.isTrue(Lists.newArrayList(SecurityDepositStatusEnum.PAID_OFF, SecurityDepositStatusEnum.NOT_REFUNDED).contains(entity.getStatus()), "只有已交清或者未退完方可退款");

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
        securitydepositManagerService.updateById(entity);
        return IdmResDTO.success();
    }

}
