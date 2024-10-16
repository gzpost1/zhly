package cn.cuiot.dmp.externalapi.provider.controller.admin.gw;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParams;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardParamEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.*;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardAccessRecordService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardOperationRecordService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardParamService;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardAccessRecordVO;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardOperationPageVO;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 后台-格物门禁
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@RestController
@RequestMapping("/gw/entranceGuard")
public class GwEntranceGuardController {

    @Autowired
    private GwEntranceGuardService gwEntranceGuardService;
    @Autowired
    private GwEntranceGuardOperationRecordService operationRecordService;
    @Autowired
    private GwEntranceGuardAccessRecordService gwEntranceGuardAccessRecordService;
    @Autowired
    private GwEntranceGuardParamService gwEntranceGuardParamService;

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<GwEntranceGuardPageVo>> queryForPage(@RequestBody GwEntranceGuardPageQuery query) {
        return IdmResDTO.success(gwEntranceGuardService.queryForPage(query));
    }

    /**
     * 格物-门禁导出
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/export")
    public IdmResDTO export(@RequestBody GwEntranceGuardPageQuery query){
        gwEntranceGuardService.export(query);
        return IdmResDTO.success();
    }
    /**
     * 详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public IdmResDTO<GwEntranceGuardEntity> queryForDetail(@RequestBody IdParam param) {
        return IdmResDTO.success(gwEntranceGuardService.queryForDetail(param.getId()));
    }

    /**
     * 查询参数
     */
    @RequiresPermissions
    @PostMapping("/queryForParam")
    public IdmResDTO<GwEntranceGuardParamEntity> queryForParam(@RequestBody IdParam param) {
        return IdmResDTO.success(gwEntranceGuardParamService.queryForParam(param.getId()));
    }

    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createGwEntranceGuard", operationName = "创建格物门禁设备", serviceType = "gwEntranceGuard", serviceTypeName = "格物门禁管理")
    @PostMapping("/create")
    public IdmResDTO<?> create(@RequestBody @Valid GwEntranceGuardCreateDto dto) {
        gwEntranceGuardService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateGwEntranceGuard", operationName = "更新格物门禁设备", serviceType = "gwEntranceGuard", serviceTypeName = "格物门禁管理")
    @PostMapping("/update")
    public IdmResDTO<?> update(@RequestBody @Valid GwEntranceGuardUpdateDto dto) {
        gwEntranceGuardService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 启停用
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateStatusGwEntranceGuard", operationName = "启停用格物门禁设备", serviceType = "gwEntranceGuard", serviceTypeName = "格物门禁管理")
    @PostMapping("/updateStatus")
    public IdmResDTO<?> updateStatus(@RequestBody @Valid UpdateStatusParams param) {
        gwEntranceGuardService.updateStatus(param);
        return IdmResDTO.success();
    }

    /**
     * 删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteGwEntranceGuard", operationName = "删除格物门禁设备", serviceType = "gwEntranceGuard", serviceTypeName = "格物门禁管理")
    @PostMapping("/delete")
    public IdmResDTO<?> delete(@RequestBody @Valid IdsParam param) {
        gwEntranceGuardService.delete(param.getIds());
        return IdmResDTO.success();
    }

    /**
     * 开门
     */
    @RequiresPermissions
    @LogRecord(operationCode = "openTheDoor", operationName = "门禁开门", serviceType = "gwEntranceGuard", serviceTypeName = "格物门禁管理")
    @PostMapping("/openTheDoor")
    public IdmResDTO<?> openTheDoor(@RequestBody @Valid GwEntranceGuardOperationDto param) {
        gwEntranceGuardService.openTheDoor(param);
        return IdmResDTO.success();
    }

    /**
     * 重启
     */
    @RequiresPermissions
    @LogRecord(operationCode = "restart", operationName = "门禁重启", serviceType = "gwEntranceGuard", serviceTypeName = "格物门禁管理")
    @PostMapping("/restart")
    public IdmResDTO<?> restart(@RequestBody @Valid GwEntranceGuardOperationDto param) {
        gwEntranceGuardService.restart(param);
        return IdmResDTO.success();
    }

    /**
     * 修改参数
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateParam", operationName = "门禁参数修改", serviceType = "gwEntranceGuard", serviceTypeName = "格物门禁管理")
    @PostMapping("/updateParam")
    public IdmResDTO<?> updateParam(@RequestBody @Valid GwEntranceGuardParamDto dto) {
        gwEntranceGuardParamService.updateParam(dto);
        return IdmResDTO.success();
    }

    /**
     * 操作分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryOperationForPage")
    public IdmResDTO<IPage<GwEntranceGuardOperationPageVO>> queryForPage(@RequestBody @Valid GwEntranceGuardOperationQuery query) {
        return IdmResDTO.success(operationRecordService.queryForPage(query));
    }

    /**
     * 通行分页
     */
    @RequiresPermissions
    @PostMapping("/queryAccessForPage")
    public IdmResDTO<IPage<GwEntranceGuardAccessRecordVO>> queryForPage(@RequestBody GwEntranceGuardAccessRecordQuery query) {
        return IdmResDTO.success(gwEntranceGuardAccessRecordService.queryForPage(query));
    }

    /**
     * 通行记录导出
     */
    @RequiresPermissions
    @PostMapping("/exportGuardAccess")
    public IdmResDTO exportGuardAccess(@RequestBody GwEntranceGuardAccessRecordQuery query){
        gwEntranceGuardAccessRecordService.export(query);
        return IdmResDTO.success();
    }

}
