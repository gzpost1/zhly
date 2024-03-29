package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


/**
 * @author wensq
 * @version 1.0
 * @description: 小区查询入参
 * @date 2022/9/14 18:06
 */
@Data
public class BuildingListQryDto {

    /**
     * 选择空间树节点deptId
     */
    private Long deptId;

    /**
     * 楼栋名
     */
    private String key;

    /**
     * 分页参数
     */
    private Integer currentPage = 1;

    /**
     * 分页参数
     */
    private Integer pageSize = 10;

    private Long orgId;

    private Long userId;

    private Integer group;

    private String path;

    /**
     * 标签id
     */
    @JsonIgnore
    private Integer labelId;
}
