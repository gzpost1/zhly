package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author wen
 * @Description 行政区域entity
 * @Date 2021/12/27 9:31
 * @param
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysParamEntity {

    /**
     * 主键
     */
    private Long id;

    /**
     * id
     */
    private Long orgId;

    /**
     * title
     */
    private String title;

    /**
     * 等级
     */
    private Integer logoId;

    /**
     * 组织层级树
     */
    private String deptTreePath;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者id
     */
    private String createdBy;

    /**
     * 更新者组织
     */
    private String updaterPath;

    /**
     * LOGO图片地址
     */
    private String logoPath;
}
