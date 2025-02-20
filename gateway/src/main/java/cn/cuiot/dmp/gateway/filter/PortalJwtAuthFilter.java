package cn.cuiot.dmp.gateway.filter;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.UserLongTimeLoginEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.gateway.config.AppProperties;
import cn.cuiot.dmp.gateway.config.GatewayAccessLimitProperties;
import cn.cuiot.dmp.gateway.config.IgnoreAuthProperties;
import cn.cuiot.dmp.gateway.service.SignatureService;
import cn.cuiot.dmp.gateway.utils.SecrtUtil;
import com.alibaba.nacos.common.utils.MD5Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关认证过滤器
 *
 * @author: wuyongchong
 * @date: 2024/5/10 15:43
 **/
@Slf4j
@Component
public class PortalJwtAuthFilter implements GlobalFilter, Ordered {

    /**
     * 请求头
     */
    public static final String TOKEN = "token";

    /**
     * 请求头
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * 用户组织
     */
    public static final String USERORG = "org";

    /**
     * 用户Id
     */
    public static final String USERID = "userId";

    /**
     * 内部token头部名称
     */
    public final static String INNER_TOKEN_NAME = "access-token";

    /**
     * 用户接口限制次数缓存Key前缀
     */
    public static final String USER_API_LIMIT_KEY_PREFIX = "userApiLimitKey:";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private IgnoreAuthProperties ignoreAuthProperties;

    @Autowired
    private GatewayAccessLimitProperties gatewayAccessLimitProperties;

    private final AntPathMatcher urlMatcher = new AntPathMatcher();

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        URI uri = serverHttpRequest.getURI();

        //初始化透传配置
        initConfig();

        //透传URL
        Iterator<String> it = SecrtUtil.filterThroughUrl.iterator();
        while (it.hasNext()) {
            String urlThrough = it.next();
            if (urlMatcher.match(urlThrough, uri.getPath())) {
                return chain.filter(exchange);
            }
        }
        HttpHeaders headers = serverHttpRequest.getHeaders();

        //内部token校验
        if (Boolean.TRUE.equals(appProperties.getEnableGatewayAccessTokenRequest())) {
            List<String> accessTokenList = headers.get(INNER_TOKEN_NAME);
            if (CollectionUtils.isNotEmpty(accessTokenList)) {
                String headAccessToken = accessTokenList.get(0);
                if (appProperties.getAccessToken().equals(headAccessToken)) {
                    return chain.filter(exchange);
                }
            }
        }
        /**
         * 用户token校验
         */
        // 如果当前url跳过鉴权，则不进行token校验
        String currentUrl = uri.getPath();
        if (shouldIgnoreAuth(currentUrl)) {
            return chain.filter(exchange);
        }

        // 签名校验
        boolean needCheckSignature = signatureService.isNeedCheckSignature(exchange);
        if (needCheckSignature) {
            return signatureService.checkSignature(exchange, chain);
        }

