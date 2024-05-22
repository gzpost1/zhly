package cn.cuiot.dmp.gateway.filter;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.UserLongTimeLoginEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.gateway.config.AppProperties;
import cn.cuiot.dmp.gateway.service.SignatureService;
import cn.cuiot.dmp.gateway.utils.SecrtUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关认证过滤器
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
    public final static String INNER_TOKEN_NAME="access-token";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private AppProperties appProperties;

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
        if(Boolean.TRUE.equals(appProperties.getEnableGatewayAccessTokenRequest())){
            List<String> accessTokenList = headers.get(INNER_TOKEN_NAME);
            if(CollectionUtils.isNotEmpty(accessTokenList)){
                String headAccessToken = accessTokenList.get(0);
                if (appProperties.getAccessToken().equals(headAccessToken)) {
                    return chain.filter(exchange);
                }
            }
        }
        /**
         * 用户token校验
         */
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
            checkToken(jwt);
        }

        // 签名校验
        boolean needCheckSignature = signatureService.isNeedCheckSignature(exchange);
        if (needCheckSignature) {
            return signatureService.checkSignature(exchange, chain);
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
    private void checkToken(String jwt) {
        try {

            Claims claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();

            String userId = claims.get(USERID).toString();
            String orgId = claims.get(USERORG).toString();

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
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (ex instanceof ExpiredJwtException) {
                throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED, "登录时效已过期");
            } else {
                throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
            }
        }
    }
}
