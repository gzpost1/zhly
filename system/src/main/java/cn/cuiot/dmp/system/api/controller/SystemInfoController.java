package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.system.application.param.dto.SystemInfoCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.SystemInfoQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.SystemInfoVO;
import cn.cuiot.dmp.system.application.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author caorui
 * @date 2024/4/28
 */
@RestController
@RequestMapping("/systemInfo")
public class SystemInfoController {

    @Autowired
    private SystemInfoService systemInfoService;

    /**
     * 根据id获取详情
     */
    @RequiresPermissions
    @PostMapping("/queryForDetail")
    public SystemInfoVO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return systemInfoService.queryForDetail(idParam.getId());
    }

    /**
     * 根据来源id和来源类型获取详情
     */
    @RequiresPermissions
    @PostMapping("/queryBySource")
    public SystemInfoVO queryBySource(@RequestBody @Valid SystemInfoQueryDTO systemInfoQueryDTO) {
        return systemInfoService.queryBySource(systemInfoQueryDTO);
    }

    /**
     * 创建或更新：
     * 由于所有字段可以传空，所以先查数据库，没有就新增，有就更新
     */
    @RequiresPermissions
    @PostMapping("/createOrUpdate")
    public int createOrUpdate(@RequestBody @Valid SystemInfoCreateDTO systemInfoCreateDTO) {
        return systemInfoService.saveOrUpdateSystemInfo(systemInfoCreateDTO);
    }

}
