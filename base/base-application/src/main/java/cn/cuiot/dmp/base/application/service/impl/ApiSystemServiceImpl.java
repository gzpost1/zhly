package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.ApiSystemService;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ApiSystemService实现
 *
 * @author: wuyongchong
 * @date: 2024/4/25 15:53
 */
@Slf4j
@Service
public class ApiSystemServiceImpl implements ApiSystemService {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Override
    public DepartmentDto lookUpDepartmentInfo(Long deptId, Long userId, Long orgId) {
        try {
            IdmResDTO<DepartmentDto> idmResDTO = systemApiFeignService
                    .lookUpDepartmentInfo(deptId, userId, orgId);
            if (Objects.nonNull(idmResDTO) && ResultCode.SUCCESS.getCode()
                    .equals(idmResDTO.getCode())) {
                return idmResDTO.getData();
            }
            String message = null;
            if (Objects.nonNull(idmResDTO)) {
                message = idmResDTO.getMessage();
            }
            throw new RuntimeException(message);
        } catch (Exception ex) {
            log.info("ApiSystemServiceImpl==lookUpDepartmentInfo==fail", ex);
            throw new BusinessException(ResultCode.QUERY_USER_DEPT_ERROR);
        }
    }

}
