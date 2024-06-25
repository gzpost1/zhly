package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 租赁管理-定价管理
 *
 * @author caorui
 * @date 2024/6/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "price_manage", autoResultMap = true)
public class PriceManageEntity extends BaseEntity {

    private static final long serialVersionUID = -7019488707505472120L;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 定价单名称
     */
    private String name;

    /**
     * 定价单类别（系统配置自定义）
     */
    private Long categoryId;

    /**
     * 定价单类型（系统配置自定义）
     */
    private Long typeId;

    /**
     * 定价日期，格式为yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date priceDate;

    /**
     * 执行日期，格式为yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date executeDate;

    /**
     * 定价人id
     */
    private Long priceUserId;

    /**
     * 定价资源数
     */
    private Integer priceResourceNum;

    /**
     * 定价说明
     */
    private String remark;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 定价管理明细房屋列表
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> houseIds;

    /**
     * 状态(1:草稿,2:审核中,3:审核通过,4:审核不通过,5:已执行,6:已作废)
     */
    private Byte status;

}
