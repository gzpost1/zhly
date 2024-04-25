package cn.cuiot.dmp.baseconfig.flow.dto.flowjson;

import lombok.Data;

/**
 * @Description 节点按钮
 * @Date 2024/4/23 20:22
 * @Created by libo
 */
@Data
public class NodeButton {
    /**
     * 按钮类型
     */
    private Byte buttonType;

    /**
     * 按钮名称
     */
    private String buttonName;

    /**
     * 是否启用 0否 1是
     */
    private Byte status;
}
