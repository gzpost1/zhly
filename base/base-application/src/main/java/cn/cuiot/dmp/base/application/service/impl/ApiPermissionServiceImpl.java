package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.config.AppProperties;
import cn.cuiot.dmp.base.application.service.ApiPermissionService;
import cn.cuiot.dmp.base.infrastructure.dto.MenuDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限校验服务
 *
 * @author wuyongchong
 * @classname ApiPermissionServiceImpl
 * @date 2024/04/25
 */
@Slf4j
@Service
public class ApiPermissionServiceImpl implements ApiPermissionService {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private AppProperties appProperties;

    @Override
    public void checkApiPermission(String permissionCode, String userId, String orgId) {
        log.info("checkApiPermission, permissionCode:{}, userId: {}, orgId: {}", permissionCode,
                userId, orgId);
        if(Boolean.TRUE.equals(appProperties.getEnablePermission())){
            try {
                IdmResDTO<MenuDTO> idmResDTO = systemApiFeignService
                        .lookUpPermission(userId, orgId, permissionCode);
                if (Objects.nonNull(idmResDTO) && ResultCode.SUCCESS.getCode()
                        .equals(idmResDTO.getCode())) {
                    return;
                }
                String message = null;
                if (Objects.nonNull(idmResDTO)) {
                    message = idmResDTO.getMessage();
                }
                throw new RuntimeException(message);
            } catch (Exception ex) {
                log.info("ApiPermissionServiceImpl==checkApiPermission==fail", ex);
                throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
            }
        }
    }

}
