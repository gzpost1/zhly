package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author jz
 * @classname DeptTreeReqDto
 * @description 组织树查询请求dto
 * @date 2023/04/07
 */
@Data
public class DeptTreeReqDto {

    /**
     * 组织id（不传则使用当前用户组织id）
     */
    private String deptId;

    /**
     * 组织名称（模糊查询）
     */
    private String deptName;

    /**
     * 账户类型（5：联通账户，11：物业账户，12：通用账户）
     */
    private List<Integer> orgTypeList;

    /**
     * 组织类型（1：系统，2：租户，3：小区，4：楼栋，5：房屋，6：区域，7：楼层）
     */
    @Range(min = 1, max = 7, message = "组织类型不合法")
    @JsonProperty("dGroup")
    private String dGroup;

    /**
     * 账户标签列表（9：厂园区）
     */
    private List<Integer> orgLabelList;

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
     * 初始查询
     */
    @JsonIgnore
    private Boolean init;

}
