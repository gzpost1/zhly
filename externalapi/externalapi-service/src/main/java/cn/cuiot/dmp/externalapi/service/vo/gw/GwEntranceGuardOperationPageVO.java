package cn.cuiot.dmp.externalapi.service.vo.gw;

import lombok.Data;

import java.util.Date;

/**
 * 格物门禁-操作记录
 *
 * @Author: zc
 * @Date: 2024-09-11
 */
@Data
public class GwEntranceGuardOperationPageVO {

    /**
     * id
     */
    private Long id;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 执行状态（0：失败，1：成功，2：执行中）
     */
    private Byte executionStatus;

    /**
     * 原因
     */
    private String deviceSecret;

    /**
     * 操作人id
     */
    private Long operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;
}
