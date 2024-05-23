package cn.cuiot.dmp.app.controller;

import cn.cuiot.dmp.app.dto.AppUserDto;
import cn.cuiot.dmp.app.dto.login.Code2SessionDto;
import cn.cuiot.dmp.app.dto.login.MiniLoginDto;
import cn.cuiot.dmp.app.service.AppLoginService;
import cn.cuiot.dmp.base.application.service.WeChatMiniAppService;
import cn.cuiot.dmp.base.application.utils.IpUtil;
import cn.cuiot.dmp.common.constant.IdmResDTO;
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
     * 用户登出
     */
    @PostMapping("logOut")
    public IdmResDTO logOut() {
        appLoginService.logOut(request);
        return IdmResDTO.success(null);
    }



}
