package cn.cuiot.dmp.externalapi.service.vo.gw;

import cn.cuiot.dmp.common.enums.EnumValidator;
import cn.cuiot.dmp.externalapi.service.entity.gw.GwSmogEntity;
import cn.cuiot.dmp.externalapi.service.enums.GwSmogPowerSavingModeEnums;
import cn.cuiot.dmp.externalapi.service.enums.GwSmogSensitivityEnums;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * <p>
 * 格物烟雾报警器
 * </p>
 *
 * @author wuyongchong
 * @since 2024-10-23
 */
@Data
public class GwSmogDetailVo extends GwSmogEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 灵敏度
     * "1": "低灵敏度",
     * "2": "普通",
     * "3": "高灵敏度"
     */
    @EnumValidator(value = GwSmogSensitivityEnums.class)
    private String sensitivity;

    /**
     * 省电模式
     *    "0": "PSM",
     *    "1": "DRX",
     *    "2": "eDRX",
     *   "20": "未开通"
     */
    @EnumValidator(value = GwSmogPowerSavingModeEnums.class)
    private String powerSavingMode;

    /**
     * 温度报警阈值
     *    "minimum": "-1500",
     * 	  "maximum": "7500",
     */
    @Min(value = -1500,message = "最小值为-1500")
    @Max(value = 7500,message = "最大值为7500")
    private Double tempLimit;

    /**
     * 温度报警阈值
     * "minimum": "0",
     * "maximum": "100",
     */
    @Min(value = 0,message = "最小值为0")
    @Max(value = 100,message = "最大值为100")
    private Double dbmLimit;

    /**
     * 烟雾传感器污染度
     * "minimum": "0",
     * "maximum": "100",
     */
    @Min(value = 0,message = "最小值为0")
    @Max(value = 100,message = "最大值为100")
    private Double smokeDirt;

}
