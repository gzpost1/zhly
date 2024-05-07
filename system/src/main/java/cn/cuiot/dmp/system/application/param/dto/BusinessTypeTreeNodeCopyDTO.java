package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.system.application.param.vo.BusinessTypeTreeNodeVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/7
 */
@Data
public class BusinessTypeTreeNodeCopyDTO implements Serializable {

    private static final long serialVersionUID = -7214766992526388466L;

    /**
     * 业务类型树节点
     */
    private List<BusinessTypeTreeNodeVO> businessTypeTreeNodeVOList;

}
