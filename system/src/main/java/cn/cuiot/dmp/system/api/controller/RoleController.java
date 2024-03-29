package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.annotation.RequiresPermissions;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.controller.BaseController;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.ValidateUtil;
import cn.cuiot.dmp.system.application.service.RoleService;
import cn.cuiot.dmp.system.infrastructure.entity.bo.RoleBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CreateRoleDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.RoleCreatedDTO;
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
     * @param currentPage
     * @param pageSize
     * @param roleName
     * @param roleKey
     * @return
     */
    @RequiresPermissions("system:role:control")
    @GetMapping(value = "/listRoles", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<RoleDTO> getRoleListByPage(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value ="roleName", required = false) String roleName ,
            @RequestParam(value ="roleKey", required = false) String roleKey
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
            put("roleName",roleName);
            put("roleKey",roleKey);
        }};
        return roleService.getRoleListByPage(paramsMap);
    }


    /**
     * 角色全量列表查询(不含超管)
     * @return
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
     * @param dto
     * @return
     */
    @RequiresPermissions("system:role:add")
    @PostMapping(value = "/createRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> createRole(@RequestBody @Valid CreateRoleDto dto) {
        //校验角色名称
        if (!dto.getRoleName().matches(RegexConst.ROLE_NAME)
                || ValidateUtil.calculateStrLength(dto.getRoleName()) > RegexConst.ROLE_NAME_LENGTH) {
            throw new BusinessException(ResultCode.ROLE_NAME_FORMAT_ERROR);
        }

        //校验角色备注
        if (StringUtils.isNotBlank(dto.getDescription()) && RegexConst.ROLE_DESCRIPTION_LENGTH < dto.getDescription().length()) {
            throw new BusinessException(ResultCode.ROLE_NAME_FORMAT_ERROR);
        }
        String userId = getUserId();
        dto.setLoginUserId(userId);
        dto.setLoginOrgId(getOrgId());
        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("id", this.roleService.createRole(dto));
        return resultMap;
    }


    /**
     * 删除角色
     * @param paramsMap
     * @return
     */
    @RequiresPermissions("system:role:edit")
    @PostMapping(value = "deleteRoles", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteRoles(@RequestBody Map<String, List<Long>> paramsMap) {
        List<Long> idList = paramsMap.get("id");
        if (idList.size() < 1) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }

        Map<String, Object> resultMap = new HashMap<>(1);
        RoleBo roleBo = new RoleBo();
        roleBo.setOrgId(getOrgId());
        roleBo.setDeleteIdList(idList);
        resultMap.put("succeedCount", this.roleService.deleteRoles(roleBo));
        return resultMap;
    }

    /**
     * 查询角色详情（系统预置角色）
     * @param id
     * @return
     */
    @RequiresPermissions("system:role:detail")
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
     * @param roleId
     * @return
     */
    @GetMapping(value = "/getRoleOne", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleCreatedDTO getRoleOne(@RequestParam(value = "id", required = true) String roleId) {
        String orgId = getOrgId();
        String userId = getUserId();
        return roleService.getRoleAll(roleId, orgId, userId);
    }


    /**
     * 查询账号下的角色
     * @return
     */
    @GetMapping(value = "getRoleListByOrgId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RoleDTO> getRoleListByOrgId() {
        return this.roleService.getRoleListByOrgId(getOrgId(), getUserId());
    }


    /**
     * 编辑角色
     * @param roleBo
     * @return
     */
    @RequiresPermissions("system:role:edit")
    @PostMapping(value = "/updateRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateRole(@RequestBody RoleBo roleBo) {
        if (null == roleBo || StringUtils.isBlank(String.valueOf(roleBo.getId())) || StringUtils.isBlank(roleBo.getRoleName())
            || StringUtils.isBlank(roleBo.getRoleKey()) || StringUtils.isBlank(String.valueOf(roleBo.getPermit()))) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        try {
            String userId = getUserId();
            roleBo.setUserId(userId);
            roleBo.setOrgId(getOrgId());
            roleBo.getId();
            roleBo.getPermit();
        } catch (NumberFormatException nfe) {
            throw new BusinessException(ResultCode.INVALID_PARAM_TYPE);
        }

        //校验角色名称
        if (!roleBo.getRoleName().matches(RegexConst.ROLE_NAME)
            || ValidateUtil.calculateStrLength(roleBo.getRoleName()) > RegexConst.ROLE_NAME_LENGTH) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR);
        }

        //校验角色key
        if (!roleBo.getRoleKey().matches(RegexConst.ROLE_KEY)) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR);
        }

        //校验角色备注
        if (StringUtils.isNotBlank(roleBo.getDescription()) && RegexConst.ROLE_DESCRIPTION_LENGTH < roleBo.getDescription().length()) {
            throw new BusinessException(ResultCode.REQUEST_FORMAT_ERROR);
        }

        Map<String, Object> resultMap = new HashMap<>(1);
        roleBo.setOrgId(getOrgId());
        roleBo.setUserId(getUserId());
        resultMap.put("id", this.roleService.updateRole(roleBo));
        return resultMap;
    }
}
