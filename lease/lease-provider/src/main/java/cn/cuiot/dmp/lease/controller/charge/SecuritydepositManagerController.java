//package cn.cuiot.dmp.lease.controller.charge;
//
//import cn.cuiot.dmp.base.application.annotation.LogRecord;
//import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
//import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
//import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
//import cn.cuiot.dmp.common.constant.IdmResDTO;
//import cn.cuiot.dmp.common.constant.ServiceTypeConst;
//import cn.cuiot.dmp.common.utils.AssertUtil;
//import cn.cuiot.dmp.domain.types.LoginInfoHolder;
//import cn.cuiot.dmp.lease.dto.charge.*;
//import cn.cuiot.dmp.lease.entity.charge.*;
//import cn.cuiot.dmp.lease.enums.*;
//import cn.cuiot.dmp.lease.service.charge.TbChargeAbrogateService;
//import cn.cuiot.dmp.lease.service.charge.TbSecuritydepositManagerService;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//import java.util.Objects;
//
///**
// * 押金管理
// * @Description 押金管理
// * @Date 2024/6/14 10:26
// * @Created by libo
// */
//@RestController
//@RequestMapping("/securitydepositManager")
//public class SecuritydepositManagerController {
//
//    @Autowired
//    private TbSecuritydepositManagerService securitydepositManagerService;
//    @Autowired
//    private TbChargeAbrogateService tbChargeAbrogateService;
//
//    /**
//     * 获取分页
//     *
//     * @param query
//     * @return
//     */
//    @PostMapping("/queryForPage")
//    public IdmResDTO<IPage<SecuritydepositManagerPageDto>> queryForPage(@RequestBody SecuritydepositManagerQuery query) {
//        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
//        IPage<SecuritydepositManagerPageDto> chargeManagerPageDtoIPage = securitydepositManagerService.queryForPage(query);
//        //todo 房屋信息 客户信息
//        return IdmResDTO.success().body(chargeManagerPageDtoIPage);
//    }
//
//    /**
//     * 获取详情
//     *
//     * @param idParam
//     * @return
//     */
//    @PostMapping("/queryForDetail")
//    public IdmResDTO<ChargeManagerDetailDto> queryForDetail(@RequestBody @Valid IdParam idParam) {
//        return IdmResDTO.success().body(securitydepositManagerService.queryForDetail(idParam.getId()));
//    }
//
//    /**
//     * 获取作废明细分页
//     *
//     * @param query
//     * @return
//     */
//    @PostMapping("/queryForAbrogatePage")
//    public IdmResDTO<IPage<TbChargeAbrogate>> queryForAbrogatePage(@RequestBody @Valid ChargeHangupQueryDto query) {
//        query.setDataType( ChargeAbrogateTypeEnum.DEPOSIT.getCode());
//
//        IPage<TbChargeAbrogate> tbChargeHangupIPage = tbChargeAbrogateService.queryForPage(query);
//        //todo 填充操作人员名称
//        return IdmResDTO.success().body(tbChargeHangupIPage);
//    }
//
//    /**
//     * 获取收款明细分页
//     *
//     * @param queryDto
//     * @return
//     */
//    @PostMapping("/queryForReceivedPage")
//    public IdmResDTO<IPage<TbChargeReceived>> queryForReceivedPage(@RequestBody @Valid ChargeHangupQueryDto queryDto) {
//        IPage<TbChargeReceived> tbChargeHangupIPage = securitydepositManagerService.queryForReceivedPage(queryDto);
//        //todo 填充操作人员名称
//        return IdmResDTO.success().body(tbChargeHangupIPage);
//    }
//
//    /**
//     * 作废
//     *
//     * @param idParam
//     * @return
//     */
//    @RequiresPermissions
//    @PostMapping("/abrogateStatus")
//    @LogRecord(operationCode = "abrogateStatus", operationName = "押金管理-作废", serviceType = ServiceTypeConst.RECEIVED_GENERATE)
//    public IdmResDTO abrogateStatus(@RequestBody @Valid ChargeAbrogateInsertDto idParam) {
//        TbSecuritydepositManager entity = securitydepositManagerService.getById(idParam.getDataId());
//        AssertUtil.notNull(entity, "数据不存在");
//        AssertUtil.isTrue(Objects.equals(SecurityDepositStatusEnum.UNPAID.getCode(),entity.getStatus()), "未开交应收押金才可作废");
//
//        securitydepositManagerService.abrogateStatus(entity, idParam.getAbrogateDesc());
//        return IdmResDTO.success();
//    }
//
//    /**
//     * 收款
//     *
//     * @param
//     * @return
//     */
//    @RequiresPermissions
//    @PostMapping("/receivedAmount")
//    @LogRecord(operationCode = "receivedAmount", operationName = "押金管理-收款", serviceType = ServiceTypeConst.RECEIVED_GENERATE)
//    public IdmResDTO receivedAmount(@RequestBody @Valid ChargeReceiptsReceivedDto dto) {
//        securitydepositManagerService.receivedAmount(dto);
//        return IdmResDTO.success();
//    }
//
//}
