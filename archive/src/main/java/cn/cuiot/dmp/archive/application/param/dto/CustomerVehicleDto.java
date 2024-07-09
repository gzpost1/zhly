package cn.cuiot.dmp.archive.application.param.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author: wuyongchong
 * @date: 2024/6/12 15:18
 */
@Data
public class CustomerVehicleDto implements Serializable {

    /**
     * 主键ID
     */
    private Long id;


    /**
     * 客户ID
     */
    private Long customerId;


    /**
     * 车牌号
     */
    @NotBlank(message = "请完善车辆信息")
    private String vehicleCode;


    /**
     * 车主姓名
     */
    @Length(max = 11, message = "车主姓名限30字")
    private String ownerName;


    /**
     * 手机号
     */
    @Length(max = 11, message = "手机号限11位")
    private String phone;


    /**
     * 证件类型
     */
    private String certificateType;


    /**
     * 证件号码
     */
    @Length(max = 50, message = "证件号码限50字")
    private String certificateCdoe;


    /**
     * 备注
     */
    private String remark;
}
