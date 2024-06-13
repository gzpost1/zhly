package cn.cuiot.dmp.archive.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户信息
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_customer", autoResultMap = true)
public class CustomerEntity extends YjBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 客户名称
     */
    private String customerName;


    /**
     * 客户类型
     */
    private String customerType;


    /**
     * 客户分类
     */
    private String customerCate;


    /**
     * 客户级别
     */
    private String customerLevel;


    /**
     * 联系人
     */
    private String contactName;


    /**
     * 联系人手机号
     */
    private String contactPhone;


    /**
     * 公司性质
     */
    private String companyNature;


    /**
     * 所属行业
     */
    private String companyIndustry;


    /**
     * 信用等级
     */
    private String creditLevel;


    /**
     * 客户星级
     */
    private Double customerStar;


    /**
     * 邮箱
     */
    private String email;


    /**
     * 性别
     */
    private String sex;


    /**
     * 证件类型
     */
    private String certificateType;


    /**
     * 证件号码
     */
    private String certificateCdoe;


    /**
     * 地址
     */
    private String address;


    /**
     * 附件
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> attachments;


    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;


    /**
     * 备注
     */
    private String remark;

}
