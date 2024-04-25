package cn.cuiot.dmp.base.infrastructure.feign;

import cn.cuiot.dmp.base.infrastructure.dto.DepartmentDto;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadParam;
import cn.cuiot.dmp.base.infrastructure.dto.FileUploadResponse;
import cn.cuiot.dmp.common.constant.IdmResDTO;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


}
