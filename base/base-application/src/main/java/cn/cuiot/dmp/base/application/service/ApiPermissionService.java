package cn.cuiot.dmp.base.application.service;

/**
 * 权限校验服务接口
 *
 * @author wuyongchong
 * @classname ApiPermissionServiceImpl
 * @date 2024/04/25
 */
public interface ApiPermissionService {

    /**
     * 校验用户是否有该权限访问
     * @param permissionCode
     * @param userId
     * @param orgId
     */
    void checkApiPermission(String permissionCode, String userId, String orgId);

}
