package cn.cuiot.dmp.lease.dto.price;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
public class PriceManageCreateDTO implements Serializable {

    private static final long serialVersionUID = 7489087479581726859L;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 定价单名称
     */
    @NotBlank(message = "定价单名称不能为空")
    private String name;

    /**
     * 定价单类别（系统配置自定义）
     */
    @NotNull(message = "定价单类别不能为空")
    private Long categoryId;

    /**
     * 定价单类型（系统配置自定义）
     */
    @NotNull(message = "定价单类型不能为空")
    private Long typeId;

    /**
     * 定价日期，格式为yyyy-MM-dd
     */
    @NotNull(message = "定价日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date priceDate;

    /**
     * 执行日期，格式为yyyy-MM-dd
     */
    @NotNull(message = "执行日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date executeDate;

    /**
     * 定价人id
     */
    private Long priceUserId;

    /**
     * 定价资源数
     */
    @NotNull(message = "定价资源数不能为空")
    private Integer priceResourceNum;

    /**
     * 定价说明
     */
    private String remark;

}
