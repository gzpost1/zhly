package cn.cuiot.dmp.sms.feign;

import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ExternalapiApiFeignService;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 外部系统平台信息
 *
 * @Author: zc
 * @Date: 2024-09-26
 */
@Slf4j
@Service
public class ApiExternalapiService {

    @Autowired
    private ExternalapiApiFeignService externalapiApiFeignService;

    /**
     * 查询对接平台信息
     */
    public List<PlatfromInfoRespDTO> queryForList(PlatfromInfoReqDTO dto) {
        try {
            IdmResDTO<List<PlatfromInfoRespDTO>> idmResDTO = externalapiApiFeignService.queryForList(dto);
            if (Objects.nonNull(idmResDTO) && Objects.equals(ResultCode.SUCCESS.getCode(), idmResDTO.getCode())) {
                return idmResDTO.getData();
            }
            String message = null;
            if (Objects.nonNull(idmResDTO)) {
                message = idmResDTO.getMessage();
            }
            throw new RuntimeException(message);
        } catch (Exception ex) {
            log.info("ApiExternalapiService==queryForList==fail", ex);
            throw new BusinessException(ResultCode.ERROR);
        }
    }
}
