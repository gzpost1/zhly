package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Data
public class FormConfigCacheDTO implements Serializable {


    private static final long serialVersionUID = 7047406489693732937L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 表单配置内容
     */
    private String content;

}
