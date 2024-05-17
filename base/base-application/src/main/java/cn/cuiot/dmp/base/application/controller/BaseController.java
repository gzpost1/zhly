package cn.cuiot.dmp.base.application.controller;

import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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

    /**
     * 获取当前登陆人的企业ID
     *
     * @return
     */
    protected String getOrgId() {
        return Objects.nonNull(LoginInfoHolder.getCurrentOrgId()) ? LoginInfoHolder.getCurrentOrgId().toString() : null;
    }

    /**
     * 获取当前登陆人的用户ID
     *
     * @return
     */
    protected String getUserId() {
        return Objects.nonNull(LoginInfoHolder.getCurrentUserId()) ? LoginInfoHolder.getCurrentUserId().toString() : null;
    }

    /**
     * 获取当前登陆人的用户名
     *
     * @return
     */
    protected String getUserName() {
        return StringUtils.isNoneBlank(LoginInfoHolder.getCurrentName()) ? LoginInfoHolder.getCurrentName() : null;
    }
}
