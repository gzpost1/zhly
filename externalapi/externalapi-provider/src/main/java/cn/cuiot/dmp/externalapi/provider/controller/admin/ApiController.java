package cn.cuiot.dmp.externalapi.provider.controller.admin;

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.service.park.PlatfromInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 供其他服务调用接口
 *
 * @Author: zc
 * @Date: 2024-09-26
 */
@InternalApi
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private PlatfromInfoService platfromInfoService;

    /**
     * 获取平台短信信息
     *
     * @return PlatfromInfoRespDTO
     * @Param 企业id
     */
    @PostMapping("/queryPlatfromSmsRedis")
    public IdmResDTO<PlatfromInfoRespDTO> queryPlatfromSmsRedis(@RequestBody PlatfromInfoReqDTO dto) {
        return IdmResDTO.success(platfromInfoService.queryPlatfromSmsRedis(dto));
    }

}
