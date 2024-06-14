package cn.cuiot.dmp.system.infrastructure.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统配置-C端用户审核列表
 *
 * @author caorui
 * @date 2024/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user_house_audit", autoResultMap = true)
public class UserHouseAuditEntity extends BaseEntity {

    private static final long serialVersionUID = 933772055372195340L;

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
    @TableField(typeHandler = JsonTypeHandler.class)
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
