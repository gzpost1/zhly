package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.BusinessTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.BusinessTypeRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigRspDTO;

import java.util.List;

/**
 * ApiSystemService
 *
 * @author: wuyongchong
 * @date: 2024/4/25 15:52
 */
public interface ApiSystemService {

    /**
     * 获取部门信息
     */
    DepartmentDto lookUpDepartmentInfo(Long deptId, Long userId, Long orgId);

    /**
     * 根据业务类型id列表获取业务类型列表（流程/工单配置）
     */
    List<BusinessTypeRspDTO> batchGetBusinessType(BusinessTypeReqDTO businessTypeReqDTO);

    /**
     * 批量查询表单配置
     */
    List<FormConfigRspDTO> batchQueryFormConfig(FormConfigReqDTO formConfigReqDTO);

}
