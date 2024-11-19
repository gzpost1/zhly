package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Data
public class CommonOptionSettingSyncDTO implements Serializable {

    private static final long serialVersionUID = -1145964792883152392L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 常用选项设置名称
     */
    private String name;

    /**
     * 常用选项ID
     */
    private Long commonOptionId;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    private Integer deletedFlag;

    /**
     * 排序
     */
    private Byte sort;

}
