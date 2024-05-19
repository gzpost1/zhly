package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/17
 */
@Data
public class CustomConfigDetail implements Serializable {

    private static final long serialVersionUID = 6336854047332406914L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 自定义配置详情名称
     */
    private String name;

}