        List<String> list = headers.get(TOKEN);
        if (list == null || list.isEmpty()) {
            list = headers.get(AUTHORIZATION);
        }
        if (list == null || list.isEmpty()) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }
        String jwt = list.get(0);

        if (StringUtils.isEmpty(jwt)) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        } else {
            //解析与校验token
            String userId = checkToken(jwt);
            //检测访问流量限制
            checkFlowLimit(currentUrl,userId);
        }
        return chain.filter(exchange);
    }

    private void initConfig() {
        if (SecrtUtil.filterThroughUrl.size() == 0) {
            SAXReader saxReader = new SAXReader();
            InputStream inputStream = null;
            try {
                ClassPathResource classPathResource = new ClassPathResource("secret-config.xml");
                inputStream = classPathResource.getInputStream();
                Document document = saxReader.read(inputStream);
                Element users = document.getRootElement();
                for (Iterator i = users.elementIterator(); i.hasNext(); ) {
                    Element user = (Element) i.next();
                    for (Iterator j = user.elementIterator(); j.hasNext(); ) {
                        Element node = (Element) j.next();
                        SecrtUtil.filterThroughUrl.add(node.attribute(0).getValue());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析与校验token
     */
    private String checkToken(String jwt) {
        try {

            Claims claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();

            String userId = claims.get(USERID).toString();
            String orgId = Objects.nonNull(claims.get(USERORG))?claims.get(USERORG).toString():null;

            //踢下线
            if (!StringUtils.isEmpty(orgId)) {
                String toKick = redisTemplate.opsForValue().get(CacheConst.LOGIN_ORG_TO_KICK);
                if (!StringUtils.isEmpty(toKick)) {
                    String[] kickOrgIdArr = toKick.split(",");
                    List<String> kickOrgIds = Arrays.asList(kickOrgIdArr);
                    if (kickOrgIds.contains(orgId)) {
                        throw new BusinessException(ResultCode.LOGIN_INVALID);
                    }
                }
            }

            // PC端jwt缓存
            String jwtRedis = redisTemplate.opsForValue().get(CacheConst.LOGIN_USERS_JWT + jwt);

            //app端jwt缓存
            String jwtRedisWx = redisTemplate.opsForValue().get(CacheConst.LOGIN_USERS_JWT_WX + jwt);

            if (StringUtils.isEmpty(jwtRedis) && StringUtils.isEmpty(jwtRedisWx)) {
                throw new BusinessException(ResultCode.LOGIN_INVALID);
            } else if (!userId.equals(jwtRedis) && !userId.equals(jwtRedisWx)) {
                throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
            }

            if (!StringUtils.isEmpty(jwtRedis)) {

                if (Objects.equals(UserLongTimeLoginEnum.OPEN.getCode(),
                        redisTemplate.opsForValue().get(CacheConst.USER_LONG_TIME_LOGIN + userId))) {

                    redisTemplate.expire(CacheConst.LOGIN_USERS_JWT + jwt,
                            Const.USER_LONG_TIME_LOGIN_SESSION_TIME, TimeUnit.SECONDS);

                    redisTemplate.expire(CacheConst.USER_LONG_TIME_LOGIN + userId,
                            Const.USER_LONG_TIME_LOGIN_SESSION_TIME, TimeUnit.SECONDS);

                } else {
                    redisTemplate.expire(CacheConst.LOGIN_USERS_JWT + jwt, Const.SESSION_TIME,
                            TimeUnit.SECONDS);
                }

            } else if (!StringUtils.isEmpty(jwtRedisWx)) {
                redisTemplate.expire(CacheConst.LOGIN_USERS_JWT_WX + jwt, Const.WX_SESSION_TIME,
                        TimeUnit.SECONDS);
            }
            return userId;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (ex instanceof ExpiredJwtException) {
                throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED, "登录时效已过期");
            } else {
                throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
            }
        }
    }

    /**
     * 跳过鉴权的路径
     *
     * @param currentUrl 当前请求路径
     */
    private boolean shouldIgnoreAuth(String currentUrl) {
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String ignoreAuthPath : ignoreAuthProperties.getUrls()) {
            if (pathMatcher.match(ignoreAuthPath, currentUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测访问流量限制
     */
    private void checkFlowLimit(String url,String userId){
        String key = USER_API_LIMIT_KEY_PREFIX+userId+":"+ MD5Utils.md5Hex(url,"UTF-8");
        //获取RRateLimiter对象
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);

        if (shouldSpecialLimitUrl(url)) {
            log.info("SpecialL url:{},key:{}",url,key);
            Integer specialApiAccessLimit=0;
            String urlRedisLimit = redisTemplate.opsForValue().get(USER_API_LIMIT_KEY_PREFIX + key);
            if(org.apache.commons.lang3.StringUtils.isNotBlank(urlRedisLimit)){
                specialApiAccessLimit = Integer.valueOf(urlRedisLimit);
            }
            //特殊URL接口
            if(!specialApiAccessLimit.equals(gatewayAccessLimitProperties.getSpecialApiAccessLimit())){
                specialApiAccessLimit = gatewayAccessLimitProperties.getSpecialApiAccessLimit();
                rateLimiter.setRate(RateType.OVERALL, specialApiAccessLimit, 1, RateIntervalUnit.MINUTES);
                redisTemplate.opsForValue().set(USER_API_LIMIT_KEY_PREFIX + key,specialApiAccessLimit.toString());
            }else {
                rateLimiter.trySetRate(RateType.OVERALL, specialApiAccessLimit, 1, RateIntervalUnit.MINUTES);
            }

        }else if (shouldOperateLimitUrl(url)) {
            log.info("Operate url:{},key:{}}",url,key);
            //操作类接口限制访问次数
            Integer operateApiAccessLimit=0;
            String urlRedisLimit = redisTemplate.opsForValue().get(USER_API_LIMIT_KEY_PREFIX + key);
            if(org.apache.commons.lang3.StringUtils.isNotBlank(urlRedisLimit)){
                operateApiAccessLimit = Integer.valueOf(urlRedisLimit);
            }
            //特殊URL接口
            if(!operateApiAccessLimit.equals(gatewayAccessLimitProperties.getOperateApiAccessLimit())){
                operateApiAccessLimit = gatewayAccessLimitProperties.getOperateApiAccessLimit();
                rateLimiter.setRate(RateType.OVERALL, operateApiAccessLimit, 1, RateIntervalUnit.MINUTES);
                redisTemplate.opsForValue().set(USER_API_LIMIT_KEY_PREFIX + key,operateApiAccessLimit.toString());
            }else{
                rateLimiter.trySetRate(RateType.OVERALL, operateApiAccessLimit, 1, RateIntervalUnit.MINUTES);
            }
        } else{
            log.info("default url:{},key:{}",url,key);
            Integer userApiAccessLimit=0;
            String urlRedisLimit = redisTemplate.opsForValue().get(USER_API_LIMIT_KEY_PREFIX + key);
            if(org.apache.commons.lang3.StringUtils.isNotBlank(urlRedisLimit)){
                userApiAccessLimit = Integer.valueOf(urlRedisLimit);
            }
            //普通类接口URL
            if(!userApiAccessLimit.equals(gatewayAccessLimitProperties.getUserApiAccessLimit())){
                //设置每分钟限制次数
                userApiAccessLimit = gatewayAccessLimitProperties.getUserApiAccessLimit();
                rateLimiter.setRate(RateType.OVERALL, userApiAccessLimit, 1, RateIntervalUnit.MINUTES);
                redisTemplate.opsForValue().set(USER_API_LIMIT_KEY_PREFIX + key,userApiAccessLimit.toString());
            }else{
                rateLimiter.trySetRate(RateType.OVERALL, userApiAccessLimit, 1, RateIntervalUnit.MINUTES);
            }
        }
        //尝试获取许可
        boolean isPermitted = rateLimiter.tryAcquire(1);
        if (!isPermitted) {
            //超出访问限制
            throw new BusinessException(ResultCode.SERVER_BUSY,"超出访问限制，请稍后重试");
        }
    }

    /**
     * 匹配操作类URL
     * @param currentUrl
     * @return
     */
    private boolean shouldOperateLimitUrl(String currentUrl) {
        List<String> operateApiAccessLimitUrls = gatewayAccessLimitProperties.getOperateApiAccessLimitUrls();
        if(CollectionUtils.isEmpty(operateApiAccessLimitUrls)){
            return false;
        }
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String url : operateApiAccessLimitUrls) {
            if (pathMatcher.match(url, currentUrl)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 匹配特殊类URL
     * @param currentUrl
     * @return
     */
    private boolean shouldSpecialLimitUrl(String currentUrl) {
        List<String> specialApiAccessLimitUrls = gatewayAccessLimitProperties.getSpecialApiAccessLimitUrls();
        if(CollectionUtils.isEmpty(specialApiAccessLimitUrls)){
            return false;
        }
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String url : specialApiAccessLimitUrls) {
            if (pathMatcher.match(url, currentUrl)) {
                return true;
            }
        }
        return false;
    }

}
