package cn.cuiot.dmp.sms.feign;

import cn.cuiot.dmp.base.infrastructure.constants.PlatfromInfoRedisKeyConstants;
import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ExternalapiApiFeignService;
import cn.cuiot.dmp.base.infrastructure.utils.RedisUtil;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询楼房选项
     */
    public PlatfromInfoRespDTO queryPlatfromSmsRedis(PlatfromInfoReqDTO dto) {
        try {

            String jsonStr = redisUtil.get(PlatfromInfoRedisKeyConstants.PLATFROM_INFO_SMS + dto.getCompanyId());
            if (StringUtils.isNotBlank(jsonStr)) {
                return JsonUtil.readValue(jsonStr, PlatfromInfoRespDTO.class);
            }

            IdmResDTO<PlatfromInfoRespDTO> idmResDTO = externalapiApiFeignService.queryPlatfromSmsRedis(dto);
            if (Objects.nonNull(idmResDTO) && Objects.equals(ResultCode.SUCCESS.getCode(), idmResDTO.getCode())) {
                return idmResDTO.getData();
            }
            String message = null;
            if (Objects.nonNull(idmResDTO)) {
                message = idmResDTO.getMessage();
            }
            throw new RuntimeException(message);
        } catch (Exception ex) {
            log.info("ApiExternalapiService==queryPlatfromSmsRedis==fail", ex);
            throw new BusinessException(ResultCode.ERROR);
        }
    }
}
