package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 租赁管理-线索管理表
 *
 * @author caorui
 * @date 2024/6/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "clue", autoResultMap = true)
public class ClueEntity extends BaseEntity {

    private static final long serialVersionUID = 4918676414161147103L;

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
     * 当前跟进人ID
     */
    private Long currentFollowerId;

    /**
     * 关联客户ID
     */
    private Long customerUserId;

    /**
     * 完成人ID
     */
    private Long finishUserId;

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
     * 表单配置详情
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

}
