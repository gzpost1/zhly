package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.constant.RegexConst;
import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * @author huw51
 */
@Data
public class PropertyHouseAddReqDto extends AbstractResourceParam {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 组织id
     */
    @NotNull(message = "组织id为空")
    private Long deptId;

    /**
     * 小区id
     */
    @NotNull(message = "园区id为空")
    private Long parkId;

    /**
     * 楼栋id
     */
    @NotNull(message = "楼座id为空")
    private Long buildingId;

    /**
     * 楼层id
     */
    @NotNull(message = "楼层id为空")
    private Long floorId;

    /**
     * 区域id
     */
    private Long regionId;

    /**
     * 房屋名称
     */
    @NotBlank(message = "房屋名称为空")
    private String houseName;

    /**
     * 房号
     */
    @NotBlank(message = "房号为空")
    private String houseNum;

    /**
     * 房屋属性 0 自住 1出租
     */
    @NotNull(message = "房屋属性为空")
    private String houseAttribute;

    /**
     * 房屋面积
     */
    @NotBlank(message = "房屋面积为空")
    @Pattern(regexp = RegexConst.NEW_HOUSE_AREA, message = "房屋面积只支持数字")
    private String houseArea;

    /**
     * 使用面积
     */
   /** @Pattern(regexp = RegexConst.NEW_HOUSE_AREA, message = "使用面积只支持数字")*/
    private String usedArea;

    /**
     * 公摊面积
     */
   /** @Pattern(regexp = RegexConst.NEW_HOUSE_AREA, message = "公摊面积只支持数字")*/
    private String publicArea;


    /**
     * 描述
     */
    @Length(max = 200, message = "房屋描述不可超过200字")
    private String description;

    /**
     * 当前登录人orgId
     */
    private Long pkOrgId;

    /**
     * 当前登录人userId
     */
    private String LoginUserId;

    /**
     * 房屋使用状态
     */
    @Range(min = 1, max = 7, message = "房屋使用状态不合法")
    @NotNull(message = "使用状态为空")
    private Integer usedStatus;
}
