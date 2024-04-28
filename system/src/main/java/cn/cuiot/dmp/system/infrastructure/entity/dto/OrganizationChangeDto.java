package cn.cuiot.dmp.system.infrastructure.entity.dto;


import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/4/28 17:31
 */
@Data
public class OrganizationChangeDto implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 企业主键ID
     */
    private Long pkOrgId;

    /**
     * 变更类型
     */
    private String changeType;

    /**
     * 变更名称
     */
    private String changeName;

    /**
     * 变更时间
     */
    private Date changeDate;

    /**
     * 操作人用户ID
     */
    private String changeUserId;

    /**
     * 操作人用户名
     */
    private String changeUsername;

    /**
     * 操作人姓名
     */
    private String changePerson;

    /**
     * 变更内容
     */
    private String changeData;
}
