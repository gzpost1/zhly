package cn.cuiot.dmp.system.api.controller;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @描述:
 * @Author: sunking
 * @Date: 2020/7/23 11:40 AM
 */
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
            System.out.println(jwt);
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

    protected String getUserId(){
        String jwt = request.getHeader("token");
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        if(claims == null){
            throw new BusinessException(ResultCode.TOKEN_VERIFICATION_FAILED);
        }
        String userId = claims.get("userId").toString();
        if (userId == null) {
            throw new BusinessException(ResultCode.USER_ID_NOT_EXIST);
        }
        return userId;
    }

}
