package cn.cuiot.dmp.largescreen.service.vo;//	模板

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/31 15:19
 */
@Data
public class ContentAuditVO implements Serializable {
    private static final long serialVersionUID = 2518614737020932506L;

    private Long id;

    /**
     * 数据id
     */
    private Long dataId;

    /**
     * 审核状态
     */
    private Byte auditStatus;

    /**
     * 审核人id
     */
    private Long auditUser;

    /**
     * 审核人
     */
    private String auditUserName;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 审核意见
     */
    private String auditOpinion;
}
