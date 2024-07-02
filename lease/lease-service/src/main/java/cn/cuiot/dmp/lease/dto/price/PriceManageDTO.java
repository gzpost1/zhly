package cn.cuiot.dmp.lease.dto.price;

import cn.cuiot.dmp.lease.entity.PriceManageDetailEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
public class PriceManageDTO implements Serializable {

    private static final long serialVersionUID = -8048250866595386159L;

    /**
     * 定价单编码
     */
    private Long id;

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
     * 定价单类别名称（系统配置自定义）
     */
    private String categoryName;

    /**
     * 定价单类型（系统配置自定义）
     */
    private Long typeId;

    /**
     * 定价单类型名称（系统配置自定义）
     */
    private String typeName;

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
     * 定价人名称
     */
    private String priceUserName;

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
     * 作废备注
     */
    private String invalidRemark;

    /**
     * 定价管理明细房屋列表
     */
    private List<String> houseIds;

    /**
     * 状态(1:草稿,2:审核中,3:审核通过,4:审核不通过,5:已执行,6:已作废)
     */
    private Byte status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。
     */
    private String createdBy;

    /**
     * 创建者名称
     */
    private String createdName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;

    /**
     * 更新者。取值：{userKey}：Portal用户更新的；{appKey}：API更新的。
     */
    private String updatedBy;

    /**
     * 更新者名称
     */
    private String updatedName;

    /**
     * 定价管理明细列表
     */
    private List<PriceManageDetailEntity> priceManageDetailEntities;

}
