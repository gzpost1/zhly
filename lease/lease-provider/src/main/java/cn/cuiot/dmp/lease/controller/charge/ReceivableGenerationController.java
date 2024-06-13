package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.entity.charge.TbChargeAbrogate;
import cn.cuiot.dmp.lease.entity.charge.TbChargeHangup;
import cn.cuiot.dmp.lease.entity.charge.TbChargeManager;
import cn.cuiot.dmp.lease.entity.charge.TbChargeReceived;
import cn.cuiot.dmp.lease.enums.ChargeAbrogateEnum;
import cn.cuiot.dmp.lease.enums.ChargeHangUpEnum;
import cn.cuiot.dmp.lease.enums.ChargeReceivbleEnum;
import cn.cuiot.dmp.lease.service.charge.TbChargeManagerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 应收生成明细
 *
 * @Description 应收生成明细
 * @Date 2024/6/13 14:58
 * @Created by libo
 */
@RestController
@RequestMapping("/receivableGeneration")
public class ReceivableGenerationController {

    @Autowired
    private TbChargeManagerService tbChargeManagerService;


    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ChargeManagerPageDto>> queryForPage(@RequestBody TbChargeManagerQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        IPage<ChargeManagerPageDto> chargeManagerPageDtoIPage = tbChargeManagerService.queryForPage(query);
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
    public IdmResDTO<ChargeManagerDetailDto> queryForDetail(@RequestBody @Valid IdParam idParam) {
        return IdmResDTO.success().body(tbChargeManagerService.queryForDetail(idParam.getId()));
    }

    /**
     * 获取挂解明细分页
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/queryForHangupPage")
    public IdmResDTO<IPage<TbChargeHangup>> queryForHangupPage(@RequestBody @Valid ChargeHangupQueryDto queryDto) {
        IPage<TbChargeHangup> tbChargeHangupIPage = tbChargeManagerService.queryForHangupPage(queryDto);
        //todo 填充操作人员名称
        return IdmResDTO.success().body(tbChargeHangupIPage);
    }

    /**
     * 获取作废明细分页
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/queryForAbrogatePage")
    public IdmResDTO<IPage<TbChargeAbrogate>> queryForAbrogatePage(@RequestBody @Valid ChargeHangupQueryDto queryDto) {
        IPage<TbChargeAbrogate> tbChargeHangupIPage = tbChargeManagerService.queryForAbrogatePage(queryDto);
        //todo 填充操作人员名称
        return IdmResDTO.success().body(tbChargeHangupIPage);
    }

    /**
     * 获取收款明细分页
     *
     * @param queryDto
     * @return
     */
    @PostMapping("/queryForReceivedPage")
    public IdmResDTO<IPage<TbChargeReceived>> queryForReceivedPage(@RequestBody @Valid ChargeHangupQueryDto queryDto) {
        IPage<TbChargeReceived> tbChargeHangupIPage = tbChargeManagerService.queryForReceivedPage(queryDto);
        //todo 填充操作人员名称
        return IdmResDTO.success().body(tbChargeHangupIPage);
    }

    /**
     * 挂起/解挂
     *
     * @param idParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/updateHangUpStatus")
    @LogRecord(operationCode = "updateHangUpStatus", operationName = "应收生成明细-挂起/解挂", serviceType = ServiceTypeConst.RECEIVED_GENERATE)
    public IdmResDTO updateHangUpStatus(@RequestBody @Valid ChargeAbrogateInsertDto idParam) {
        TbChargeManager entity = tbChargeManagerService.getById(idParam.getDataId());
        AssertUtil.notNull(entity, "数据不存在");

        tbChargeManagerService.updateHangUpStatus(entity, idParam.getAbrogateDesc());
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
    @LogRecord(operationCode = "abrogateStatus", operationName = "应收生成明细-作废", serviceType = ServiceTypeConst.RECEIVED_GENERATE)
    public IdmResDTO abrogateStatus(@RequestBody @Valid ChargeAbrogateInsertDto idParam) {
        TbChargeManager entity = tbChargeManagerService.getById(idParam.getDataId());
        AssertUtil.notNull(entity, "数据不存在");
        AssertUtil.isFalse(ChargeReceivbleEnum.isShowAbrogate(entity.getReceivbleStatus()), "已开交、已交清的状态不显示作废按钮");
        AssertUtil.isTrue(Objects.equals(ChargeHangUpEnum.HANG_UP.getCode(), entity.getHangUpStatus()), "已挂起的数据不能作废");

        tbChargeManagerService.abrogateStatus(entity, idParam.getAbrogateDesc());
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
    @LogRecord(operationCode = "receivedAmount", operationName = "应收生成明细-收款", serviceType = ServiceTypeConst.RECEIVED_GENERATE)
    public IdmResDTO receivedAmount(@RequestBody @Valid ChargeReceiptsReceivedDto dto) {
        tbChargeManagerService.receivedAmount(dto);
        return IdmResDTO.success();
    }

    /**
     * 删除
     *
     * @param deleteParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/delete")
    @LogRecord(operationCode = "delete", operationName = "应收生成明细-删除", serviceType = ServiceTypeConst.RECEIVED_GENERATE)
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {
        TbChargeManager entity = tbChargeManagerService.getById(deleteParam.getId());
        AssertUtil.notNull(entity, "数据不存在");
        //只有已作废的数据才能删除
        AssertUtil.isTrue(Objects.equals(ChargeAbrogateEnum.ABROGATE.getCode(), entity.getAbrogateStatus()), "只有已作废的数据才能删除");

        tbChargeManagerService.removeById(deleteParam.getId());
        return IdmResDTO.success();
    }
}
