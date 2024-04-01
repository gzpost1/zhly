package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.system.application.service.SysParamService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetSysParamResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysParamDto;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统参数设置
 *
 * @author wen
 * @date 2021-12-27 10:07:42
 */
@RestController
@RequestMapping("/sys")
public class SysParamController extends BaseController {

    @Autowired
    private SysParamService sysParamService;

    @RequiresPermissions("basics:system:control")
    @LogRecord(operationCode = "sysParamSet", operationName = "设置系统参数", serviceType = ServiceTypeConst.SUPER_ORGANIZATION_MANAGEMENT)
    @PostMapping(value = "/sysParamSet", produces = MediaType.APPLICATION_JSON_VALUE)
    public int sysParamSet(@RequestBody @Valid SysParamDto dto) {
        String sessionOrgId = getOrgId();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        dto.setOrgId(Long.parseLong(sessionOrgId));
        dto.setUserId(Long.parseLong(getUserId()));
        return sysParamService.addOrUpdate(dto);
    }

    /**
     * 查询系统参数
     * @param path
     * @return
     */
    @GetMapping(value = "/sysParamGet", produces = "application/json;charset=UTF-8")
    public GetSysParamResDto sysParamGet(@RequestParam(value = "path", required = false) String path) {
        // 获取session中的orgId
        String sessionOrgId = getOrgId();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        return sysParamService.getByPath(path, getUserId());
    }

    /**
     *判断组织下系统参数是否需要覆盖
     *
     * @param path
     * @return
     */
    @GetMapping(value = "/getSysParamWhether", produces = "application/json;charset=UTF-8")
    public Boolean getSysParamWhether(@RequestParam(value = "path", required = true) String path ) {
        return sysParamService.getSysParamWhether(path);
    }



}
