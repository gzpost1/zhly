package cn.cuiot.dmp.archive.application.param.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author: wuyongchong
 * @date: 2024/6/12 15:15
 */
@Data
public class CustomerHouseDto implements Serializable {

    /**
     * 主键ID
     */
    private Long id;


    /**
     * 客户ID
     */
    private Long customerId;


    /**
     * 房屋ID
     */
    @NotNull(message = "房屋ID不能为空")
    private Long houseId;


    /**
     * 身份
     */
    @NotBlank(message = "请完善房屋信息")
    private String identityType;


    /**
     * 迁入日期
     */
    @NotNull(message = "请完善房屋信息")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    private Date moveInDate;


    /**
     * 迁出日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    private Date moveOutDate;

    /**
     * 备注
     */
    @Length(max = 100, message = "备注限100字")
    private String remark;
}
