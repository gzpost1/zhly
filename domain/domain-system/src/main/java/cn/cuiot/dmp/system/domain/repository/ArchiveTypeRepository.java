package cn.cuiot.dmp.system.domain.repository;

import cn.cuiot.dmp.system.domain.aggregate.ArchiveType;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface ArchiveTypeRepository {

    /**
     * 根据条件获取档案类型列表
     */
    List<ArchiveType> queryForList(ArchiveType archiveType);

    /**
     * 根据id获取档案类型详情
     */
    ArchiveType queryForDetail(Long id);

}
