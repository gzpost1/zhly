package cn.cuiot.dmp.archive.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/24
 */
public interface ArchivesApiMapper {

    /**
     * 根据楼盘档案id统计是否存在关联的房屋/空间/设备/车位档案
     */
    List<Integer> countArchiveByBuildingId(@Param("buildingId") Long buildingId);

    /**
     * 根据档案id和档案类型查询二位码id
     */
    Long getCodeId(@Param("archiveId") Long archiveId, @Param("archiveType") Byte archiveType);

}
