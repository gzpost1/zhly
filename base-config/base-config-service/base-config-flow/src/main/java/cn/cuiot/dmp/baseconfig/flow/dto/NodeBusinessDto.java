package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/5/7 14:03
 */
@Data
public class NodeBusinessDto {
    /**
     * 操作人员
     */
    private Long  userId;

    /**
     * 操作时间
     */
    private Date  startTime;

    /**
     * 操作类型0挂起1超时2评论3督办4终止5转办6完成
     */
    private Byte businessType;
    /**
     * 原因
     */
    private String comments;

    /**
     * 补充说明
     */
    private String reason;

    /**
     * 表单id
     */
    private String formId;
}
