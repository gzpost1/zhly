package cn.cuiot.dmp.base.infrastructure.feign;//	模板

import cn.cuiot.dmp.base.infrastructure.domain.pojo.BuildingArchiveReq;
import cn.cuiot.dmp.base.infrastructure.domain.pojo.IdsReq;
import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
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

/**
 * 档案Feign服务
 * @author hantingyao
 * @Description
 * @data 2024/5/30 17:11
 */
@Component
@FeignClient(value = "community-archive")
public interface ArchiveFeignService {
    /**
     * 查询楼盘信息
     */
    @PostMapping(value = "/api/buildingArchiveQueryForList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<BuildingArchive>> buildingArchiveQueryForList(@RequestBody @Valid BuildingArchiveReq buildingArchiveReq);

    /**
     * 根据ID获取楼盘信息
     */
    @PostMapping(value = "/api/lookupBuildingArchiveInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<BuildingArchive> lookupBuildingArchiveInfo(@RequestBody @Valid IdParam idParam);
    /**
     * 根据ID获取房屋
     */
    @PostMapping(value = "/api/queryHousesList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<HousesArchivesVo>> queryHousesList(@RequestBody @Valid IdsReq ids);

    /**
     * 查询当前组织及下级组织下的楼盘列表
     */
    @PostMapping(value = "/api/lookupBuildingArchiveByDepartmentList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<BuildingArchive>> lookupBuildingArchiveByDepartmentList(@RequestBody @Valid DepartmentReqDto reqDto);

    /**
     * 查询客户
     */
    @PostMapping(value = "/api/lookupCustomerUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<CustomerUserRspDto>> lookupCustomerUsers(@RequestBody @Valid CustomerUseReqDto reqDto);
}
