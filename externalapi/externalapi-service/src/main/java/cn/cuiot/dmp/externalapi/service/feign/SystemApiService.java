package cn.cuiot.dmp.externalapi.service.feign;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * ApiSystemService实现
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@Slf4j
@Service
public class SystemApiService {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    /**
     * 分页查询外部平台参数信息
     *
     * @return IPage
     * @Param dto 参数
     */
    public IPage<PlatfromInfoRespDTO> queryPlatfromInfoPage(PlatfromInfoReqDTO dto) {
        try {
            IdmResDTO<IPage<PlatfromInfoRespDTO>> idmResDTO = systemApiFeignService.queryPlatfromInfoPage(dto);
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
            log.info("ApiSystemServiceImpl==queryPlatfromInfoPage==fail", ex);
            throw new BusinessException(ResultCode.QUERY_FORM_CONFIG_ERROR);
        }
    }
}
