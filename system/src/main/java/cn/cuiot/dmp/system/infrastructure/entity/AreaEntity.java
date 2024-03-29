package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @Author wen
 * @Description 行政区域entity
 * @Date 2021/12/27 9:31
 * @param
 * @return
 **/
@Data
public class AreaEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 房号
     */
    private String name;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 描述
     */
    private String parentCode;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者id
     */
    private String createdBy;

    /**
     * 创建者id
     */
    private Integer createdByType;

}
