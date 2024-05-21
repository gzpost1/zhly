package cn.cuiot.dmp.archive.domain.repository;

import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchives;
import cn.cuiot.dmp.archive.domain.aggregate.BuildingArchivesPageQuery;
import cn.cuiot.dmp.common.constant.PageResult;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface BuildingArchivesRepository {

    /**
     * 根据id获取楼盘档案详情
     */
    BuildingArchives queryForDetail(Long id);

    /**
     * 根据条件获取楼盘档案详情列表
     */
    List<BuildingArchives> queryForList(BuildingArchivesPageQuery pageQuery);

    /**
     * 根据条件获取楼盘档案详情分页列表
     */
    PageResult<BuildingArchives> queryForPage(BuildingArchivesPageQuery pageQuery);

    /**
     * 保存
     */
    int saveBuildingArchives(BuildingArchives buildingArchives);

    /**
     * 更新
     */
    int updateBuildingArchives(BuildingArchives buildingArchives);

    /**
     * 删除
     */
    int deleteBuildingArchives(Long id);

    /**
     * 批量更新
     */
    int batchUpdateBuildingArchives(Long departmentId);

    /**
     * 批量删除
     */
    int batchDeleteBuildingArchives(List<Long> idList);

}
