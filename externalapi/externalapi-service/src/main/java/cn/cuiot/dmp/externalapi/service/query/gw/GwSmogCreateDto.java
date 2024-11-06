package cn.cuiot.dmp.externalapi.service.query.gw;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
public class GwSmogCreateDto implements Serializable {

    /**
    * 设备名称
    */
    @NotBlank(message = "设备名称不能为空")
    @Length(max = 32,message = "设备名称长度必须小于32位")
    private String name;

    /**
    * 楼盘id
    */
    @NotNull(message = "楼盘id不能为空")
    private Long buildingId;

    /**
    * 设备IMEI号，全局唯一
    */
    @NotBlank(message = "设备IMEI不能为空")
    @Length(max = 100,message = "设备IMEI号，全局唯一长度必须小于100位")
    private String imei;

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





}
