package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.dto.DeptTreeReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.DeptTreeResDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SpaceTreeReqDto;
import cn.cuiot.dmp.system.infrastructure.entity.dto.SpaceTreeResDto;
import java.util.List;

/**
 * @author hk
 * @classname TreeService
 * @description 树管理service
 * @date 2023/04/07
 */
public interface TreeService {

    /**
     * 组织树查询
     *
     * @param deptTreeReqDto 组织树查询请求dto
     * @return 组织树列表
     */
    List<DeptTreeResDto> getDeptTree(DeptTreeReqDto deptTreeReqDto);

    /**
     * 空间树查询
     *
     * @param spaceTreeReqDto 空间树查询请求dto
     * @return 空间树列表
     */
    List<SpaceTreeResDto> getSpaceTree(SpaceTreeReqDto spaceTreeReqDto);

}
