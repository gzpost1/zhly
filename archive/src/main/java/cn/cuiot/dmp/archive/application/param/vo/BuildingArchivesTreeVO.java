package cn.cuiot.dmp.archive.application.param.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/22
 */
@Data
public class BuildingArchivesTreeVO implements Serializable {

    private static final long serialVersionUID = -1701841534756997180L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private Integer dGroup;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    private Long pkOrgId;

    private Integer sort;

    private Integer level;

    private String path;

    private Integer isInit;

    /**
     * 注册时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 类型
     */
    private String type;

    /**
     * 子集
     */
    private List<BuildingArchivesTreeVO> children;

}
