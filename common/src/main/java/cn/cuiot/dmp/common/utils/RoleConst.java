package cn.cuiot.dmp.common.utils;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;

/**
 * @Description 角色相关常量
 * @Author cds
 * @Date 2020/9/24
 */
public class RoleConst {

    private RoleConst() {
        throw new BusinessException(ResultCode.CANNOT_CREATE_CONST_CLASS_OBJECT);
    }

    /**
     * 超级管理员角色模板主键
     */
    public static final Long DEFAULT_SUPER_ADMIN_ROLE_PK = 1L;

    /**
     * 物联网（系统高级）管理员角色模板主键
     */
    public static final Long DEFAULT_SUPER_OPERATOR_ROLE_PK = 2L;

    /**
     * 物业管理员
     */
    public static final Long DEFAULT_COMMUNITY_ADMIN = 4L;

    /**
     * 通用管理员
     */
    public static final Long DEFAULT_COMMON_ADMIN = 5L;

    /**
     * 厂园区管理员
     */
    public static final Long DEFAULT_FACTORY_PARK_ADMIN = 2779L;

}
