package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.ApiPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wensq
 * @classname AsynchronousServiceImpl
 * @description
 * @date 2021/12/21
 */
@Slf4j
@Service
public class ApiPermissionServiceImpl implements ApiPermissionService {

    @Override
    public void checkApiPermission(String permissionCode, String userId, String orgId) {
        log.info("checkApiPermission, permissionCode:{}, userId: {}, orgId: {}", permissionCode,
                userId, orgId);
        /*User user = userRepository.find(new UserId(userId));
        if (UserTypeEnum.USER.equals(user.getUserType())) {
            MenuEntity menuEntity = menuDao.getChangeOrgMenu(userId, orgId, permissionCode);
            if (menuEntity == null) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }*/
    }

}
