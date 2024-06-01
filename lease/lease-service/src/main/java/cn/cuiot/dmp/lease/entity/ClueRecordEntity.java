package cn.cuiot.dmp.lease.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 租赁管理-线索管理记录表
 *
 * @author caorui
 * @date 2024/6/1
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "clue_record", autoResultMap = true)
public class ClueRecordEntity implements Serializable {

    private static final long serialVersionUID = 6790695408753991678L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 线索ID
     */
    private Long clueId;

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
     * 跟进状态（系统配置自定义）
     */
    private Long followStatusId;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

    /**
     * 逻辑删除，1已删除，0未删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private Integer deletedFlag;

}
