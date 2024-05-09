package cn.cuiot.dmp.system.api.controller;

import static cn.cuiot.dmp.common.constant.ResultCode.OLD_PASSWORD_IS_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORDS_NOT_EQUALS;
import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORD_IS_INVALID;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_NOT_EXIST;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.application.utils.CommonCsvUtil;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.common.utils.ValidateUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.service.DepartmentService;
import cn.cuiot.dmp.system.application.service.UserService;
import cn.cuiot.dmp.system.infrastructure.entity.DepartmentEntity;
import cn.cuiot.dmp.system.infrastructure.entity.UserDataEntity;
import cn.cuiot.dmp.system.infrastructure.entity.bo.UserBo;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ChangeUserStatusDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ExportUserCmd;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetUserDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.MoveUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SmsCodeCheckReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdatePasswordDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import com.alibaba.fastjson.JSONObject;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 *
 * @author guoying
 * @className LogControllerImpl
 * @description 用户管理
 * @date 2020-10-23 10:00:07
 */
@RestController
@Slf4j
public class UserController extends BaseController {

    /**
     * 最大分页数
     */
    private static final int MAX_PAGE_SIZE = 100;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping(value = "/user/getDGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getDGroup() {
        return userService.getDGroup(getOrgId(), getUserId());
    }

    /**
     * 用户列表筛选-分页
     */
    @GetMapping(value = "/user/listUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<UserDataResDTO> getPage(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "deptIds", required = false) String deptIds,
            @RequestParam(value = "status", required = false) Byte status,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        if (pageSize > MAX_PAGE_SIZE) {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT);
        }

        Map<String, Object> params = new HashMap<>(2);
        // 获取session中的orgId
        String sessionOrgId = getOrgId();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        params.put("orgId", sessionOrgId);
        params.put("loginUserId", getUserId());

        // 组织为空则使用当前登陆用户的组织
        if (deptId == null) {
            deptId = Optional.ofNullable(userService.getDeptId(getUserId(), sessionOrgId))
                    .map(Long::valueOf).orElse(null);
        }
        // 获取子组织
        if (deptId != null) {
            DepartmentEntity departmentEntity = departmentService
                    .getDeptById(String.valueOf(deptId));
            if (departmentEntity != null) {
                params.put("path", departmentEntity.getPath());
            }
        }

        if(!StringUtils.isEmpty(deptIds)){
            params.put("deptIds",Splitter.on(",").splitToList(deptIds));
        }

