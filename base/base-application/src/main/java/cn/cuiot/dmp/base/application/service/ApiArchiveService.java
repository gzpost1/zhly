package cn.cuiot.dmp.base.application.service;

import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;

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

}
