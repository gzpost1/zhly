package cn.cuiot.dmp.externalapi.service.query.gw.entranceguard;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 格物门禁 DTO
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
public class GwEntranceGuardCreateDto {

    /**
     * 门禁名称
     */
    @NotBlank(message = "门禁名称不能为空")
    private String name;

    /**
     * 楼盘id
     */
    @NotNull(message = "楼盘不能为空")
    private Long buildingId;

    /**
     * 门禁品牌id
     */
    @NotNull(message = "门禁品牌不能为空")
    private Long brandId;

    /**
     * 门禁型号id
     */
    @NotNull(message = "门禁型号不能为空")
    private Long modelId;

    /**
     * 门禁SN
     */
    @NotBlank(message = "门禁SN不能为空")
    @Length(min = 15, max = 15, message ="门禁SN长度必须是15位")
    private String sn;

    /**
     * 通行方向
     */
    @NotNull(message = "通行方向不能为空")
    private Integer direction;

    /**
     * 详细地址
     */
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
    private String remark;
}
