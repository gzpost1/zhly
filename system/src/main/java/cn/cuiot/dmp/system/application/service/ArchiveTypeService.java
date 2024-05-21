package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.vo.ArchiveTypeVO;
import cn.cuiot.dmp.system.domain.aggregate.ArchiveType;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface ArchiveTypeService {

    /**
     * 根据条件获取档案类型列表
     */
    List<ArchiveTypeVO> queryForList(ArchiveType archiveType);

    /**
     * 根据id获取档案类型详情
     */
    ArchiveTypeVO queryForDetail(Long id);

}
