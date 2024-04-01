package cn.cuiot.dmp.gateway.filter;

import cn.cuiot.dmp.common.constant.CacheConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.UserLongTimeLoginEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.gateway.service.SignatureService;
import cn.cuiot.dmp.gateway.utils.JwtUtil;
import cn.cuiot.dmp.gateway.utils.SecrtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
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
 * function:
 *
 * @author wangyh
 * @date 2020/8/4 10:56 上午
 **/
@Component
public class PortalJwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SignatureService signatureService;

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
        List<String> list = headers.get("token");
        if (list == null || list.isEmpty()) {
            list = headers.get("Authorization");
        }

        if (list == null || list.isEmpty()) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }

        String jwt = list.get(0);
        String userName = null;

        if (StringUtils.isEmpty(jwt)) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        } else {
            boolean auth = validateToken(jwt);
            if (!auth) {
                throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
            }
        }
        String userId = getUserId(jwt);
        String orgId = getOrgId(jwt);

        String toKick = redisTemplate.opsForValue().get(CacheConst.LOGIN_ORG_TO_KICK);
        if (!StringUtils.isEmpty(toKick)) {
            String[] kickOrgIdArr = toKick.split(",");
            List<String> kickOrgIds = Arrays.asList(kickOrgIdArr);
            if (kickOrgIds.contains(orgId)) {
                throw new BusinessException(ResultCode.LOGIN_INVALID);
            }
        }

        //判断session 是否失效
        // 增加微信小程序登陆校验
        String jwtRedis = redisTemplate.opsForValue().get(CacheConst.LOGIN_USERS_JWT + jwt);
        String jwtRedisWx = redisTemplate.opsForValue().get(CacheConst.LOGIN_USERS_JWT_WX + jwt);
        if (StringUtils.isEmpty(jwtRedis) && StringUtils.isEmpty(jwtRedisWx)) {
            throw new BusinessException(ResultCode.LOGIN_INVALID);
        } else if (!userId.equals(jwtRedis) && !userId.equals(jwtRedisWx)) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }
        if (!StringUtils.isEmpty(jwtRedis)) {
            if (Objects.equals(redisTemplate.opsForValue()
                            .get(CacheConst.USER_LONG_TIME_LOGIN + userId),
                    UserLongTimeLoginEnum.OPEN.getCode())) {
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

    protected String getOrgId(String jwt) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            claims = null;
        }
        if (claims == null) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }

        String orgId = claims.get("org").toString();
        if (orgId == null) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        return orgId;
    }

    protected String getUserId(String jwt) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            claims = null;
        }
        if (claims == null) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }
        String userId = claims.get("userId").toString();
        if (userId == null) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }
        return userId;
    }

    /**
     * 验证令牌
     */
    public static Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }


    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = JwtUtil.getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

}
