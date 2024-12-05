package cn.cuiot.dmp.archive.infrastructure.persistence.mapper;

import cn.cuiot.dmp.archive.infrastructure.entity.BuildingArchivesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface BuildingArchivesMapper extends BaseMapper<BuildingArchivesEntity> {

    /**
     * 批量更新
     *
     * @param departmentId 部门id
     * @param idList       楼盘档案id列表
     */
    int batchUpdateBuildingArchives(@Param("departmentId") Long departmentId, @Param("idList") List<Long> idList);

    int batchUpdateBuildingArchivesType(@Param("type") String type, @Param("idList") List<Long> idList);

    /**
     * 批量保存楼盘档案列表
     *
     * @param buildingArchivesEntityList 楼盘档案列表
     */
    int batchSaveBuildingArchives(@Param("list") List<BuildingArchivesEntity> buildingArchivesEntityList);

    Long quertOrgIdByHouse(@Param("houseId") Long houseId);

}
