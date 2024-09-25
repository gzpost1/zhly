package cn.cuiot.dmp.app.controller.app;

import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.dto.user.*;
import cn.cuiot.dmp.app.service.AppAuthService;
import cn.cuiot.dmp.app.service.AppUserService;
import cn.cuiot.dmp.app.service.AppVerifyService;
import cn.cuiot.dmp.base.application.annotation.LogRecord;
import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.base.application.constant.OperationSourceContants;
import cn.cuiot.dmp.base.application.rocketmq.LogSendService;
import cn.cuiot.dmp.base.application.service.WeChatMiniAppService;
import cn.cuiot.dmp.base.application.utils.IpUtil;
import cn.cuiot.dmp.base.infrastructure.syslog.LogContextHolder;
import cn.cuiot.dmp.base.infrastructure.syslog.OptTargetInfo;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.LogLevelEnum;
import cn.cuiot.dmp.common.enums.StatusCodeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.domain.types.enums.UserTypeEnum;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_LOCKED_ERROR;
import static cn.cuiot.dmp.common.constant.ResultCode.USER_ACCOUNT_NOT_EXIST;

/**
 * 【APP】认证与用户接口
 *
 * @author: wuyongchong
 * @date: 2024/5/22 11:44
 */
@Slf4j
@RestController
@RequestMapping("/app/auth")
public class AppAuthController {

    @Autowired
    private WeChatMiniAppService weChatMiniAppService;

    @Autowired
    private AppAuthService appAuthService;

    @Autowired
    private AppVerifyService appVerifyService;

    @Resource
    protected HttpServletRequest request;

    @Autowired
    private LogSendService sendService;
    @Autowired
    private AppUserService appUserService;

    public final static Long PLATFORM_COMPANY_ID = 1L;

