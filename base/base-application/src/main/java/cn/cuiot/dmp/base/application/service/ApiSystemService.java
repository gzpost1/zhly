package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.BaseUserDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BaseUserReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ApiSystemService
 *
 * @author: wuyongchong
 * @date: 2024/4/25 15:52
 */
public interface ApiSystemService {

    /**
     * 查询部门
     */
    List<DepartmentDto> lookUpDepartmentList(DepartmentReqDto query);

    /**
     * 查询用户
     */
    List<BaseUserDto> lookUpUserList(BaseUserReqDto query);

    /**
     * 获取用户信息
     */
    BaseUserDto lookUpUserInfo(@RequestBody BaseUserReqDto query);

    /**
     * 获取部门信息
     */
    DepartmentDto lookUpDepartmentInfo(Long deptId, Long userId, Long orgId);

    /**
     * 查询子部门
     */
    List<DepartmentDto> lookUpDepartmentChildList(DepartmentReqDto query);

    /**
     * 根据业务类型id列表获取业务类型列表（流程/工单配置）
     */
    List<BusinessTypeRspDTO> batchGetBusinessType(BusinessTypeReqDTO businessTypeReqDTO);

    /**
     * 批量查询表单配置
     */
    List<FormConfigRspDTO> batchQueryFormConfig(FormConfigReqDTO formConfigReqDTO);

}
