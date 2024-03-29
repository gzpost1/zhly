package cn.cuiot.dmp.system.infrastructure.entity.vo;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author cwl
 * @classname ComplaintDetailsDto
 * @description 账户列表 出参
 * @date 2021/12/28
 */
@Data
public class ListOrganizationVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 账户名称
     */
    private String orgName;

    /**
     * 管理员id
     */
    private String orgOwner;

    /**
     * 管理员用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 组织id
     */
    private String deptId;

    /**
     * 组织名
     */
    private String deptName;

    /**
     * 注册渠道
     */
    private String createdByChannel;

    /**
     * 创建时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建者类型
     */
    private Integer createdByType;

    /**
     * 创建者名称
     */
    private String createdName;

    /**
     * 账户状态（0：禁用、 1：正常）
     */
    private Integer status;

    /**
     *  orgTypeId
     **/
    private Integer orgTypeId;

    /**
     *  orgTypeName
     **/
    private String orgTypeName;

    /**
     * 统一社会信用代码
     */
    private String socialCreditCode;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 来源
     */
    private Integer source;

}
