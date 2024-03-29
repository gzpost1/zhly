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
public class PropertyHouseUpdateReqDto extends AbstractResourceParam {

    /**
     * 主键id
     */
    @NotNull(message = "主键id为空")
    private Long id;

    /**
     * 房屋名称
     */
    @NotNull(message = "房屋名称为空")
    private String houseName;

    /**
     * 房号
     */
    @NotBlank(message = "房号为空")
    private String houseNum;

    /**
     * 房屋使用状态
     */
    @Range(min = 1, max = 7, message = "房屋使用状态不合法")
    private Integer usedStatus;

    /**
     * 房屋面积
     */
    @NotBlank(message = "建筑面积为空")
    @Pattern(regexp = RegexConst.NEW_HOUSE_AREA, message = "建筑面积只支持数字")
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
     * 房屋属性
     */
    private String houseAttribute;

    /**
     * 当前登录人userId
     */
    private String LoginUserId;

    /**
     * 企业id
     */
    private String orgId;
}
