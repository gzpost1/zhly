package cn.cuiot.dmp.lease.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/1
 */
@Data
public class ClueDTO implements Serializable {

    private static final long serialVersionUID = -7686590014013548705L;

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
     * 线索来源（系统配置自定义）
     */
    private Long sourceId;

    /**
     * 跟进人ID
     */
    private Long followUserId;

    /**
     * 跟进时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followTime;

    /**
     * 关联客户ID
     */
    private Long customerUserId;

    /**
     * 线索结果（系统配置自定义）
     */
    private Long resultId;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

    /**
     * 线索状态（1待分配，2跟进中，3已完成）
     */
    private Byte status;

}
