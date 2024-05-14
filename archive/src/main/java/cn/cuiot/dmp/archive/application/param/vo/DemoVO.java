package cn.cuiot.dmp.archive.application.param.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/5/14
 */
@Data
public class DemoVO implements Serializable {

    private static final long serialVersionUID = -2551786800764240776L;

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
