package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.application.config.AppProperties;
import cn.cuiot.dmp.base.application.constant.WxUrlConstant;
import cn.cuiot.dmp.base.application.dto.wechat.AccessTokenResp;
import cn.cuiot.dmp.base.application.dto.wechat.Code2SessionVo;
import cn.cuiot.dmp.base.application.dto.wechat.WechatPhoneResp;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author: wuyongchong
 * @date: 2024/5/22 11:13
 */
@Slf4j
@Service
public class WeChatMiniAppService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * accessTokena缓存Key
     */
    private final static String ACCESS_TOKEN_KEY = "WX_ACCESS_TOKEN";

    /**
     * accessTokena缓存时间（秒）
     */
    private final static Long ACCESS_TOKEN_TIMEOUT = 180L;

    /**
     * 会话密钥缓存key
     */
    private final String SESSION_KEY_CACHE_SUFFIX = ":MINI:SESSION_KEY";

    /**
     * 会话密钥缓时间（秒）
     */
    private final long SESSION_KEY_EXPIRE_TIME = 60 * 60 * 72;

    /**
     * URL编码
     */
    public static URI createURI(String url) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        return uriBuilder.build().encode().toUri();
    }

    /**
     * 获取微信openId
     */
    public String code2Session(String code) {
        String url = WxUrlConstant.CODE2SESSION_URL.replace("{appId}", appProperties.getAppId())
                .replace("{appSecret}", appProperties.getAppSecret())
                .replace("{code}", code);
        String resbody = restTemplate.getForObject(createURI(url), String.class);
        log.info("code2Session resbody:{}", resbody);
        Code2SessionVo code2SessionVo = JSON.parseObject(resbody, Code2SessionVo.class);
        if (Objects.isNull(code2SessionVo) || StringUtils.isBlank(code2SessionVo.getOpenId())) {
            throw new BusinessException(ResultCode.INNER_ERROR, "调用微信获取openId异常");
        }
        //将会话密钥存入redis
        redisTemplate.opsForValue().set(code2SessionVo.getOpenId() + SESSION_KEY_CACHE_SUFFIX,
                code2SessionVo.getSessionKey(),
                SESSION_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
        return code2SessionVo.getOpenId();
    }

    /**
     * 获取accessTokena
     */
    public String getAccessToken() {
        String accesstoken = (String) redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY);
        if (accesstoken == null || accesstoken == "") {
            AccessTokenResp accessTokenResp = restTemplate
                    .getForObject(WxUrlConstant.TOKEN_URL, AccessTokenResp.class,
                            appProperties.getAppId(), appProperties.getAppSecret());
            redisTemplate.opsForValue()
                    .set(ACCESS_TOKEN_KEY, accessTokenResp.getAccess_token(), ACCESS_TOKEN_TIMEOUT,
                            TimeUnit.SECONDS);
            accesstoken = (String) redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY);
        }
        return accesstoken;
    }

    /**
     * 根据微信获取的code置换手机号
     */
    public String getPhoneByWechatCode(String code) {
        String accessToken = getAccessToken();
        JSONObject paramJson = new JSONObject();
        paramJson.put("code", code);
        log.info("getPhoneByWechatCode parama:{},url:{}", JSON.toJSONString(paramJson),
                WxUrlConstant.USER_PHONENUMBER_URL);
        ResponseEntity<WechatPhoneResp> responseEntity =
                restTemplate.exchange(WxUrlConstant.USER_PHONENUMBER_URL, HttpMethod.POST,
                        new HttpEntity<>(paramJson),
                        new ParameterizedTypeReference<WechatPhoneResp>() {
                        }, accessToken);
        log.info("getPhoneByWechatCode res:{}", responseEntity);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            throw new BusinessException(ResultCode.INNER_ERROR, "调用微信获取手机号异常");
        }
        if (responseEntity.getBody() == null || Objects
                .isNull(responseEntity.getBody().getPhone_info())) {
            throw new BusinessException(ResultCode.INNER_ERROR, "调用微信获取手机号异常");
        }
        if (!StringUtils.equals(NumberConst.STR_ZERO, responseEntity.getBody().getErrcode())) {
            throw new BusinessException(ResultCode.INNER_ERROR, "调用微信获取手机号异常");
        }
        return responseEntity.getBody().getPhone_info().getPurePhoneNumber();
    }

}
