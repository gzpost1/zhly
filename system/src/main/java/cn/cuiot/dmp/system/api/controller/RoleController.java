package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.RoleService;
import cn.cuiot.dmp.system.infrastructure.entity.bo.RoleBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CreateRoleDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleDTO;
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
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value = "roleKey", required = false) String roleKey
    ) {
        // 参数校验
        if (null == currentPage || currentPage.compareTo(0) <= 0 ||
                null == pageSize || pageSize.compareTo(100) > 0 || pageSize.compareTo(0) <= 0) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR);
        }

        Map<String, Object> paramsMap = new HashMap<String, Object>(6) {{
            put("orgId", getOrgId());
            put("userId", getUserId());
            put("currentPage", currentPage);
            put("pageSize", pageSize);
            put("roleName", roleName);
            put("roleKey", roleKey);
        }};
        return roleService.getRoleListByPage(paramsMap);
    }


    /**
     * 角色全量列表查询(不含超管)
     */
    @GetMapping(value = "/listRolesAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RoleDTO> getRoleListByPage() {
        Map<String, Object> paramsMap = new HashMap<String, Object>(6) {{
            put("orgId", getOrgId());
            put("userId", getUserId());
        }};
        return roleService.getRoleListNotDefault(paramsMap);
    }


    /**
     * 新增角色
     */
    @RequiresPermissions
    @PostMapping(value = "/createRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> createRole(@RequestBody @Valid CreateRoleDto dto) {
        String userId = getUserId();
        dto.setLoginUserId(userId);
        dto.setLoginOrgId(getOrgId());
        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("id", this.roleService.createRole(dto));
        return resultMap;
    }


    /**
     * 删除角色
     */
    @RequiresPermissions
    @PostMapping(value = "deleteRoles", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteRoles(@RequestBody Map<String, List<Long>> paramsMap) {
        List<Long> idList = paramsMap.get("id");
        if (idList.size() < 1) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }

        Map<String, Object> resultMap = new HashMap<>(1);
        RoleBo roleBo = new RoleBo();
        roleBo.setSessionOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        roleBo.setDeleteIdList(idList);
        resultMap.put("succeedCount", this.roleService.deleteRoles(roleBo));
        return resultMap;
    }

    /**
     * 查询角色详情（系统预置角色）
     */
    @RequiresPermissions
    @GetMapping(value = "/getRoleInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO getRoleInfo(@RequestParam("id") String id) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }

        try {
            Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE);
        }

        return this.roleService.getRoleInfo(Long.valueOf(getOrgId()), Long.valueOf(id));
    }


    /**
     * 查询当前角色所有信息（自定角色，含菜单集合）
     */
    @GetMapping(value = "/getRoleOne", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO getRoleOne(@RequestParam(value = "id", required = true) String roleId) {
        String orgId = getOrgId();
        String userId = getUserId();
        return roleService.getRoleAll(roleId, orgId, userId);
    }


    /**
     * 查询账号下的角色
     */
    @GetMapping(value = "getRoleListByOrgId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RoleDTO> getRoleListByOrgId() {
        return this.roleService.getRoleListByOrgId(getOrgId(), getUserId());
    }


    /**
     * 编辑角色
     */
    @RequiresPermissions
    @PostMapping(value = "/updateRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateRole(@RequestBody RoleBo roleBo) {
        if (null == roleBo || StringUtils.isBlank(String.valueOf(roleBo.getId())) || StringUtils
                .isBlank(roleBo.getRoleName())
                || StringUtils.isBlank(roleBo.getRoleKey()) || StringUtils
                .isBlank(String.valueOf(roleBo.getPermit()))) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        try {
            roleBo.setSessionUserId(LoginInfoHolder.getCurrentUserId().toString());
            roleBo.setSessionOrgId(LoginInfoHolder.getCurrentOrgId().toString());
            roleBo.getId();
            roleBo.getPermit();
        } catch (NumberFormatException nfe) {
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE);
        }

        Map<String, Object> resultMap = new HashMap<>(1);
        roleBo.setSessionUserId(LoginInfoHolder.getCurrentUserId().toString());
        roleBo.setSessionOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        resultMap.put("id", this.roleService.updateRole(roleBo));
        return resultMap;
    }
}
