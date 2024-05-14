package cn.cuiot.dmp.archive.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Data
public class DemoEntity extends BaseEntity {

    private static final long serialVersionUID = 6931804219738965543L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 状态，0-禁用，1-启用
     */
    private Byte status;

}
