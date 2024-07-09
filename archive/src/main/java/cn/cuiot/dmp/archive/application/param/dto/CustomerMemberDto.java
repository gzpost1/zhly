package cn.cuiot.dmp.archive.application.param.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author: wuyongchong
 * @date: 2024/6/12 15:17
 */
@Data
public class CustomerMemberDto implements Serializable {
    /**
     * 主键ID
     */
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
    @NotBlank(message = "请完善成员信息")
    private String relationshipType;


    /**
     * 姓名
     */
    @NotBlank(message = "请完善成员信息")
    @Length(max = 30, message = "姓名限100字")
    private String memberName;


    /**
     * 手机号
     */
    @NotBlank(message = "请输入联系人手机号")
    @Length(max = 11, message = "手机号限11位")
    private String memberPhone;


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
     * 邮箱
     */
    private String email;


    /**
     * 备注
     */
    private String remark;
}
