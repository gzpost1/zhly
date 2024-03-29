package cn.cuiot.dmp.system.infrastructure.entity.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 商务楼宇房屋、厂园区房屋、商业综合体商铺列表请求dto
 * @author huw51
 */
@Data
public class PropertyHouseListReqDto {

    /**
     * 关键词
     */
    @Length(max = 20, message = "关键词长度不得超过20")
    private String keyword;

    /**
     * 租户id
     */
    @JsonIgnore
    private String orgId;

    /**
     * 用户id
     */
    @JsonIgnore
    private String userId;

    /**
     * 组织Id
     */
    @JsonIgnore
    private Long deptId;

    /**
     * 空间树
     */
    private String spaceTreePath;

    /**
     * 标签Id(9：厂园区)
     */
    private Integer labelId;

    /**
     * 使用状态（2：已租，4：待租）
     */
    private List<Integer> usedStatusList;

    private Integer currentPage = 1;

    private Integer pageSize = 10;
}
