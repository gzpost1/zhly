package cn.cuiot.dmp.lease.dto.clue;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/1
 */
@Data
public class ClueDTO implements Serializable {

    private static final long serialVersionUID = -7686590014013548705L;

    /**
     * 线索ID
     */
    private Long id;

    /**
     * 线索名称
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 楼盘ID
     */
    private Long buildingId;

    /**
     * 楼盘名称
     */
    private String buildingName;

    /**
     * 楼盘所属部门ID
     */
    private Long buildingDepartmentId;

    /**
     * 线索来源（系统配置自定义）
     */
    private Long sourceId;

    /**
     * 线索来源名称
     */
    private String sourceIdName;

    /**
     * 关联客户ID
     */
    private Long customerUserId;

    /**
     * 完成人ID
     */
    private Long finishUserId;

    /**
     * 完成人名称
     */
    private String finishUserName;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /**
     * 线索结果（系统配置自定义）
     */
    private Long resultId;

    /**
     * 线索结果名称
     */
    private String resultIdName;

    /**
     * 线索表单配置数据
     */
    private JSONObject formData;

    /**
     * 当前线索表单配置快照
     */
    private String formConfigDetail;

    /**
     * 线索备注
     */
    private String remark;

    /**
     * 线索状态（1待分配，2跟进中，3已完成）
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
     * 当前跟进人ID
     */
    private Long currentFollowerId;

    /**
     * 当前跟进人名称
     */
    private String currentFollowerName;

    /**
     * 跟进状态（系统配置自定义）
     */
    private Long currentFollowStatusId;

    /**
     * 跟进状态名称
     */
    private String currentFollowStatusIdName;

    /**
     * 跟进时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date currentFollowTime;

}
