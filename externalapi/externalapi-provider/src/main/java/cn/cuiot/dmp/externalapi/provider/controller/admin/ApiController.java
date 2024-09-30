package cn.cuiot.dmp.externalapi.provider.controller.admin;

import cn.cuiot.dmp.base.application.annotation.InternalApi;
import cn.cuiot.dmp.base.infrastructure.dto.req.PlatfromInfoReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import cn.cuiot.dmp.externalapi.service.service.park.PlatfromInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * 获取平台信息分页
     *
     * @return Page
     * @Param 企业id
     */
    @PostMapping("/queryForPage")
    public IdmResDTO<Page<PlatfromInfoRespDTO>> queryForPage(PlatfromInfoReqDTO dto) {
        return IdmResDTO.success(platfromInfoService.queryForPage(dto));
    }

    /**
     * 获取平台信息列表
     *
     * @return List
     * @Param 企业id
     */
    @PostMapping("/queryForList")
    public IdmResDTO<List<PlatfromInfoRespDTO>> queryForList(PlatfromInfoReqDTO dto) {
        return IdmResDTO.success(platfromInfoService.queryForList(dto));
    }
}
