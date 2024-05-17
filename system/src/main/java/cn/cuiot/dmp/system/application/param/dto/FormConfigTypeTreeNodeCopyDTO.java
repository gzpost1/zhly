package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/8
 */
@Data
public class FormConfigTypeTreeNodeCopyDTO implements Serializable {

    private static final long serialVersionUID = -6521449545270449460L;

    /**
     * 表单配置树节点
     */
    private List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList;

}
