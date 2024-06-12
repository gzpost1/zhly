package cn.cuiot.dmp.archive.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 客户房屋信息
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_customer_house",autoResultMap = true)
public class CustomerHouseEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;


    /**
     * 客户ID
     */
    private Long customerId;


    /**
     * 房屋ID
     */
    private Long houseId;


    /**
     * 身份
     */
    private String identityType;


    /**
     * 迁入日期
     */
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
     * 状态(0:禁用,1:正常)
     */
    private Byte status;


    /**
     * 备注
     */
    private String remark;

}
