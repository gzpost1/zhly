package cn.cuiot.dmp.system.application.param.dto;

import cn.cuiot.dmp.system.application.param.vo.CommonOptionTypeTreeNodeVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
public class CommonOptionTypeTreeNodeCopyDTO implements Serializable {

    private static final long serialVersionUID = 5566605781380688025L;

    /**
     * 常用选项树节点
     */
    private List<CommonOptionTypeTreeNodeVO> commonOptionTypeTreeNodeVOList;

}
