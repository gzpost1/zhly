package cn.cuiot.dmp.common.controller;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @描述:
 * @Author: sunking
 * @Date: 2020/7/23 11:40 AM
 */
@Slf4j
public class BaseController {

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    protected String getOrgId(){
        String jwt = request.getHeader("token");
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            log.info("jwt:{}",jwt);
            e.printStackTrace();
            claims = null;
        }
        if(claims == null){
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }

        String orgId = claims.get("org").toString();
        if (orgId == null) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        return orgId;
    }

    /**
     * 获取当前登陆人的用户名
     * @return
     */
    protected String getUserName(){
        String jwt = request.getHeader("token");
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            claims = null;
        }
        if(claims == null){
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }

        String userName = claims.get(Claims.SUBJECT).toString();
        if (userName == null) {
            throw new BusinessException(ResultCode.ORG_ID_NOT_EXIST);
        }
        return userName;
    }

    protected String getUserId(){
        return getItemFromToken("userId");
    }

    private String getItemFromToken(String itemName) {
        String jwt = request.getHeader("token");
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            log.error("getItemFromToken error,", e);
            claims = null;
        }
        if(claims == null){
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }
        Object itemId = claims.get(itemName);
        if (itemId == null) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }
        return itemId.toString();
    }

    /**
     * 获取token过期时间
     *
     * @return 10位时间戳
     */
    protected Long getExpiredTime() {
        String jwt = request.getHeader("Authorization");
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(cn.cuiot.dmp.common.utils.Const.SECRET).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            claims = null;
        }
        if (claims == null) {
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }
        return claims.getExpiration().getTime() / 1000;
    }

}
