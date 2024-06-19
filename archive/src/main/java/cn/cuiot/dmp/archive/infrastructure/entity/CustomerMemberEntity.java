package cn.cuiot.dmp.archive.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.dto.YjBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户成员信息
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_customer_member",autoResultMap = true)
public class CustomerMemberEntity extends YjBaseEntity {

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
     * 关联客户ID
     */
    private Long relateCustomerId;


    /**
     * 关系
     */
    private String relationshipType;


    /**
     * 姓名
     */
    private String memberName;


    /**
     * 手机号
     */
    private String memberPhone;


    /**
     * 证件类型
     */
    private String certificateType;


    /**
     * 证件号码
     */
    private String certificateCdoe;


    /**
     * 邮箱
     */
    private String email;


    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;


    /**
     * 备注
     */
    private String remark;


}
