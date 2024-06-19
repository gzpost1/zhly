package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.CustomerUseReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.req.DepartmentReqDto;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomerUserRspDto;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;

import java.util.List;

/**
 * 档案信息查询
 *
 * @author: wuyongchong
 * @date: 2024/6/3 17:43
 */
public interface ApiArchiveService {

    /**
     * 根据ID获取楼盘信息
     */
    BuildingArchive lookupBuildingArchiveInfo(Long id);

    /**
     * 查询当前组织及下级组织下的楼盘列表
     */
    List<BuildingArchive> lookupBuildingArchiveByDepartmentList(DepartmentReqDto reqDto);

    /**
     * 查询客户
     */
    List<CustomerUserRspDto> lookupCustomerUsers(CustomerUseReqDto reqDto);

}
