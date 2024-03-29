package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author hk
 * @version 1.0
 * @description: 厂园区楼层查询入参
 * @date 2023/1/12 18:06
 */
@Data
public class FactoryParkFloorQryDto {

    /**
     * 选择空间树节点deptId
     */
    private Long deptId;

    /**
     * 楼层名
     */
    private String floorName;

    /**
     * 分页参数
     */
    @Min(value = 0, message = "每页数据量不能小于0")
    @Max(value = 100, message = "每页最大数据量不能大于100")
    @NotNull(message = "分页参数不能为空")
    private Integer pageSize;

    /**
     * 分页参数
     */
    @NotNull(message = "分页参数不能为空")
    private Integer currentPage;

    private Long orgId;

    private Long userId;

    private Integer group;

    private String deptTreePath;

    private String path;


    /**
     * 标签id
     */
    @JsonIgnore
    private Integer labelId;
}
