package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CustomConfigDetailRspDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.dto.CustomConfigCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CustomConfigDTO;
import cn.cuiot.dmp.system.application.param.dto.CustomConfigUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionVO;
import cn.cuiot.dmp.system.application.param.vo.CustomConfigDetailVO;
import cn.cuiot.dmp.system.application.param.vo.CustomConfigVO;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;
import cn.cuiot.dmp.system.domain.aggregate.CustomConfigPageQuery;

import java.util.List;

/**
 * @author caorui
 * @date 2024/5/19
 */
public interface CustomConfigService {

    /**
     * 根据id获取详情
     */
    CustomConfigVO queryForDetail(Long id);

    /**
     * 根据名称获取详情
     */
    CustomConfigVO queryForDetailByName(CustomConfigDTO customConfigDTO);

    /**
     * 保存
     */
    int saveCustomConfig(CustomConfigCreateDTO createDTO);

    /**
     * 更新
     */
    int updateCustomConfig(CustomConfigUpdateDTO updateDTO);

    /**
     * 更新状态
     */
    int updateCustomConfigStatus(UpdateStatusParam updateStatusParam);

    /**
     * 删除
     */
    int deleteCustomConfig(Long id);

    /**
     * 根据档案类型查询自定义配置列表
     */
    PageResult<CustomConfigVO> queryCustomConfigByType(CustomConfigPageQuery pageQuery);

    /**
     * 根据id集合批量查询自定义配置详情
     */
    List<CustomConfigDetailRspDTO> batchQueryCustomConfigDetails(CustomConfigDetailReqDTO customConfigDetailReqDTO);
    
}
