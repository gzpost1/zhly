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
     * 跟进人名称
     */
    private String followerName;

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
     * 线索表单配置数据
     */
    private JSONObject formData;

    /**
     * 当前线索表单配置快照
     */
    private String formConfigDetail;

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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;

    /**
     * 创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。
     */
    private String updatedBy;

    /**
     * 创建者名称
     */
    private String updatedName;

}
