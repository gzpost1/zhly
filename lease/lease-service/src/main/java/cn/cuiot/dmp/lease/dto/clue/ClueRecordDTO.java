package cn.cuiot.dmp.lease.dto.clue;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/1
 */
@Data
public class ClueRecordDTO implements Serializable {

    private static final long serialVersionUID = -3967288747228333494L;

    /**
     * 主键ID
     */
    private Long id;

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
     * 跟进状态名称
     */
    private String followStatusIdName;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

}
