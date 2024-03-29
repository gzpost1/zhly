package cn.cuiot.dmp.gateway.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.Const;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author wangyh
 */
public class JwtUtil {

    private JwtUtil() {
        throw new BusinessException(ResultCode.CANNOT_CREATE_CONST_CLASS_OBJECT);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(Const.SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}
