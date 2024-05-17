package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

/**
 * @Description 撤销配置
 * @Date 2024/4/28 17:02
 * @Created by libo
 */
@Data
public class CancelInfo {
    /**
     * 撤销配置 0i)不允许撤销 1ii)发起人可随时撤销进行中的流程 2iii)发起人可在指定节点之前撤销
     */
    private Byte type;
    /**
     * 撤销节点ID
     */
    private String nodeId;
}
