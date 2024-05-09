package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/9
 */
@Data
public class FormConfigDetailQueryDTO implements Serializable {

    private static final long serialVersionUID = 6106559517079472172L;

    /**
     * 表单配置ID
     */
    private Long id;

    /**
     * 表单配置ID列表
     */
    private List<Long> idList;

}
