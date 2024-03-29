package cn.cuiot.dmp.common.service;

/**
 * @author wensq
 * @classname OperateLogService
 * @description 日志保存接口
 * @date 2021/12/3
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
