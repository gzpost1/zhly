package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.ApiBaseConfigService;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.infrastructure.feign.BaseConfigApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 流程
 * @Author: zc
 * @Date: 2024-11-12
 */
@Slf4j
@Service
public class ApiBaseConfigServiceImpl implements ApiBaseConfigService {

    @Autowired
    private BaseConfigApiFeignService baseConfigApiFeignService;

    @Override
    public void syncFlowTaskConfig(SyncCompanyDTO dto) {
        log.info("syncFlowTaskConfig: {}", JsonUtil.writeValueAsString(dto));
        try {
            IdmResDTO<Boolean> idmResDTO = baseConfigApiFeignService.syncFlowTaskConfig(dto);
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
            log.info("ApiBaseConfigServiceImpl==syncFlowTaskConfig==fail", ex);
            throw new BusinessException(ResultCode.ERROR);
        }
    }

    @Override
    public void syncFlowConfig(SyncCompanyDTO dto) {
        log.info("syncFlowConfig: {}", JsonUtil.writeValueAsString(dto));
        try {
            IdmResDTO<Boolean> idmResDTO = baseConfigApiFeignService.syncFlowConfig(dto);
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
            log.info("ApiBaseConfigServiceImpl==syncFlowConfig==fail", ex);
            throw new BusinessException(ResultCode.ERROR);
        }
    }

    @Override
    public void cleanSyncFlowTaskConfigData(SyncCompanyDTO dto) {
        log.info("cleanSyncFlowTaskConfigData: {}", JsonUtil.writeValueAsString(dto));
        try {
            IdmResDTO<?> idmResDTO = baseConfigApiFeignService.cleanSyncFlowTaskConfigData(dto);
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
            log.info("ApiBaseConfigServiceImpl==cleanSyncFlowTaskConfigData==fail", ex);
            throw new BusinessException(ResultCode.ERROR);
        }
    }

    @Override
    public void cleanSyncFlowConfigData(SyncCompanyDTO dto) {
        log.info("cleanSyncFlowConfigData: {}", JsonUtil.writeValueAsString(dto));
        try {
            IdmResDTO<?> idmResDTO = baseConfigApiFeignService.cleanSyncFlowConfigData(dto);
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
            log.info("ApiBaseConfigServiceImpl==cleanSyncFlowConfigData==fail", ex);
            throw new BusinessException(ResultCode.ERROR);
        }
    }
}
