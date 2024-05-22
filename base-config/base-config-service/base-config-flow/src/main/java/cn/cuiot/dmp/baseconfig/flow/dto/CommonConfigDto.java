package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 流程配置-通用配置
 * @Date 2024/4/23 16:04
 * @Created by libo
 */
@Data
public class CommonConfigDto {
    /**
     * 撤销规则-撤销类型 0不允许撤销 1发起人可随时撤销进行中的流程  2发起人可在指定节点之前撤销
     */
    @NotNull(message = "撤销规则-撤销类型不能为空")
    private Byte revokeType;

    /**
     * 撤销规则-撤销节点ID
     */
    private String revokeNodeId;
}
