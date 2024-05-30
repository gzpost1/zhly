package cn.cuiot.dmp.base.infrastructure.feign;

import cn.cuiot.dmp.base.infrastructure.dto.*;
import cn.cuiot.dmp.base.infrastructure.dto.req.*;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.*;
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
//@FeignClient(value = "community-system",url = "http://220.197.15.115:9050/gateway/community-system")
public interface SystemApiFeignService {

    /**
     * 查询角色
     */
    @PostMapping(value = "/api/lookUpRoleList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<BaseRoleDto>> lookUpRoleList(@RequestBody BaseRoleReqDto query);

    /**
     * 查询部门
     */
    @PostMapping(value = "/api/lookUpDepartmentList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<DepartmentDto>> lookUpDepartmentList(@RequestBody DepartmentReqDto query);

    /**
     * 查询用户
     */
    @PostMapping(value = "/api/lookUpUserList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<BaseUserDto>> lookUpUserList(@RequestBody BaseUserReqDto query);

    /**
     * 获取用户信息
     */
    @PostMapping(value = "/api/lookUpUserInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<BaseUserDto> lookUpUserInfo(@RequestBody BaseUserReqDto query);

    /**
     * 获取部门信息
     */
    @GetMapping(value = "/api/lookUpDepartmentInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<DepartmentDto> lookUpDepartmentInfo(
            @RequestParam(value = "deptId", required = false) Long deptId,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "orgId", required = false) Long orgId);

    /**
     * 查询子部门
     */
    @PostMapping(value = "/api/lookUpDepartmentChildList", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<DepartmentDto>> lookUpDepartmentChildList(@RequestBody DepartmentReqDto query);

    /**
     * 查询子部门(可多部门查询)
     */
    @PostMapping(value = "/api/lookUpDepartmentChildList2", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<DepartmentDto>> lookUpDepartmentChildList2(DepartmentReqDto query);

    /**
     * 查询组织树
     */
    @PostMapping(value = "/api/lookUpDepartmentTree", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<DepartmentTreeRspDTO>> lookUpDepartmentTree(@RequestParam(value = "orgId", required = false) Long orgId,
                                                               @RequestParam(value = "userId", required = false) Long userId);

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

    /**
     * 根据id集合批量查询自定义配置详情
     */
    @PostMapping(value = "/api/batchQueryCustomConfigDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<CustomConfigDetailRspDTO>> batchQueryCustomConfigDetails(@RequestBody @Valid CustomConfigDetailReqDTO customConfigDetailReqDTO);

    /**
     * 根据条件批量查询自定义配置列表
     */
    @PostMapping(value = "/api/batchQueryCustomConfigs", produces = MediaType.APPLICATION_JSON_VALUE)
    IdmResDTO<List<CustomConfigRspDTO>> batchQueryCustomConfigs(@RequestBody @Valid CustomConfigReqDTO customConfigReqDTO);

}
