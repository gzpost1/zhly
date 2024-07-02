package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.CommonOptionTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.CommonOptionTypeRspDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.CommonOptionTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionTypeVO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.CommonOptionVO;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface CommonOptionTypeService {

    /**
     * 根据id获取业务类型详情
     */
    CommonOptionTypeVO queryForDetail(Long id);

    /**
     * 根据条件查询企业的表单配置类型
     */
    List<CommonOptionTypeTreeNodeVO> queryByCompany(CommonOptionTypeQueryDTO queryDTO);

    /**
     * 查询自定选项树（包含分类/选项名称/选项值）
     */
    List<CommonOptionTypeTreeNodeVO> queryCommonOptionTypeTree(CommonOptionTypeQueryDTO queryDTO);

    /**
     * 编辑时查询类型列表（排除当前节点）
     */
    List<CommonOptionTypeTreeNodeVO> queryExcludeChild(CommonOptionTypeQueryDTO queryDTO);

    /**
     * 保存
     */
    int saveCommonOptionType(CommonOptionTypeCreateDTO commonOptionTypeCreateDTO);

    /**
     * 更新
     */
    int updateCommonOptionType(CommonOptionTypeUpdateDTO commonOptionTypeUpdateDTO);

    /**
     * 删除
     */
    int deleteCommonOptionType(CommonOptionTypeQueryDTO queryDTO);

    /**
     * 根据表单配置类型id列表获取表单配置类型列表（流程/工单配置）
     */
    List<CommonOptionTypeRspDTO> batchGetCommonOptionType(CommonOptionTypeReqDTO commonOptionTypeReqDTO);

    /**
     * 根据表单分类查询表单配置列表
     */
    PageResult<CommonOptionVO> queryCommonOptionByType(CommonOptionPageQuery pageQuery);

}
