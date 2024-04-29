package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/4/29 15:44
 */
@Data
public class MenuQuery implements Serializable {

    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 状态
     */
    private Integer status;
}
