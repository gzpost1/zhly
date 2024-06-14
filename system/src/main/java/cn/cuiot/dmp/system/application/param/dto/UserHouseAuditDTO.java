package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/13
 */
@Data
public class UserHouseAuditDTO implements Serializable {

    private static final long serialVersionUID = -1978478377944925911L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 楼盘ID
     */
    private Long buildingId;

    /**
     * 房屋ID
     */
    private Long houseId;

    /**
     * 身份类型(1:户主,2:租户,3:家属)
     */
    private Byte identityType;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 证件类型（系统配置自定义）
     */
    private Long cardTypeId;

    /**
     * 证件类型名称（系统配置自定义）
     */
    private String cardTypeIdName;

    /**
     * 身份证号
     */
    private String identityNum;

    /**
     * 补充说明-纯文本
     */
    private String remark;

    /**
     * 补充说明-图片（仅限jpg, jpeg, png格式，6张）
     */
    private List<String> remarkImages;

    /**
     * 审核状态(0:待审核,1:审核通过,2:审核驳回)
     */
    private Byte auditStatus;

    /**
     * 驳回理由
     */
    private String rejectReason;

    /**
     * 绑定客户ID
     */
    private Long bindCustomerId;

    /**
     * 绑定客户类型(1:客户本人 2:客户家庭成员)
     */
    private Byte bindCustomerType;

    /**
     * 绑定客户成员ID
     */
    private Long bindCustomerMemberId;

}
