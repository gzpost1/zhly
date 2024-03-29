package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @param
 * @Author xieSH
 * @Description 组织实体
 * @Date 2021/8/17 9:32
 * @return
 **/
@Data
public class DepartmentDto {

    /**
     * 组织id
     */
    private Long id;

    /**
     * 组织名
     */
    private String name;

    /**
     * 组织编码
     */
    private String code;

    /**
     * 组织类型
     */
    private String dGroup;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 排序
     */
    private Long orgId;

    /**
     * 路径
     */
    private String path;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;
    /**
     * 描述
     */
    private String description;

    /**
     * 创建者
     */
    private String createdBy;
}
