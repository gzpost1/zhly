package cn.cuiot.dmp.base.infrastructure.feign;

import cn.cuiot.dmp.base.infrastructure.dto.*;
import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;
import cn.cuiot.dmp.common.constant.IdmResDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统管理Feign服务
 *
 * @author: wuyongchong
 * @date: 2024/4/1 20:46
 */
@Component
@FeignClient(value = "community-system")
public interface SystemApiFeignService {

    /**
     * 获取部门信息
     */
    @GetMapping(value = "/api/lookUpDepartmentInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<DepartmentDto> lookUpDepartmentInfo(
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "orgId", required = false) Long orgId);


    /**
     * 获取权限信息
     */
    @GetMapping(value = "/api/lookUpPermission", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<MenuDTO> lookUpPermission(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "orgId", required = false) String orgId,
            @RequestParam(value = "permissionCode", required = false) String permissionCode);

    /**
     * 根据业务类型id列表获取业务类型列表（流程/工单配置）
     */
    @PostMapping(value = "/api/batchGetBusinessType", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<BusinessTypeRspDTO>> batchGetBusinessType(@RequestBody @Valid BusinessTypeReqDTO businessTypeReqDTO);

    /**
     * 批量查询表单配置
     */
    @PostMapping(value = "/api/batchQueryFormConfig", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<FormConfigRspDTO>> batchQueryFormConfig(@RequestBody @Valid FormConfigReqDTO formConfigReqDTO);

}
