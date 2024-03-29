package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author wqd
 * @classname ParkUpdateReqDto
 * @description
 * @date 2023/1/13
 */
@Data
public class ParkUpdateReqDto {


    @JsonIgnore
    private String orgId;

    @JsonIgnore
    private String userId;

    /**
     * 组织id
     */
    @NotNull(message = "id为空")
    private Long parkId;

    /**
     * 园区名
     */
    @NotBlank(message = "园区名为空")
    @Length(max = 32, message = "园区名称长度不合格")
    private String parkName;

    /**
     * 园区类型
     */
    private String parkType;

    /**
     * 其他园区类型名称
     */
    @Length(max = 64, message = "其他园区类型名称")
    private String otherParkTypeName;


    /**
     * 所属街道社区
     */
    @NotBlank(message = "所属街道社区为空")
    private String areaCode;

    /**
     * 详细地址
     */
    @Length(max = 64, message = "地址超长")
    @NotBlank(message = "详细地址不能为空")
    private String address;

    /**
     * 备注
     */
    @Length(max = 64, message = "备注超长")
    private String description;

}
