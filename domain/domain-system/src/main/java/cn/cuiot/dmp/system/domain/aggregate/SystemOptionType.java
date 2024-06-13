package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/19
 */
@Data
public class SystemOptionType implements Serializable {

    private static final long serialVersionUID = -5314884308043982573L;

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
