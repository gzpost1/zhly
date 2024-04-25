package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ApiSystemService
 * @author: wuyongchong
 * @date: 2024/4/25 15:52
 */
public interface ApiSystemService {

    /**
     * 获取部门信息
     */
    DepartmentDto lookUpDepartmentInfo(Long deptId,Long userId,Long orgId);

}
