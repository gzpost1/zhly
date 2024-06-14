package cn.cuiot.dmp.lease.dto.clue;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author caorui
 * @date 2024/6/2
 */
@Data
public class ClueFollowDTO implements Serializable {

    private static final long serialVersionUID = -561752869274727868L;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 线索跟进id
     */
    private Long id;

    /**
     * 线索id
     */
    @NotNull(message = "线索id不能为空")
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
    @NotNull(message = "跟进状态不能为空")
    private Long followStatusId;

    /**
     * 线索表单配置数据
     */
    private JSONObject formData;

    /**
     * 当前线索表单配置快照
     */
    private String formConfigDetail;

}
