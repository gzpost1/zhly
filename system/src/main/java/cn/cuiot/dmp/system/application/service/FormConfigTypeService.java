package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.FormConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.FormConfigTypeRspDTO;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.FormConfigTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeVO;
import cn.cuiot.dmp.system.application.param.vo.FormConfigVO;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface FormConfigTypeService {

    /**
     * 根据id获取表单配置类型详情
     */
    FormConfigTypeVO queryForDetail(Long id);

    /**
     * 根据条件获取表单配置类型列表
     */
    List<FormConfigTypeVO> queryForList(FormConfigTypeQueryDTO queryDTO);

    /**
     * 根据条件查询企业的表单配置类型
     */
    List<FormConfigTypeTreeNodeVO> queryByCompany(FormConfigTypeQueryDTO queryDTO);

    /**
     * 编辑时查询类型列表（排除当前节点）
     */
    List<FormConfigTypeTreeNodeVO> queryExcludeChild(FormConfigTypeQueryDTO queryDTO);

    /**
     * 保存
     */
    int saveFormConfigType(FormConfigTypeCreateDTO formConfigTypeCreateDTO);

    /**
     * 更新
     */
    int updateFormConfigType(FormConfigTypeUpdateDTO formConfigTypeUpdateDTO);

    /**
     * 删除
     */
    int deleteFormConfigType(FormConfigTypeQueryDTO queryDTO);

    /**
     * 根据表单配置类型id列表获取表单配置类型列表（流程/工单配置）
     */
    List<FormConfigTypeRspDTO> batchGetFormConfigType(FormConfigTypeReqDTO formConfigTypeReqDTO);

    /**
     * 根据表单分类查询表单配置列表
     */
    PageResult<FormConfigVO> queryFormConfigByType(FormConfigPageQuery pageQuery);

}
