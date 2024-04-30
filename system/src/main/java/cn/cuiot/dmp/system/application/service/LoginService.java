package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginReqDTO;
import cn.cuiot.dmp.system.infrastructure.entity.dto.LoginResDTO;
import cn.cuiot.dmp.system.domain.entity.User;
import javax.servlet.http.HttpServletRequest;

/**
 * @param
 * @Author xieSH
 * @Description 登录业务接口
 * @Date 2021/8/6 10:08
 * @return
 **/
public interface LoginService {

    /**
     * Dmp认证，返回是否成功
     * @param loginReqDTO 用户信息
     * @return
     */
    User authDmp(LoginReqDTO loginReqDTO);

    /**
     * 用户登录
     * @param userDataEntity
     * @return
     */
    LoginResDTO loginIdentity(User userDataEntity, HttpServletRequest request);

    /**
     * 用户登出
     * @param request
     */
    void logoutIdentity(HttpServletRequest request, String orgId, String userId, String userName);

    /**
     * 模拟登录
     * @param username
     * @param phoneNumber
     * @return
     */
    LoginResDTO simulateLogin(String username, String phoneNumber, HttpServletRequest request);
}
