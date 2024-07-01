package cn.cuiot.dmp.lease.vo;

import lombok.Data;

import java.util.Date;

/**
 * 收费管理-催款记录分页Vo
 *
 * @author zc
 */
@Data
public class ChargeCollectionRecordVo {
    /**
     * 催款id
     */
    private Long id;

    /**
     * 通知渠道（1:短信，2:微信）
     */
    private Byte channel;

    /**
     * 催款时间
     */
    private Date date;

    /**
     * 催缴类型（1:手动催缴，2:计划催缴）
     */
    private Byte type;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 操作人
     */
    private String operatorName;
}