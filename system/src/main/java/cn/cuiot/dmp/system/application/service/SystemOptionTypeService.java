package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.dto.SystemOptionTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.vo.SystemOptionTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.SystemOptionTypeVO;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/20
 */
public interface SystemOptionTypeService {

    /**
     * 根据条件获取档案类型列表
     */
    List<SystemOptionTypeVO> queryForList(SystemOptionTypeQueryDTO queryDTO);

    /**
     * 根据id获取档案类型详情
     */
    SystemOptionTypeVO queryForDetail(Long id);

    /**
     * 根据条件获取自定义配置档案类型树
     */
    List<SystemOptionTypeTreeNodeVO> queryForTree(SystemOptionTypeQueryDTO queryDTO);

}
