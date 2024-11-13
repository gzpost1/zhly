package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.ApiLeaseService;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.infrastructure.feign.LeaseApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 收费管理
 *
 * @Author: zc
 * @Date: 2024-11-12
 */
@Slf4j
@Service
public class ApiLeaseServiceImpl implements ApiLeaseService {

    @Autowired
    private LeaseApiFeignService leaseApiFeignService;

    @Override
    public void syncChargeStandard(SyncCompanyDTO dto) {
        log.info("syncChargeStandard: {}", JsonUtil.writeValueAsString(dto));
        try {
            IdmResDTO<?> idmResDTO = leaseApiFeignService.syncChargeStandard(dto);
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
            log.info("ApiLeaseServiceImpl==syncChargeStandard==fail", ex);
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
    }
}
