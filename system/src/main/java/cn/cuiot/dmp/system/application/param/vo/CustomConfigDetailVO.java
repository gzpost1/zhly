package cn.cuiot.dmp.system.application.param.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/22
 */
@Data
public class CustomConfigDetailVO implements Serializable {

    private static final long serialVersionUID = 4635395395633796591L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 自定义配置详情名称
     */
    private String name;

}
