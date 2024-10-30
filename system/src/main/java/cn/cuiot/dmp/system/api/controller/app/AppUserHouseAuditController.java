package cn.cuiot.dmp.system.api.controller.app;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.constant.PermissionContants;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditPageQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditUpdateDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseBuildingDTO;
import cn.cuiot.dmp.system.application.service.UserHouseAuditService;
import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<UserHouseAuditDTO> queryForList(
            @RequestBody @Valid UserHouseAuditPageQueryDTO queryDTO) {
        return userHouseAuditService.queryForList(queryDTO);
    }

    /**
     * 查询分页列表
     */
    @PostMapping("/queryForPage")
    public IPage<UserHouseAuditDTO> queryForPage(
            @RequestBody @Valid UserHouseAuditPageQueryDTO queryDTO) {
        return userHouseAuditService.queryForPage(queryDTO);
    }

    /**
     * 根据用户id查询楼盘列表
     */
    @PostMapping("/queryBuildingsByUser")
    public List<UserHouseBuildingDTO> queryBuildingsByUser(
            @RequestBody @Valid UserHouseAuditPageQueryDTO queryDTO) {
        Long userId = LoginInfoHolder.getCurrentUserId();
        return userHouseAuditService.queryBuildingsByUser(userId);
    }

    /**
     * 保存
     */
    @RequiresPermissions(allowUserType = PermissionContants.USER_CLIENT)
    @LogRecord(operationCode = "saveUserHouseAudit", operationName = "添加房屋", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/save")
    public boolean saveUserHouseAudit(@RequestBody @Valid UserHouseAuditCreateDTO createDTO) {
        if (!PhoneUtil.isPhone(createDTO.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_NOT_VALID, "请输入正确的11位手机号");
        }
        Long userId = LoginInfoHolder.getCurrentUserId();
        createDTO.setUserId(userId);
        return userHouseAuditService.saveUserHouseAudit(createDTO);
    }

    /**
     * 更新
     */
    @RequiresPermissions(allowUserType = PermissionContants.USER_CLIENT)
    @LogRecord(operationCode = "updateUserHouseAudit", operationName = "更新房屋", serviceType = ServiceTypeConst.SYSTEM_MANAGEMENT)
    @PostMapping("/update")
    public boolean updateUserHouseAudit(@RequestBody @Valid UserHouseAuditUpdateDTO updateDTO) {
        if (!PhoneUtil.isPhone(updateDTO.getPhoneNumber())) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_NOT_VALID, "请输入正确的11位手机号");
        }
        Long userId = LoginInfoHolder.getCurrentUserId();
        updateDTO.setUserId(userId);
        return userHouseAuditService.updateUserHouseAudit(updateDTO);
    }

}
