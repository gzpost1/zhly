package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.application.param.dto.BusinessTypeCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.BusinessTypeUpdateDTO;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeTreeNodeVO;
import cn.cuiot.dmp.system.application.param.vo.BusinessTypeVO;

import java.util.List;

/**
 * @author caorui
 * @date 2024/4/26
 */
public interface BusinessTypeService {

    /**
     * 根据id获取业务类型详情
     */
    BusinessTypeVO queryForDetail(Long id);

    /**
     * 根据条件查询企业的业务类型详情
     */
    List<BusinessTypeTreeNodeVO> queryByCompany(BusinessTypeQueryDTO queryDTO);

    /**
     * 编辑时查询类型列表（排除当前节点）
     */
    List<BusinessTypeTreeNodeVO> queryExcludeChild(BusinessTypeQueryDTO queryDTO);

    /**
     * 保存
     */
    int saveBusinessType(BusinessTypeCreateDTO businessTypeCreateDTO);

    /**
     * 更新
     */
    int updateBusinessType(BusinessTypeUpdateDTO businessTypeUpdateDTO);

    /**
     * 删除预校验
     */
    void checkDeleteStatus(Long id);

    /**
     * 删除
     */
    int deleteBusinessType(BusinessTypeQueryDTO queryDTO);

}
