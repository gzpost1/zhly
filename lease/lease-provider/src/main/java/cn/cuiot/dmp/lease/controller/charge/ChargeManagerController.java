package cn.cuiot.dmp.lease.controller.charge;


import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
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
import cn.cuiot.dmp.lease.enums.*;
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
 * 收费管理-收银台-缴费管理
 *
 * @author libo
 * @date 2024/6/12
 */
@RestController
@RequestMapping("/chargemanager")
public class ChargeManagerController {

    @Autowired
    private TbChargeManagerService tbChargeManagerService;

    /**
     * 获取房屋欠费等相关信息
     *
     * @param idParam
     * @return
     */
    @PostMapping("/queryForHouseDetail")
    public IdmResDTO<ChargeHouseDetailDto> queryForHouseDetail(@RequestBody @Valid IdParam idParam) {
        ChargeHouseDetailDto chargeHouseDetailDto = tbChargeManagerService.queryForHouseDetail(idParam.getId());
        // todo 房屋信息
        return IdmResDTO.success().body(chargeHouseDetailDto);
    }


    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ChargeManagerPageDto>> queryForPage(@RequestBody TbChargeManagerQuery query) {
        AssertUtil.notNull(query.getHouseId(), "房屋id不能为空");
        query.setAbrogateStatus(ChargeAbrogateEnum.NORMAL.getCode());
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
        queryDto.setDataType( ChargeAbrogateTypeEnum.CHARGE.getCode());

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
     * 创建
     *
     * @param createDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/create")
    @LogRecord(operationCode = "create", operationName = "缴费管理-创建", serviceType = ServiceTypeConst.RECEIVED_MANAGER)
    public IdmResDTO create(@RequestBody @Valid ChargeManagerInsertVo createDto) {
        tbChargeManagerService.saveData(createDto, ChargeTypeEnum.MANUAL_CREATE.getCode(), null);
        return IdmResDTO.success();
    }

    /**
     * 挂起/解挂
     *
     * @param idParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/updateHangUpStatus")
    @LogRecord(operationCode = "updateHangUpStatus", operationName = "缴费管理-挂起/解挂", serviceType = ServiceTypeConst.RECEIVED_MANAGER)
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
    @LogRecord(operationCode = "abrogateStatus", operationName = "缴费管理-作废", serviceType = ServiceTypeConst.RECEIVED_MANAGER)
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
    @LogRecord(operationCode = "receivedAmount", operationName = "缴费管理-收款", serviceType = ServiceTypeConst.RECEIVED_MANAGER)
    public IdmResDTO receivedAmount(@RequestBody @Valid ChargeReceiptsReceivedDto dto) {
        tbChargeManagerService.receivedAmount(dto);
        return IdmResDTO.success();
    }


}

