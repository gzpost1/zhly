package cn.cuiot.dmp.base.application.service.impl;

import cn.cuiot.dmp.base.application.service.ApiContentService;
import cn.cuiot.dmp.base.infrastructure.dto.companyinit.SyncCompanyDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ContentApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 小程序配置
 *
 * @Author: zc
 * @Date: 2024-11-12
 */
@Slf4j
@Service
public class ApiContentServiceImpl implements ApiContentService {

    @Autowired
    private ContentApiFeignService contentApiFeignService;

    @Override
    public void syncData(SyncCompanyDTO dto) {
        log.info("syncData: {}", JsonUtil.writeValueAsString(dto));
        try {
            IdmResDTO<?> idmResDTO = contentApiFeignService.syncData(dto);
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
            log.info("ApiContentServiceImpl==syncData==fail", ex);
            throw new BusinessException(ResultCode.NO_OPERATION_PERMISSION);
        }
    }
}
