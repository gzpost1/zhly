package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/8
 */
@Data
public class BatchFormConfigDTO implements Serializable {

    private static final long serialVersionUID = 4822312451337675866L;

    /**
     * 表单配置分类ID
     */
    private Long typeId;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 表单配置ID列表
     */
    @NotEmpty(message = "表单配置ID列表")
    private List<Long> idList;

}
