package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.SysParamService;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetSysParamResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SysParamDto;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统配置-系统信息
 *
 * @author wen
 * @date 2021-12-27 10:07:42
 */
@RestController
@RequestMapping("/sys")
public class SysParamController extends BaseController {

    @Autowired
    private SysParamService sysParamService;

    @RequiresPermissions
    @LogRecord(operationCode = "sysParamSet", operationName = "设置系统参数", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping(value = "/sysParamSet", produces = MediaType.APPLICATION_JSON_VALUE)
    public int sysParamSet(@RequestBody @Valid SysParamDto dto) {
        Long sessionOrgId = LoginInfoHolder.getCurrentOrgId();
        if (Objects.isNull(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        dto.setSessionOrgId(sessionOrgId);
        dto.setSessionUserId(LoginInfoHolder.getCurrentUserId());
        return sysParamService.addOrUpdate(dto);
    }

    /**
     * 查询系统参数
     */
    @GetMapping(value = "/sysParamGet", produces = "application/json;charset=UTF-8")
    public GetSysParamResDto sysParamGet() {
        Long sessionOrgId = LoginInfoHolder.getCurrentOrgId();
        if (Objects.isNull(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        return sysParamService.getByPath(sessionOrgId);
    }

}
