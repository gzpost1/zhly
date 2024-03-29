package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author wqd
 * @classname RegionAddReqDto
 * @description
 * @date 2023/1/12
 */
@Data
public class RegionAddReqDto {

    @JsonIgnore
    private String orgId;

    @JsonIgnore
    private String userId;

    /**
     * 组织id
     */
    private Long deptId;

    /**
     * 园区id
     */
    @NotNull(message = "园区为空")
    private Long parkId;

    /**
     * 楼栋名
     */
    @NotBlank(message = "区域名称为空")
    @Length(max = 32, message = "区域名称长度不合格")
    private String regionName;

    /**
     * 区域类型
     */
    @NotBlank(message = "区域类型为空")
    @Length(max = 64, message = "区域类型超长")
    private String regionType;

    /**
     * 其他区域类型名称
     */
    @Length(max = 64, message = "其他区域类型名称超长")
    private String otherRegionTypeName;

    /**
     * 备注
     */
    @Length(max = 64, message = "备注超长")
    private String description;
}
