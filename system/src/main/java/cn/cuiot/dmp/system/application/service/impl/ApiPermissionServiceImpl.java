package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.service.ApiPermissionService;
import cn.cuiot.dmp.domain.types.id.UserId;
import cn.cuiot.dmp.system.infrastructure.entity.MenuEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.MenuDao;
import cn.cuiot.dmp.system.user_manage.domain.entity.User;
import cn.cuiot.dmp.system.user_manage.domain.types.enums.UserTypeEnum;
import cn.cuiot.dmp.system.user_manage.repository.UserRepository;
import javax.annotation.Resource;
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

    @Resource
    private MenuDao menuDao;
    @Resource
    private UserRepository userRepository;

    @Override
    public void checkApiPermission(String permissionCode, String userId, String orgId) {
        log.info("checkApiPermission, permissionCode:{}, userId: {}, orgId: {}", permissionCode, userId,orgId);
        User user = userRepository.find(new UserId(userId));
        if (UserTypeEnum.USER.equals(user.getUserType())) {
            MenuEntity menuEntity = menuDao.getChangeOrgMenu(userId, orgId, permissionCode);
            if (menuEntity == null) {
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }
    }

}