        if (Objects.nonNull(status)) {
            params.put("status", status);
        }
        if (!StringUtils.isEmpty(name)) {
            params.put("name", name);
        }
        if (!StringUtils.isEmpty(userName)) {
            params.put("userName", userName);
        }
        if (!StringUtils.isEmpty(phoneNumber)) {
            String decrypt = Sm4.encryption(phoneNumber);
            params.put("phone", decrypt);
        }
        return userService.getPage(params, sessionOrgId, currentPage, pageSize);
    }

    /**
     * 查询用户详情
     */
    @RequiresPermissions
    @GetMapping(value = "/user/getUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDataResDTO getDetail(@RequestParam(value = "id") String id) {
        // 获取session中的orgId
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        //对敏感信息脱敏返回
        return SensitiveUtil.desCopy(userService.getOne(id, sessionOrgId, sessionUserId));
    }

    /**
     * 新增用户
     */
    @RequiresPermissions
    @PostMapping(value = "/user/insertUserD", produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertUser(@RequestBody @Valid InsertUserDTO dto) {
        String loginOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        String loginUserId = LoginInfoHolder.getCurrentUserId().toString();

        UserBo userBo = new UserBo();
        userBo.setOrgId(loginOrgId);
        userBo.setLoginUserId(loginUserId);
        userBo.setRoleId(dto.getRoleId());
        userBo.setUsername(dto.getUsername());
        userBo.setName(dto.getName());
        userBo.setPhoneNumber(dto.getPhoneNumber());
        userBo.setDeptId(dto.getDeptId());
        userBo.setPostId(dto.getPostId());
        userBo.setRemark(dto.getRemark());

        UserCsvDto userInfo = userService.insertUser(userBo);

        // 文件流输出
        List<JSONObject> jsonList = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSON(userInfo).toString());
        jsonList.add(jsonObject);

        List<Object> head = new ArrayList<>();
        head.add("username");
        head.add("password");

        response.setContentType("text/csv;charset=\"GBK\"");
        response.setHeader("Content-Disposition", "attachment; filename=credentials.csv");
        String[] split = userBo.getUsername().split("@");
        String username = split[0];
        try {
            CommonCsvUtil.createCsvFile(head, jsonList, username + "credentials", response);
        } catch (UnsupportedEncodingException e) {
            log.error("insertUser error.", e);
        }
    }

    /**
     * 修改用户
     */
    @RequiresPermissions
    @PostMapping(value = "/user/updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO updateUser(@RequestBody @Valid UpdateUserDTO dto) {

        UserBo userBo = new UserBo();
        userBo.setId(dto.getId());
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setRoleId(dto.getRoleId());
        userBo.setUsername(dto.getUsername());
        userBo.setName(dto.getName());
        userBo.setPhoneNumber(dto.getPhoneNumber());
        userBo.setDeptId(dto.getDeptId());
        userBo.setPostId(dto.getPostId());
        userBo.setRemark(dto.getRemark());

        return userService.updateUser(userBo);
    }


    /**
     * 批量移动用户
     */
    @RequiresPermissions
    @PostMapping(value = "/user/moveUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO moveUsers(@RequestBody @Valid MoveUserDTO dto) {

        UserBo userBo = new UserBo();
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setDeptId(dto.getDeptId());
        userBo.setIds(dto.getIds());

        userService.moveUsers(userBo);

        return IdmResDTO.success();
    }

    /**
     * 批量启停用
     */
    @RequiresPermissions
    @PostMapping(value = "/user/changeUserStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public IdmResDTO changeUserStatus(@RequestBody @Valid ChangeUserStatusDTO dto) {

        UserBo userBo = new UserBo();
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setStatus(dto.getStatus());
        userBo.setIds(dto.getIds());

        userService.changeUserStatus(userBo);

        return IdmResDTO.success();
    }

    /**
     * 批量删除用户
     */
    @RequiresPermissions
    @PostMapping(value = "/user/deleteUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteUsers(@RequestBody List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        // 获取session中的userId
        Long sessionUserId = LoginInfoHolder.getCurrentUserId();
        if (Objects.isNull(sessionUserId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST, "获取当前登录信息失败");
        }
        // 获取session中的orgId
        String sessionOrgId = getOrgId();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST, "获取当前登录信息失败");
        }
        UserBo userBo = new UserBo();
        userBo.setOrgId(sessionOrgId);
        userBo.setLoginUserId(sessionUserId.toString());
        userBo.setIds(ids);
        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("succeedCount", this.userService.deleteUsers(userBo));
        return resultMap;
    }


    /**
     * 导出用户
     */
    @RequiresPermissions
    @PostMapping(value = "/user/exportUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public void exportUsers(@RequestBody @Valid ExportUserCmd dto) {
        UserBo userBo = new UserBo();
        userBo.setOrgId(LoginInfoHolder.getCurrentOrgId().toString());
        userBo.setLoginUserId(LoginInfoHolder.getCurrentUserId().toString());
        userBo.setDeptId(dto.getDeptId());
        List<UserDataResDTO> list = userService.exportUsers(userBo);

    }


    /**
     * 用户管理组织树（懒加载）
     */
    @GetMapping(value = "/user/getUserDepartmentTreeLazy", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GetDepartmentTreeLazyResDto> getUserDepartmentTreeLazy(
            @Valid GetUserDepartmentTreeLazyReqDto dto) {
        dto.setLoginUserId(getUserId());
        dto.setLoginOrgId(getOrgId());
        return userService.getUserDepartmentTreeLazy(dto);
    }

    /**
     * 查询登录用户信息
     */
    @PostMapping(value = "/getLoginUserInfo", produces = "application/json;charset=UTF-8")
    public UserResDTO queryUserById() {
        // 获取session中的userId
        String sessionUserId = LoginInfoHolder.getCurrentUserId().toString();
        String sessionOrgId = LoginInfoHolder.getCurrentOrgId().toString();
        // 判断是否具有查询权限
        UserResDTO userResDTO = userService
                .getUserMenuByUserIdAndOrgId(sessionUserId, sessionOrgId);
        if (userResDTO == null) {
            // 账号不存在
            throw new BusinessException(USER_ACCOUNT_NOT_EXIST);
        }
        //对加密的手机号码解密--2020/12/08
        //增加手机号为空判断，在不为空的情况下才做解密处理 --2020/12/15
        if (null != userResDTO.getPhoneNumber()) {
            userResDTO.setPhoneNumber(Sm4.decrypt(userResDTO.getPhoneNumber()));
        }
        //安全考虑去除密码返回
        userResDTO.setPassword(null);
        return SensitiveUtil.desCopy(userResDTO);
    }

    /**
     * 修改密码(登录人自行修改)
     */
    @PostMapping(value = "/user/updatePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePassword(@RequestBody UpdatePasswordDto dto) {
        // 获取密码
        ValidateUtil.validate(dto);
        final String password = dto.getPassword();
        final String newPassword = dto.getNewPassword();
        final String newPasswordAgain = dto.getNewPasswordAgain();
        // 密码参数校验
        if (!newPassword.equals(newPasswordAgain)) {
            throw new BusinessException(PASSWORDS_NOT_EQUALS);
        }
        // 判断密码不符合规则
        if (!newPassword.matches(RegexConst.PASSWORD_REGEX) || ValidateUtil.checkRepeat(password)
                || ValidateUtil.checkBoardContinuousChar(password)) {
            throw new BusinessException(PASSWORD_IS_INVALID);
        }
        Long userId = LoginInfoHolder.getCurrentUserId();
        UserResDTO loginUser = userService.getUserById(userId.toString());
        String oldPassword = Sm4.decrypt(loginUser.getPassword());
        if (!password.equals(oldPassword)) {
            throw new BusinessException(OLD_PASSWORD_IS_ERROR);
        }
        UserDataEntity userDataEntity = new UserDataEntity();
        userDataEntity.setPassword(Sm4.encryption(newPassword));
        userDataEntity.setId(userId);
        // 更改密码
        userService.updatePassword(userDataEntity);
    }

    /**
     * 修改手机号(登录人自行修改)
     */
    @PostMapping(value = "/user/updatePhoneNumber", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updatePhoneNumber(@RequestBody SmsCodeCheckReqDTO dto) {
        String phoneNumber = Optional.ofNullable(dto).map(d -> d.getPhoneNumber()).orElse(null);
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        // 手机号参数校验
        if (!phoneNumber.matches(RegexConst.PHONE_NUMBER_REGEX)) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID);
        }
        // 获取短信验证码
        String smsCode = Optional.ofNullable(dto).map(d -> d.getSmsCode()).orElse(null);
        if (StringUtils.isEmpty(smsCode)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        // 获取session中的userId
        Long userId = LoginInfoHolder.getCurrentUserId();
        if (Objects.isNull(userId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }

        UserBo userBo = new UserBo();
        userBo.setId(userId);
        userBo.setSmsCode(smsCode);
        userBo.setPhoneNumber(phoneNumber);
        userBo.setOrgId(getOrgId());
        // 更改手机号
        userService.updatePhoneNumber(userBo);
    }


}
