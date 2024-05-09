package cn.cuiot.dmp.system.api.controller;

import static cn.cuiot.dmp.common.constant.ResultCode.OLD_PASSWORD_IS_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.PARAM_CANNOT_NULL;
import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORDS_NOT_EQUALS;
import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORD_IS_EMPTY;
import static cn.cuiot.dmp.common.constant.ResultCode.PASSWORD_IS_INVALID;
import static cn.cuiot.dmp.common.constant.ResultCode.SMS_TEXT_OLD_INVALID;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_NOT_EXIST;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.controller.BaseController;
import cn.cuiot.dmp.base.application.utils.CommonCsvUtil;
import cn.cuiot.dmp.common.constant.CacheConst;
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
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetDepartmentTreeLazyResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.GetUserDepartmentTreeLazyReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.InsertUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LabelTypeDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ResetPasswordReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SimpleStringResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SmsCodeCheckReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SmsCodeCheckStrongerReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdatePasswordDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UpdateUserDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserCsvDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserDataResDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import cn.cuiot.dmp.system.infrastructure.utils.VerifyUnit;
import com.alibaba.fastjson.JSONObject;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
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
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 根据用户名查询
     */
    private static final Integer SEARCH_TYPE_USER_NAME = 1;

    /**
     * 根据手机号码查询
     */
    private static final Integer SEARCH_TYPE_PHONE = 2;

    /**
     * 根据角色查询
     */
    private static final Integer SEARCH_TYPE_ROLE_NAME = 3;

    /**
     * 根据组织名查询
     */
    private static final Integer SEARCH_TYPE_DEPARTMENT_NAME = 4;

    private static final String FALSE = "false";

    @Value("${self.debug}")
    private String debug;

    /**
     * 自动注入verifyUnit
     */
    @Autowired
    private VerifyUnit verifyUnit;

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
     * 批量删除用户
     */
    @PostMapping(value = "/user/deleteUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteUsers(@RequestBody List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        // 获取session中的userId
        Long userId = LoginInfoHolder.getCurrentUserId();
        if (Objects.isNull(userId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }
        // 获取session中的orgId
        String sessionOrgId = getOrgId();
        if (StringUtils.isEmpty(sessionOrgId)) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        UserBo userBo = new UserBo();
        userBo.setOrgId(sessionOrgId);
        userBo.setId(userId);
        userBo.setIds(ids);
        Map<String, Object> resultMap = new HashMap<>(1);
        resultMap.put("succeedCount", this.userService.deleteUsers(userBo));
        return resultMap;
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
     * 获取标签类型列表
     */
    @GetMapping(value = "/user/getLabelTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LabelTypeDto> getLabelTypeList(@RequestParam("labelType") String labelType) {
        if (labelType.isEmpty()) {
            throw new BusinessException(ResultCode.LABEL_TYPE_NOT_EXIST);
        }
        return userService.getLabelTypeList(labelType);
    }

    /**
     * 修改手机号
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

    /**
     * 修改手机号
     */
    @PostMapping(value = "/user/replacePhoneNumber", produces = MediaType.APPLICATION_JSON_VALUE)
    public void replacePhoneNumber(@RequestBody SmsCodeCheckStrongerReqDTO dto) {
        String phoneNumber = Optional.ofNullable(dto).map(d -> d.getPhoneNumber()).orElse(null);
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }
        // 手机号参数校验
        if (!phoneNumber.matches(RegexConst.PHONE_NUMBER_REGEX)) {
            throw new BusinessException(ResultCode.PHONE_NUMBER_IS_INVALID);
        }

        // 获取session中的userId
        Long userId = LoginInfoHolder.getCurrentUserId();
        if (Objects.isNull(userId)) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }

        // 加强校验，校验第一步的旧手机号验证
        String smsCodeOld = Optional.ofNullable(dto).map(SmsCodeCheckStrongerReqDTO::getSmsCodeOld)
                .orElse(null);
        if (StringUtils.isEmpty(smsCodeOld)) {
            // 短信验证码为空
            throw new BusinessException(SMS_TEXT_OLD_INVALID);
        }
        //0.9改动原手机号参数去除，由后端自行获取--2020/12/07
        String phoneNumberOld = Sm4
                .decrypt(userService.getUserById(userId.toString()).getPhoneNumber());
        boolean smsCodeVerified = true;
        if (FALSE.equals(debug)) {
            // 短信验证码验证
            try {
                smsCodeVerified = verifyUnit.checkSmsCode(
                        CacheConst.SMS_CODE_TEXT_REDIS_KEY_P + userId + phoneNumberOld,
                        phoneNumberOld + smsCodeOld);
            } catch (BusinessException e) {
                log.warn("短信验证码错误");
                throw new BusinessException(SMS_TEXT_OLD_INVALID);
            }
            // 验证成功
            if (!smsCodeVerified) {
                throw new BusinessException(SMS_TEXT_OLD_INVALID);
            }
        }

        // 获取短信验证码
        String smsCode = Optional.ofNullable(dto).map(d -> d.getSmsCode()).orElse(null);
        if (StringUtils.isEmpty(smsCode)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL);
        }

        UserBo userBo = new UserBo();
        userBo.setId(userId);
        userBo.setSmsCode(smsCode);
        userBo.setPhoneNumber(phoneNumber);
        userBo.setOrgId(getOrgId());
        // 更改手机号
        userService.updatePhoneNumber(userBo);
    }

    /**
     * 重置密码
     *
     * @param resetPasswordReqDTO 修改密码信息
     * @return SimpleStringResDTO
     */
    @PostMapping(value = "/user/resetPasswordByPhone", produces = MediaType.APPLICATION_JSON_VALUE)
    public SimpleStringResDTO resetPasswordByPhone(
            @RequestBody ResetPasswordReqDTO resetPasswordReqDTO) {
        if (resetPasswordReqDTO == null || StringUtils.isEmpty(resetPasswordReqDTO.getSmsCode())
                || StringUtils.isEmpty(resetPasswordReqDTO.getPassword())) {
            throw new BusinessException(PARAM_CANNOT_NULL);
        }
        // 获取密码
        String password = resetPasswordReqDTO.getPassword();
        // 密码参数校验
        if (StringUtils.isEmpty(password)) {
            {
            }
            // 密码为空
            throw new BusinessException(PASSWORD_IS_EMPTY);
        }
        // 密码参数校验
        if (!password.matches(RegexConst.PASSWORD_REGEX)) {
            // 密码不符合规则
            throw new BusinessException(PASSWORD_IS_INVALID);
        }
        if (!StringUtils.isEmpty(resetPasswordReqDTO.getPasswordAgain())
                && !password.equals(resetPasswordReqDTO.getPasswordAgain())) {
            throw new BusinessException(PASSWORDS_NOT_EQUALS);
        }
        UserBo userBo = new UserBo();
        userBo.setId(LoginInfoHolder.getCurrentUserId());
        userBo.setResetPasswordReqDTO(resetPasswordReqDTO);
        // 重置密码
        return userService.updatePasswordByPhoneWithoutSid(userBo);
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

}
