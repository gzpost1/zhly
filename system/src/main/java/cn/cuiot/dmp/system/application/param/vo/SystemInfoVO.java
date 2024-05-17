package cn.cuiot.dmp.system.application.param.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Data
public class SystemInfoVO implements Serializable {

    private static final long serialVersionUID = 7937065951414514912L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统logo
     */
    private String logoUrl;

    /**
     * 来源id（平台或者企业）
     */
    private Long sourceId;

    /**
     * 系统信息类型（0：平台 1：企业）
     */
    private Byte sourceType;

}
