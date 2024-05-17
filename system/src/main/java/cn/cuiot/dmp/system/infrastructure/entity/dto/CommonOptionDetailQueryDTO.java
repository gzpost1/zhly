package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/11
 */
@Data
public class CommonOptionDetailQueryDTO implements Serializable {

    private static final long serialVersionUID = -2217403419376756796L;

    /**
     * 常用选项ID
     */
    private Long id;

    /**
     * 常用选项ID列表
     */
    private List<Long> idList;

}
