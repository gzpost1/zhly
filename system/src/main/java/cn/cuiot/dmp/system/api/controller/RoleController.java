package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetData;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.ServiceTypeConst;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.RoleService;
import cn.cuiot.dmp.system.infrastructure.entity.bo.RoleBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CreateRoleDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateRoleDto;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理
 * @author guoying
 * @className LogControllerImpl
 * @description 角色管理
 * @date 2020-10-23 10:00:07
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 角色列表分页查询
     */
    @RequiresPermissions
    @GetMapping(value = "/listRoles", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<RoleDTO> getRoleListByPage(
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value = "roleKey", required = false) String roleKey,
            @RequestParam(value = "status", required = false) Byte status
    ) {
        // 参数校验
        if (null == pageNo || pageNo.compareTo(0) <= 0 ||
                null == pageSize || pageSize.compareTo(100) > 0 || pageSize.compareTo(0) <= 0) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR);
        }

        Map<String, Object> paramsMap = new HashMap<String, Object>(6) {{
            put("orgId", getOrgId());
            put("userId", getUserId());
            put("pageNo", pageNo);
            put("pageSize", pageSize);
            put("roleName", roleName);
            put("roleKey", roleKey);
            put("status", status);
        }};
        return roleService.getRoleListByPage(paramsMap);
    }


    /**
     * 角色全量列表查询(不含超管)
     */
    @RequiresPermissions
    @GetMapping(value = "/listRolesAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RoleDTO> getRoleListByPage( @RequestParam(value = "status", required = false) Byte status,@RequestParam(value = "roleName", required = false) String roleName) {
        Map<String, Object> paramsMap = new HashMap<String, Object>(6) {{
            put("orgId", getOrgId());
            put("userId", getUserId());
            put("status", status);
            put("roleName", roleName);
        }};
        return roleService.getRoleListNotDefault(paramsMap);
    }


    /**
     * 新增角色
     */
    @RequiresPermissions
    @LogRecord(operationCode = "createRole", operationName = "添加角色", serviceType ="role",serviceTypeName = "角色管理")
    @PostMapping(value = "/createRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> createRole(@RequestBody @Valid CreateRoleDto dto) {
        String userId = getUserId();
        dto.setLoginUserId(userId);
        dto.setLoginOrgId(getOrgId());
        Map<String, Object> resultMap = new HashMap<>(1);

        Long rolePk = this.roleService.createRole(dto);
        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("角色")
                .targetDatas(Lists.newArrayList(new OptTargetData(dto.getRoleName(),rolePk.toString())))
                .build());

        resultMap.put("id",rolePk);
        return resultMap;
    }


    /**
     * 删除角色
     */
    @RequiresPermissions
    @LogRecord(operationCode = "deleteRole", operationName = "删除角色", serviceType ="role",serviceTypeName = "角色管理")
    @PostMapping(value = "deleteRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteRoles(@RequestBody @Valid IdParam param) {
        Map<String, Object> resultMap = new HashMap<>(1);
        RoleBo roleBo = new RoleBo();
        roleBo.setSessionOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        roleBo.setDeleteIdList(Lists.newArrayList(param.getId()));
        resultMap.put("succeedCount", this.roleService.deleteRoles(roleBo));
        return resultMap;
    }

    /**
     * 查询当前角色详情
     */
    @RequiresPermissions
    @GetMapping(value = "/getRoleOne", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO getRoleOne(@RequestParam(value = "id", required = true) String roleId) {
        String orgId = getOrgId();
        String userId = getUserId();
        return roleService.getRoleInfo(roleId, orgId, userId);
    }


    /**
     * 查询账号下的角色
     */
    @RequiresPermissions
    @GetMapping(value = "getRoleListByOrgId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RoleDTO> getRoleListByOrgId() {
        return this.roleService.getRoleListByOrgId(getOrgId(), getUserId());
    }


    /**
     * 编辑角色
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateRole", operationName = "编辑角色",serviceType ="role",serviceTypeName = "角色管理")
    @PostMapping(value = "/updateRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateRole(@RequestBody @Valid UpdateRoleDto dto) {
        if (null == dto || StringUtils.isBlank(String.valueOf(dto.getId())) || StringUtils
                .isBlank(dto.getRoleName())) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        RoleBo roleBo = new RoleBo();
        roleBo.setId(dto.getId());
        roleBo.setRoleName(dto.getRoleName());
        roleBo.setDescription(dto.getDescription());
        roleBo.setMenuIds(dto.getMenuIds());
        roleBo.setSessionUserId(LoginInfoHolder.getCurrentUserId().toString());
        roleBo.setSessionOrgId(LoginInfoHolder.getCurrentOrgId().toString());

        //设置日志操作对象内容
        LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                .name("角色")
                .targetDatas(Lists.newArrayList(new OptTargetData(roleBo.getRoleName(),roleBo.getId().toString())))
                .build());

        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("id", this.roleService.updateRole(roleBo));
        return resultMap;
    }

    /**
     * 启停用
     */
    @RequiresPermissions
    @LogRecord(operationCode = "updateRoleStatus", operationName = "启停用角色", serviceType ="role",serviceTypeName = "角色管理")
    @PostMapping("/updateStatus")
    public IdmResDTO updateStatus(@RequestBody @Valid UpdateStatusParam updateStatusParam) {
        Long sessionUserId = LoginInfoHolder.getCurrentUserId();
        Long sessionOrgId = LoginInfoHolder.getCurrentOrgId();
        roleService.updateStatus(updateStatusParam, sessionUserId, sessionOrgId);
        return IdmResDTO.success();
    }
}
