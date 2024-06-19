package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ClueRecordEntity extends BaseEntity {

    private static final long serialVersionUID = 6790695408753991678L;

    /**
     * 线索ID
     */
    private Long clueId;

    /**
     * 跟进人ID
     */
    private Long followerId;

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
     * 线索表单配置数据
     */
    private String formData;

    /**
     * 当前线索表单配置快照
     */
    private String formConfigDetail;

}
