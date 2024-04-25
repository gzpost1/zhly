package cn.cuiot.dmp.base.application.interceptor;

import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.domain.types.AuthContants;
import cn.cuiot.dmp.domain.types.LoginInfo;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author: wuyongchong
 * @date: 2024/4/25 11:49
 */
@Slf4j
public class BaseAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        String jwt = request.getHeader(AuthContants.TOKEN);
        if(StringUtils.isBlank(jwt)){
            jwt = request.getHeader(AuthContants.AUTHORIZATION);
        }
        if (StringUtils.isNotBlank(jwt)) {
            try {
                Claims claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.setUserId(claims.get(AuthContants.USERID) != null ? Long.valueOf(claims.get(AuthContants.USERID).toString()) : null);
                loginInfo.setUsername(claims.get(AuthContants.USERNAME) != null ? claims.get(AuthContants.USERID).toString() : null);
                loginInfo.setPhoneNumber(claims.get(AuthContants.USER_PHONE) != null ? claims.get(AuthContants.USER_PHONE).toString() : null);
                loginInfo.setName(claims.get(AuthContants.NAME) != null ? claims.get(AuthContants.NAME).toString() : null);
                loginInfo.setOrgId(claims.get(AuthContants.USERORG) != null ? Long.valueOf(claims.get(AuthContants.USERORG).toString()) : null);
                loginInfo.setOrgTypeId(claims.get(AuthContants.USERORG_TYPE_ID) != null ? Integer.valueOf(claims.get(AuthContants.USERORG_TYPE_ID).toString()) : null);
                loginInfo.setDeptId(claims.get(AuthContants.DEPT_ID) != null ? Long.valueOf(claims.get(AuthContants.DEPT_ID).toString()) : null);
                loginInfo.setPostId(claims.get(AuthContants.POST_ID) != null ? Long.valueOf(claims.get(AuthContants.POST_ID).toString()) : null);
                LoginInfoHolder.setLocalLoginInfo(loginInfo);
            } catch (Exception e) {
                log.warn("BaseAuthInterceptor parse token error",e);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        LoginInfoHolder.clearLoginInfo();
    }
}
