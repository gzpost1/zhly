package cn.cuiot.dmp.base.application.interceptor;

import cn.cuiot.dmp.base.application.annotation.RequiresPermissions;
import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.base.application.service.ApiArchiveService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.utils.Const;
import cn.cuiot.dmp.domain.types.AuthContants;
import cn.cuiot.dmp.domain.types.LoginInfo;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author: wuyongchong
 * @date: 2024/4/25 11:49
 */
@Slf4j
public class BaseAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiArchiveService apiArchiveService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        String community = request.getHeader(AuthContants.COMMUNITY);

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
                loginInfo.setUserType(claims.get(AuthContants.USER_TYPE) != null ? Integer.valueOf(claims.get(AuthContants.USER_TYPE).toString()) : null);

                //针对业主,从头部获取小区ID
                loginInfo.setCommunityId(NumberUtil.parseLong(community,null));
                //针对业主，填充扩展信息
                if ((handler instanceof HandlerMethod)&&Objects.nonNull(loginInfo.getCommunityId())) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    ResolveExtData resolveExtData = handlerMethod.getBeanType()
                            .getAnnotation(ResolveExtData.class);
                    if (resolveExtData == null) {
                        resolveExtData = handlerMethod.getMethodAnnotation(ResolveExtData.class);
                    }
                    if(Objects.nonNull(resolveExtData)){
                        BuildingArchive buildingArchive = apiArchiveService
                                .lookupBuildingArchiveInfo(loginInfo.getCommunityId());
                        if(Objects.nonNull(buildingArchive)){
                            loginInfo.setOrgId(buildingArchive.getCompanyId());
                            loginInfo.setDeptId(buildingArchive.getDepartmentId());
                        }
                    }
                }
                //设置上下文信息
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
