package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.DeleteParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.TbChargeStandardQuery;
import cn.cuiot.dmp.lease.entity.charge.TbChargeStandard;
import cn.cuiot.dmp.lease.service.charge.TbChargeStandardService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 收费标准
 *
 * @author libo
 */
@RestController
@RequestMapping("/chargeStandard")
public class TbChargeStandardController {

    @Autowired
    private TbChargeStandardService tbChargeStandardService;

    /**
     * 获取分页
     *
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<TbChargeStandard>> queryForPage(@RequestBody TbChargeStandardQuery query) {
        query.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return IdmResDTO.success().body(tbChargeStandardService.queryForPage(query));
    }


    /**
     * 获取详情
     *
     * @param idParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public IdmResDTO<TbChargeStandard> queryForDetail(@RequestBody @Valid IdParam idParam) {
        return IdmResDTO.success().body(tbChargeStandardService.getById(idParam.getId()));
    }


    /**
     * 创建
     *
     * @param createDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/create")
    @LogRecord(operationCode = "create", operationName = "收费标准-创建", serviceType = ServiceTypeConst.CHARGE_STANDARD)
    public IdmResDTO create(@RequestBody @Valid TbChargeStandard createDto) {
        createDto.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        createDto.setStatus(EntityConstants.ENABLED);

        tbChargeStandardService.save(createDto);

        return IdmResDTO.success();
    }

    /**
     * 更新
     *
     * @param updateDto
     * @return
     */
    @RequiresPermissions
    @PostMapping("/update")
    @LogRecord(operationCode = "update", operationName = "收费标准-更新", serviceType = ServiceTypeConst.CHARGE_STANDARD)
    public IdmResDTO update(@RequestBody @Valid TbChargeStandard updateDto) {

        tbChargeStandardService.updateById(updateDto);

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
    @LogRecord(operationCode = "delete", operationName = "收费标准-删除", serviceType = ServiceTypeConst.CHARGE_STANDARD)
    public IdmResDTO delete(@RequestBody @Valid DeleteParam deleteParam) {

        tbChargeStandardService.removeById(deleteParam.getId());

        return IdmResDTO.success();
    }

    /**
     * 更新状态
     *
     * @param updateStatusParam
     * @return
     */
    @RequiresPermissions
    @PostMapping("/updateStatus")
    @LogRecord(operationCode = "updateStatus", operationName = "收费标准-修改状态", serviceType = ServiceTypeConst.CHARGE_STANDARD)
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        TbChargeStandard entity = tbChargeStandardService.getById(updateStatusParam.getId());
        entity.setStatus(updateStatusParam.getStatus());
        tbChargeStandardService.updateById(entity);
        return IdmResDTO.success();
    }

}

