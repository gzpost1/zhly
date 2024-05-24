package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.dto.ArchiveTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.ArchiveTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.ArchiveTypeVO;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface ArchiveTypeService {

    /**
     * 根据条件获取档案类型列表
     */
    List<ArchiveTypeVO> queryForList(ArchiveTypeQueryDTO queryDTO);

    /**
     * 根据id获取档案类型详情
     */
    ArchiveTypeVO queryForDetail(Long id);

    /**
     * 根据条件获取自定义配置档案类型树
     */
    List<ArchiveTypeTreeNodeVO> queryForTree(ArchiveTypeQueryDTO queryDTO);

}
