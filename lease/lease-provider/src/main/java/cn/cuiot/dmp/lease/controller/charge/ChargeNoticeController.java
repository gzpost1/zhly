package cn.cuiot.dmp.lease.controller.charge;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.charge.ChargeNoticeCreateDto;
import cn.cuiot.dmp.lease.dto.charge.ChargeNoticePageQuery;
import cn.cuiot.dmp.lease.dto.charge.ChargeNoticeSendQuery;
import cn.cuiot.dmp.lease.service.charge.ChargeNoticeService;
import cn.cuiot.dmp.lease.task.ChargeOrderTask;
import cn.cuiot.dmp.lease.vo.ChargeNoticePageVo;
import cn.cuiot.dmp.lease.vo.ChargeNoticeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 收费管理-通知单
 *
 * @author zc
 */
@RestController
@RequestMapping("/charge/notice")
public class ChargeNoticeController {

    @Autowired
    private ChargeNoticeService chargeNoticeService;
    @Autowired
    private ChargeOrderTask chargeOrderTask;
    /**
     * 分页
     */
    @RequiresPermissions
    @PostMapping("queryForPage")
    public IdmResDTO<IPage<ChargeNoticePageVo>> queryForPage(@RequestBody ChargeNoticePageQuery dto) {
        dto.setCompanyId(LoginInfoHolder.getCurrentOrgId());
        return IdmResDTO.success(chargeNoticeService.queryForPage(dto));
    }

    /**
     * 通知单列表导出
     * @param dto
     * @return
     */
//    @RequiresPermissions
//    @PostMapping("export")
//    public IdmResDTO export(@RequestBody ChargeNoticePageQuery dto) throws Exception {
//        chargeNoticeService.export(dto);
//        return IdmResDTO.success();
//    }

    /**
     * 查询详情
     */
    @RequiresPermissions
    @PostMapping("queryForDetail")
    public IdmResDTO<ChargeNoticeVo> queryForDetail(@RequestBody @Valid IdParam idParam) {
        ChargeNoticeVo vo = chargeNoticeService.queryForDetail(idParam.getId());
        return IdmResDTO.success(vo);
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @PostMapping("create")
    @LogRecord(operationCode = "create", operationName = "收费管理-通知单-创建", serviceType = ServiceTypeConst.CHARGE_NOTICE)
    public IdmResDTO<?> create(@RequestBody @Valid ChargeNoticeCreateDto dto) {
        chargeNoticeService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 修改状态
     */
    @RequiresPermissions
    @PostMapping("updateStatus")
    @LogRecord(operationCode = "updateStatus", operationName = "收费管理-通知单-修改状态", serviceType = ServiceTypeConst.CHARGE_NOTICE)
    public IdmResDTO<?> updateStatus(@RequestBody @Valid UpdateStatusParam param) {
        chargeNoticeService.updateStatus(param);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @PostMapping("delete")
    @LogRecord(operationCode = "delete", operationName = "收费管理-通知单-删除", serviceType = ServiceTypeConst.CHARGE_NOTICE)
    public IdmResDTO<?> delete(@RequestBody @Valid IdParam idParam) {
        chargeNoticeService.delete(idParam.getId());
        return IdmResDTO.success();
    }

    /**
     * 发送通知
     */
    @RequiresPermissions
    @PostMapping("sengMsg")
    @LogRecord(operationCode = "sengMsg", operationName = "收费管理-通知单-发送通知", serviceType = ServiceTypeConst.CHARGE_NOTICE)
    public IdmResDTO<?> sengMsg(@RequestBody @Valid ChargeNoticeSendQuery query) {
        chargeNoticeService.sengMsg(query);
        return IdmResDTO.success();
    }

    /**
     * 发送通知
     */
    @PostMapping("taskProcess")
    public IdmResDTO<?> taskProcess(@RequestBody  ChargeNoticeSendQuery query) {
        chargeOrderTask.prePayChargeId(null);
        return IdmResDTO.success();
    }
}