    /**
     * 获取微信openId
     */
    @PostMapping("code2session")
    public IdmResDTO code2session(@RequestBody @Valid Code2SessionDto dto) {
        String openid = weChatMiniAppService.code2Session(dto.getCode());
        Map<String, Object> data = Maps.newHashMap();
        data.put("openid", openid);
        String ipAddr = IpUtil.getIpAddr(request);
        List<AppUserDto> userList = appAuthService.openidLogin(openid, ipAddr);

        if (CollectionUtils.isNotEmpty(userList)) {
            Optional<AppUserDto> firstOptional = userList.stream()
                    .filter(item -> UserTypeEnum.USER.getValue().equals(item.getUserType()))
                    .findFirst();
            if (firstOptional.isPresent()) {
                AppUserDto appUserDto = firstOptional.get();

                OperateLogDto operateLogDto = new OperateLogDto();
                operateLogDto.setOperationSource(OperationSourceContants.APP_MANAGE_END);
                operateLogDto.setOrgId(appUserDto.getOrgId());
                operateLogDto.setRequestTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                        DateTimeUtil.DEFAULT_DATETIME_FORMAT)));
                operateLogDto.setRequestIp(IpUtil.getIpAddr(request));
                operateLogDto.setOperationCode("code2session");
                operateLogDto.setOperationName("登录系统");
                operateLogDto.setOperationById(appUserDto.getId().toString());
                operateLogDto.setOperationByName(appUserDto.getName());
                operateLogDto.setUserType(UserTypeEnum.USER.getValue());
                operateLogDto.setOperationTargetInfo("");
                operateLogDto.setServiceType("login");
                operateLogDto.setServiceTypeName("登录");
                operateLogDto.setLogLevel(LogLevelEnum.INFO.getCode());
                operateLogDto.setStatusCode(StatusCodeEnum.SUCCESS.getCode());
                // 发送记录日志消息
                sendService.sendOperaLog(operateLogDto);

            }
        }

        data.put("userList", userList);
        return IdmResDTO.success(data);
    }

    /**
     * 小程序授权登录
     */
    @LogRecord(operationCode = "miniLogin", operationName = "登录系统", serviceType = "login", serviceTypeName = "登录")
    @PostMapping("miniLogin")
    public IdmResDTO miniLogin(@RequestBody @Valid MiniLoginDto dto) {
        //根据微信获取的code置换手机号
        String phone = weChatMiniAppService.getPhoneByWechatCode(dto.getCode());
        //用户身份
        Integer userType = dto.getUserType();
        //微信openid
        String openid = dto.getOpenid();

        String ipAddr = IpUtil.getIpAddr(request);

        AppUserDto userDto = appAuthService.miniLogin(phone, userType, openid, ipAddr);

        //设置日志操作对象内容
        if (StringUtils.isNotBlank(userDto.getOrgId())) {
            LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                    .companyId(Long.valueOf(userDto.getOrgId()))
                    .operationById(userDto.getId().toString())
                    .operationByName(userDto.getName())
                    .userType(UserTypeEnum.USER.getValue())
                    .build());
        }

        return IdmResDTO.success(userDto);
    }

    /**
     * 获取图形验证码
     */
    @PostMapping("kaptcha")
    public KaptchaResDTO createKaptcha() {
        // 返回图形验证码
        return appVerifyService.createKaptchaImage();
    }

    /**
     * 获取对称密钥信息
     */
    @PostMapping("secretKey")
    public SecretKeyResDTO createSecretKey() {
        // 返回密钥信息
        return appVerifyService.createSecretKey();
    }

    /**
     * 密码登录
     */
    @LogRecord(operationCode = "pwdLogin", operationName = "登录系统", serviceType = "login", serviceTypeName = "登录")
    @PostMapping("pwdLogin")
    public IdmResDTO pwdLogin(@RequestBody @Valid PwdLoginDto dto) {
        String ipAddr = IpUtil.getIpAddr(request);
        dto.setIpAddr(ipAddr);
        AppUserDto userDto = appAuthService.pwdLogin(dto);

        //设置日志操作对象内容
        if (StringUtils.isNotBlank(userDto.getOrgId())) {
            LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                    .companyId(Long.valueOf(userDto.getOrgId()))
                    .operationById(userDto.getId().toString())
                    .operationByName(userDto.getName())
                    .userType(UserTypeEnum.USER.getValue())
                    .build());
        }

        return IdmResDTO.success(userDto);
    }

    /**
     * 手机号登录
     */
    @LogRecord(operationCode = "phoneLogin", operationName = "登录系统", serviceType = "login", serviceTypeName = "登录")
    @PostMapping("phoneLogin")
    public IdmResDTO phoneLogin(@RequestBody @Valid PhoneLoginDto dto) {
        String ipAddr = IpUtil.getIpAddr(request);
        dto.setIpAddr(ipAddr);
        AppUserDto userDto = appAuthService.phoneLogin(dto);

        //设置日志操作对象内容
        if (StringUtils.isNotBlank(userDto.getOrgId())) {
            LogContextHolder.setOptTargetInfo(OptTargetInfo.builder()
                    .companyId(Long.valueOf(userDto.getOrgId()))
                    .operationById(userDto.getId().toString())
                    .operationByName(userDto.getName())
                    .userType(UserTypeEnum.USER.getValue())
                    .build());
        }

        return IdmResDTO.success(userDto);
    }

    /**
     * 密码重置
     */
    @PostMapping("resetPwd")
    public IdmResDTO resetPwd(@RequestBody @Valid PwdResetDto dto) {
        String ipAddr = IpUtil.getIpAddr(request);
        dto.setIpAddr(ipAddr);
        appAuthService.resetPwd(dto);
        return IdmResDTO.success(null);
    }

    /**
     * 修改密码
     */
    @PostMapping("changePwd")
    public IdmResDTO changePwd(@RequestBody @Valid PwdChangeDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        appAuthService.changePwd(dto);
        return IdmResDTO.success(null);
    }

    /**
     * 获得登录用户信息
     */
    @PostMapping("loginUserInfo")
    public IdmResDTO<AppUserDto> loginUserInfo() {
        Long sessionUserId = LoginInfoHolder.getCurrentUserId();
        Long sessionOrgId = LoginInfoHolder.getCurrentOrgId();
        AppUserDto userDto = appAuthService.getLoginUserInfo(sessionUserId);
        return IdmResDTO.success(userDto);
    }

    /**
     * 设置用户头像与昵称
     */
    @PostMapping("setSampleUserInfo")
    public IdmResDTO setSampleUserInfo(@RequestBody @Valid SampleUserInfoDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        appAuthService.setSampleUserInfo(dto);
        return IdmResDTO.success(null);
    }

    /**
     * 发送手机号验证码
     */
    @PostMapping("sendPhoneSmsCode")
    public IdmResDTO<SmsCodeResDto> sendPhoneSmsCode(@RequestBody @Valid SmsCodeReqDto dto) {
        // 验证码ID参数校验
        if (StringUtils.isBlank(dto.getSid())) {
            throw new BusinessException(ResultCode.ACCESS_ERROR, "图形验证码ID参数为空");
        }
        // 验证码参数校验
        if (StringUtils.isBlank(dto.getKaptchaText())) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_IS_EMPTY, "请输入图形验证码");
        }
        //图形验证码校验
        if (!appVerifyService.checkKaptchaText(dto.getKaptchaText(), dto.getSid())) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_ERROR, "图形验证码错误");
        }
        SmsCodeResDto res = new SmsCodeResDto();
        if (UserTypeEnum.USER.getValue().equals(dto.getUserType())) {
            AppUserDto userDto = appUserService.getUserByPhoneAndUserType(dto.getPhoneNumber(), dto.getUserType());
            // 账号不存在
            if (Objects.isNull(userDto)) {
                throw new BusinessException(USER_ACCOUNT_NOT_EXIST);
            }
            // 账号被停用
            if (EntityConstants.DISABLED.equals(userDto.getStatus())) {
                throw new BusinessException(USER_ACCOUNT_LOCKED_ERROR);
            }
            res = appVerifyService.sendPhoneSmsCode(dto.getPhoneNumber(), null, Long.parseLong(userDto.getOrgId()));
        } else {
            res = appVerifyService.sendPhoneSmsCode(dto.getPhoneNumber(), null, PLATFORM_COMPANY_ID);
        }
        return IdmResDTO.success(res);
    }

    /**
     * 校验短信验证码
     */
    @PostMapping("checkPhoneSmsCode")
    public IdmResDTO<SmsCodeCheckResDto> checkPhoneSmsCode(
            @RequestBody @Valid SmsCodeCheckReqDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        String phoneNumber = Optional.ofNullable(dto).map(d -> d.getPhoneNumber()).orElse(null);
        if (StringUtils.isBlank(phoneNumber)) {
            throw new BusinessException(ResultCode.PARAM_CANNOT_NULL, "手机号不能为空");
        }
        String smsCode = Optional.ofNullable(dto).map(d -> d.getSmsCode()).orElse(null);
        if (StringUtils.isBlank(smsCode)) {
            throw new BusinessException(ResultCode.KAPTCHA_TEXT_IS_EMPTY, "短信验证码不能为空");
        }
        SmsCodeCheckResDto res = appVerifyService
                .checkPhoneSmsCode(dto.getPhoneNumber(), dto.getUserId(), smsCode, false);
        return IdmResDTO.success(res);
    }

    /**
     * 修改手机号
     */
    @PostMapping("changePhone")
    public IdmResDTO changePhone(@RequestBody @Valid ChangePhoneDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        appAuthService.changePhone(dto);
        return IdmResDTO.success(null);
    }

    /**
     * 切换身份
     */
    @PostMapping("switchUserType")
    public IdmResDTO<AppUserDto> switchUserType(@RequestBody @Valid SwitchUserTypeDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        AppUserDto userDto = appAuthService.switchUserType(dto);
        return IdmResDTO.success(userDto);
    }

    /**
     * 用户登出
     */
    @ResolveExtData
    @LogRecord(operationCode = "logOut", operationName = "退出系统", serviceType = "logOut", serviceTypeName = "退出")
    @PostMapping("logOut")
    public IdmResDTO logOut() {
        appAuthService.logOut(request);
        return IdmResDTO.success(null);
    }

}
