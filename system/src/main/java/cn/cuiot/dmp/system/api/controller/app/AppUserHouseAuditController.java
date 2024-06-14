package cn.cuiot.dmp.system.api.controller.app;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.constant.PermissionContants;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditPageQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditUpdateDTO;
import cn.cuiot.dmp.system.application.service.UserHouseAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * APP端-系统配置-C端用户审核列表
 *
 * @author caorui
 * @date 2024/6/13
 */
@RestController
@RequestMapping("/app/userHouseAudit")
public class AppUserHouseAuditController {

    @Autowired
    private UserHouseAuditService userHouseAuditService;

    /**
     * 根据id获取详情
     */
    @PostMapping("/queryForDetail")
    public UserHouseAuditDTO queryForDetail(@RequestBody @Valid IdParam idParam) {
        return userHouseAuditService.queryForDetail(idParam.getId());
    }

    /**
     * 查询列表
     */
    @PostMapping("/queryForList")
    public List<UserHouseAuditDTO> queryForList(@RequestBody @Valid UserHouseAuditPageQueryDTO queryDTO) {
        return userHouseAuditService.queryForList(queryDTO);
    }

    /**
     * 查询分页列表
     */
    @PostMapping("/queryForPage")
    public PageResult<UserHouseAuditDTO> queryForPage(@RequestBody @Valid UserHouseAuditPageQueryDTO queryDTO) {
        return userHouseAuditService.queryForPage(queryDTO);
    }

    /**
     * 保存
     */
    @RequiresPermissions(allowUserType = PermissionContants.USER_CLIENT)
    @LogRecord(operationCode = "saveUserHouseAudit", operationName = "添加房屋", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/save")
    public boolean saveUserHouseAudit(@RequestBody @Valid UserHouseAuditCreateDTO createDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        createDTO.setUserId(userId);
        return userHouseAuditService.saveUserHouseAudit(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions(allowUserType = PermissionContants.USER_CLIENT)
    @LogRecord(operationCode = "updateUserHouseAudit", operationName = "更新线索", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public boolean updateUserHouseAudit(@RequestBody @Valid UserHouseAuditUpdateDTO updateDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        updateDTO.setUserId(userId);
        return userHouseAuditService.updateUserHouseAudit(updateDTO);
    }

}
