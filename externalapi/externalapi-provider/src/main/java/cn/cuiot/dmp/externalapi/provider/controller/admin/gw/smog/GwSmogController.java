package cn.cuiot.dmp.externalapi.provider.controller.admin.gw.smog;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.IdsParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParams;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogBatchUpdateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogCreateDto;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogQuery;
import cn.cuiot.dmp.externalapi.service.query.gw.GwSmogUpdateDto;
import cn.cuiot.dmp.externalapi.service.service.gw.GwSmogService;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwSmogDetailVo;
import cn.cuiot.dmp.externalapi.service.vo.gw.GwSmogPageVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 后台-格物烟雾报警器
 *
 * @Author: xulei
 * @Date: 2024-10-23
 */
@RestController
@RequestMapping("/gw/smog")
public class GwSmogController {


    @Autowired
    private GwSmogService gwSmogService;

    /**
     * 分页查询
     */
    @RequiresPermissions
    @PostMapping("/queryForPage")
    public IdmResDTO<IPage<GwSmogPageVo>> queryForPage(@RequestBody GwSmogQuery query) {
        return IdmResDTO.success(gwSmogService.queryForPage(query));
    }

    /**
     * 格物-烟雾报警器导出
     * @param query
     * @return
     */
    @RequiresPermissions
    @PostMapping("/export")
    public IdmResDTO export(@RequestBody GwSmogQuery query){
        gwSmogService.export(query);
        return IdmResDTO.success();
    }
    /**
     * 详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public IdmResDTO<GwSmogDetailVo> queryForDetail(@RequestBody IdParam param) {
        //企业id(只能查该企业数据)
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        return IdmResDTO.success(gwSmogService.queryForDetail(param.getId(),companyId));
    }
    /**
     * 创建
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createGwSmog", operationName = "创建格物烟雾报警器设备", serviceType = "gwSmog", serviceTypeName = "格物烟雾报警器管理")
    @PostMapping("/create")
    public IdmResDTO<?> create(@RequestBody @Valid GwSmogCreateDto dto) {
        gwSmogService.create(dto);
        return IdmResDTO.success();
    }

    /**
     * 更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateGwSmog", operationName = "更新格物烟雾报警器设备", serviceType = "gwSmog", serviceTypeName = "格物烟雾报警器管理")
    @PostMapping("/update")
    public IdmResDTO<?> update(@RequestBody @Valid GwSmogUpdateDto dto) {
        gwSmogService.update(dto);
        return IdmResDTO.success();
    }

    /**
     * 批量更新
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchUpdateGwSmog", operationName = "批量更新格物烟雾报警器设备", serviceType = "gwSmog", serviceTypeName = "格物烟雾报警器管理")
    @PostMapping("/batchUpdate")
    public IdmResDTO<?> batchUpdate(@RequestBody @Valid GwSmogBatchUpdateDto dto) {
        gwSmogService.batchUpdate(dto);
        return IdmResDTO.success();
    }

    /**
     * 批量启停用
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchUpdateStatusGwSmog", operationName = "批量启停用格物烟雾报警器设备", serviceType = "gwSmog", serviceTypeName = "格物烟雾报警器管理")
    @PostMapping("/batchUpdateStatus")
    public IdmResDTO<?> batchUpdateStatus(@RequestBody @Valid UpdateStatusParams param) {
        gwSmogService.updateStatus(param);
        return IdmResDTO.success();
    }

    /**
     * 批量删除
     */
    @RequiresPermissions
    @LogRecord(operationCode = "batchDeleteGwSmog", operationName = "删除格物门禁设备", serviceType = "GwSmog", serviceTypeName = "格物烟雾报警器管理")
    @PostMapping("/batchDelete")
    public IdmResDTO<?> delete(@RequestBody @Valid IdsParam param) {
        gwSmogService.batchDelete(param.getIds());
        return IdmResDTO.success();
    }
}
