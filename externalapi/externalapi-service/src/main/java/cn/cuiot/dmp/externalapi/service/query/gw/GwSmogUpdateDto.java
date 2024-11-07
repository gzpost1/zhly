package cn.cuiot.dmp.externalapi.service.query.gw;

import cn.cuiot.dmp.common.enums.EnumValidator;
import cn.cuiot.dmp.externalapi.service.enums.GwSmogPowerSavingModeEnums;
import cn.cuiot.dmp.externalapi.service.enums.GwSmogSensitivityEnums;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
*
* @author wuyongchong
* @since 2024-10-23
*/
@Getter
@Setter
public class GwSmogUpdateDto implements Serializable {
    /**
    *  id
    */
    @NotNull(message = "id不能为空")
    private Long id;

    private static final long serialVersionUID = 1L;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    @Length(max = 30,message = "设备名称长度必须小于30位")
    private String name;

    /**
     * 楼盘id
     */
    @NotNull(message = "楼盘id不能为空")
    private Long buildingId;


    /**
     * 详细地址
     */
    @Length(max = 255,message = "详细地址长度必须小于255位")
    private String address;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 备注
     */
    @Length(max = 255,message = "备注长度必须小于255位")
    private String remark;

    /**
     * 灵敏度
     * "1": "低灵敏度",
     * "2": "普通",
     * "3": "高灵敏度"
     */
    @NotBlank(message = "灵敏度不能为空")
    @EnumValidator(value = GwSmogSensitivityEnums.class)
    private String sensitivity;

    /**
     * 省电模式
     *    "0": "PSM",
     *    "1": "DRX",
     *    "2": "eDRX",
     *   "20": "未开通"
     */
    @NotBlank(message = "省电模式不能为空")
    @EnumValidator(value = GwSmogPowerSavingModeEnums.class)
    private String powerSavingMode;

    /**
     * 温度报警阈值
     *    "minimum": "-1500",
     * 	  "maximum": "7500",
     */
    @NotNull(message = "温度报警阈值不能为空")
    @Min(value = -1500,message = "最小值为-1500")
    @Max(value = 7500,message = "最大值为7500")
    private Double tempLimit;

    /**
     * 温度报警阈值
     * "minimum": "0",
     * "maximum": "100",
     */
    @NotNull(message = "温度报警阈值不能为空")
    @Min(value = 0,message = "最小值为0")
    @Max(value = 100,message = "最大值为100")
    private Double dbmLimit;

    /**
     * 烟雾传感器污染度
     * "minimum": "0",
     * "maximum": "100",
     */
    @NotNull(message = "烟雾传感器污染度不能为空")
    @Min(value = 0,message = "最小值为0")
    @Max(value = 100,message = "最大值为100")
    private Double smokeDirt;


}
