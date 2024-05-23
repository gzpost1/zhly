package cn.cuiot.dmp.app.controller;

import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.dto.user.ChangePhoneDto;
import cn.cuiot.dmp.app.dto.user.Code2SessionDto;
import cn.cuiot.dmp.app.dto.user.KaptchaResDTO;
import cn.cuiot.dmp.app.dto.user.MiniLoginDto;
import cn.cuiot.dmp.app.dto.user.SampleUserInfoDto;
import cn.cuiot.dmp.app.dto.user.SecretKeyResDTO;
import cn.cuiot.dmp.app.service.AppLoginService;
import cn.cuiot.dmp.app.service.AppVerifyService;
import cn.cuiot.dmp.base.application.service.WeChatMiniAppService;
import cn.cuiot.dmp.base.application.utils.IpUtil;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * App认证接口
 *
 * @author: wuyongchong
 * @date: 2024/5/22 11:44
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private WeChatMiniAppService weChatMiniAppService;

    @Autowired
    private AppLoginService appLoginService;

    @Autowired
    private AppVerifyService appVerifyService;

    @Resource
    protected HttpServletRequest request;

    /**
     * 获取微信openId
     */
    @PostMapping("code2session")
    public IdmResDTO code2session(@RequestBody @Valid Code2SessionDto dto) {
        String openid = weChatMiniAppService.code2Session(dto.getCode());
        Map<String, Object> data = Maps.newHashMap();
        data.put("openid", openid);
        return IdmResDTO.success(data);
    }

    /**
     * 小程序授权登录
     */
    @PostMapping("miniLogin")
    public IdmResDTO miniLogin(@RequestBody @Valid MiniLoginDto dto) {
        //根据微信获取的code置换手机号
        String phone = weChatMiniAppService.getPhoneByWechatCode(dto.getCode());
        //用户身份
        Integer userType = dto.getUserType();
        //微信openid
        String openid = dto.getOpenid();

        String ipAddr = IpUtil.getIpAddr(request);

        AppUserDto userDto = appLoginService.miniLogin(phone, userType, openid, ipAddr);

        return IdmResDTO.success(userDto);
    }

    /**
     * 设置用户头像与昵称
     */
    @PostMapping("setSampleUserInfo")
    public IdmResDTO setSampleUserInfo(@RequestBody @Valid SampleUserInfoDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        appLoginService.setSampleUserInfo(dto);
        return IdmResDTO.success(null);
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
     * 发送绑定手机号验证码
     */
    @PostMapping("sendBindPhoneSmsCode")
    public IdmResDTO sendBindPhoneSmsCode(@RequestBody @Valid ChangePhoneDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        appLoginService.changePhone(dto);
        return IdmResDTO.success(null);
    }

    /**
     * 修改手机号
     */
    @PostMapping("changePhone")
    public IdmResDTO changePhone(@RequestBody @Valid ChangePhoneDto dto) {
        dto.setUserId(LoginInfoHolder.getCurrentUserId());
        appLoginService.changePhone(dto);
        return IdmResDTO.success(null);
    }

    /**
     * 用户登出
     */
    @PostMapping("logOut")
    public IdmResDTO logOut() {
        appLoginService.logOut(request);
        return IdmResDTO.success(null);
    }


}
