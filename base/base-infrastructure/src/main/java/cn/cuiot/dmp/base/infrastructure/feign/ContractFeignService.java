package cn.cuiot.dmp.base.infrastructure.feign;//	模板

import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.contract.ContractStatusVo;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomerUseReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomerUserRspDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.base.infrastructure.model.HousesArchivesVo;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 合同Feign服务
 * @author Mujun
 * @Description
 * @data 2024/6/19 15:11
 */
@Component
@FeignClient(value = "community-lease")
public interface ContractFeignService {
    /**
     * 根据房屋id查询关联合同状态信息
     */
    @PostMapping(value = "/api/queryConctactStatusByHouseIds", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<ContractStatusVo> queryConctactStatusByHouseIds(@RequestBody @Valid IdsReq idsReq);

    /**
     * 根据房屋id查询对应的最新定价
     */
    @PostMapping(value = "/api/batchQueryHousePriceForMap", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<Map<Long, Integer>> batchQueryHousePriceForMap(@RequestBody @Valid IdsReq idsReq);

}
