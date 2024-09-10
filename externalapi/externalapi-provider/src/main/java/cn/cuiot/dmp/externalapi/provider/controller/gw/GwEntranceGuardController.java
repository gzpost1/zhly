package cn.cuiot.dmp.externalapi.provider.controller.gw;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParams;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardEntity;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardPageQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.GwEntranceGuardUpdateDto;
import cn.cuiot.dmp.externalapi.service.service.gw.GwEntranceGuardService;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.BaseDmpResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.dmp.DmpDeviceRemoteService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwEntranceGuardPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
    private DmpDeviceRemoteService dmpDeviceRemoteService;

    /**
     * 分页查询
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<GwEntranceGuardPageVo>> queryForPage(@RequestBody GwEntranceGuardPageQuery query) {
        return IdmResDTO.success(gwEntranceGuardService.queryForPage(query));
    }

    /**
     * 详情
     */
    @PostMapping("/queryForDetail")
    public IdmResDTO<GwEntranceGuardEntity> queryForDetail(@RequestBody IdParam param) {
        return IdmResDTO.success(gwEntranceGuardService.queryForDetail(param.getId()));
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

    @RequestMapping("/getPushData")
    public void getPushData(@RequestBody Result result) {
        System.out.println("=============================================");
        System.out.println(result);
    }

    @Data
    public static class Result{
        private String devicePropKey;
        private Object dataProcessing;
        private List<Object> dataForwarding;
    }
}
