package cn.cuiot.dmp.system.application.param.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
public class SystemOptionTypeVO implements Serializable {

    private static final long serialVersionUID = -7575184364510905465L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 系统选项类型名称
     */
    private String name;

    /**
     * 系统选项类型
     */
    private Byte systemOptionType;

}
