package cn.cuiot.dmp.system.infrastructure.entity.vo;

import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.utils.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
     * 企业名称
     */
    private String companyName;

    /**
     * 企业编号
     */
    private String orgKey;

    /**
     * 组织id
     */
    private String deptId;

    /**
     * 组织名
     */
    private String deptName;

    /**
     * 组织长名称
     */
    private String deptPathName;

    /**
     * 管理员姓名
     */
    private String adminName;

    /**
     * 管理员用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 启用状态（0：禁用、 1：正常）
     */
    private Integer status;

    /**
     * 企业状态 0-未生效 1-正常 2-已过期
     */
    private Byte orgStatus;

    /**
     * orgTypeId
     **/
    private Integer orgTypeId;

    /**
     * orgTypeName
     **/
    private String orgTypeName;


    /**
     * 企业有效期-开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expStartDate;

    /**
     * 企业有效期-结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expEndDate;

    /**
     * 初始化标志位(0:未初始化,1:已初始化)
     */
    private Byte initFlag;

    /**
     * 计算企业状态
     */
    public Byte getOrgStatus() {
        if (Objects.nonNull(expStartDate)) {
            if (LocalDateTime.now().isBefore(DateTimeUtil.dateToLocalDateTime(expStartDate))) {
                return EntityConstants.NOT_EFFECTIVE;
            }
        }
        if (Objects.nonNull(expEndDate)) {
            if (LocalDateTime.now()
                    .isBefore(DateTimeUtil.dateToLocalDateTime(expEndDate).plusDays(1))) {
                return EntityConstants.NORMAL;
            }
            return EntityConstants.EXPIRE;
        }
        return EntityConstants.NORMAL;
    }
}
