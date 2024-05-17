package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/8
 */
@Data
public class FormConfigTypeRspDTO implements Serializable {

    private static final long serialVersionUID = 2866939089204056842L;

    /**
     * 表单配置类型ID
     */
    private Long formConfigTypeId;

    /**
     * 表单配置类型名称（层级结构）
     */
    private String treeName;

}
