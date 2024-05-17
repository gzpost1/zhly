package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
public class CommonOptionTypeRspDTO implements Serializable {

    private static final long serialVersionUID = -4973637125730233539L;

    /**
     * 常用选项类型ID
     */
    private Long commonOptionTypeId;

    /**
     * 常用选项类型名称（层级结构）
     */
    private String treeName;

}
