package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.*;
import cn.cuiot.dmp.lease.service.charge.ChargeCollectionPlanService;
import cn.cuiot.dmp.lease.vo.ChargeCollectionPlanPageVo;
import cn.cuiot.dmp.lease.vo.ChargeCollectionPlanVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 收费管理-催款计划
 *
 * @author zc
 */
@RestController
@RequestMapping("/charge/collectionPlan")
public class ChargeCollectionPlanController {

    @Autowired
    private ChargeCollectionPlanService chargeCollectionPlanService;

    /**
     * 分页
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<ChargeCollectionPlanPageVo>> queryForPage(@RequestBody ChargeCollectionPlanPageQuery dto) {
        dto.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return IdmResDTO.success(chargeCollectionPlanService.queryForPage(dto));
    }

    /**
     * 查询详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public IdmResDTO<ChargeCollectionPlanVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        return IdmResDTO.success(chargeCollectionPlanService.queryForDetail(idParam.getId()));
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("/create")
    @LogRecord(operationCode = "create", operationName = "收费管理-催款计划-创建", serviceType = ServiceTypeConst.CHARGE_COLLECTION_MANAGE)
    public IdmResDTO<?> create(@RequestBody @Valid ChargeCollectionPlanCreateDto dto) {
        chargeCollectionPlanService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 修改
     */
    @RequiresPermissions
    @PostMapping("/update")
    @LogRecord(operationCode = "update", operationName = "收费管理-催款计划-修改", serviceType = ServiceTypeConst.CHARGE_COLLECTION_MANAGE)
    public IdmResDTO<?> update(@RequestBody @Valid ChargeCollectionPlanUpdateDto dto) {
        chargeCollectionPlanService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 修改状态
     */
    @RequiresPermissions
    @PostMapping("/updateStatus")
    @LogRecord(operationCode = "updateStatus", operationName = "收费管理-催款计划-更新状态", serviceType = ServiceTypeConst.CHARGE_COLLECTION_MANAGE)
    public IdmResDTO<?> updateStatus(@RequestBody @Valid UpdateStatusParam param) {
        chargeCollectionPlanService.updateStatus(param);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("/delete")
    @LogRecord(operationCode = "delete", operationName = "收费管理-催款计划-删除", serviceType = ServiceTypeConst.CHARGE_COLLECTION_MANAGE)
    public IdmResDTO<?> delete(@RequestBody @Valid IdParam idParam) {
        chargeCollectionPlanService.delete(idParam.getId());
        return IdmResDTO.success();
    }
}