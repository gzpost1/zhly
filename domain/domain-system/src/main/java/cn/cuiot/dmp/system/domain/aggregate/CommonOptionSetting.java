package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Data
public class CommonOptionSetting implements Serializable {

    private static final long serialVersionUID = 1376172797099531443L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 常用选项设置名称
     */
    private String name;

}
