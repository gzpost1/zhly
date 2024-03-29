package cn.cuiot.dmp.common.log.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author guoying
 * @className PlatformOperateLogDTO
 * @description 平台操作日志DTO
 * @date 2020-09-07 17:14:59
 */
@Data
public class OperateLogDto implements Serializable {

    private static final long serialVersionUID = 8062176683480646372L;

    String operationUserId;

    String serviceTypeName;

    /**
     * 账户ID
     */
    private String orgId;

    /**
     * 请求时间
     */
    private String requestTime;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 操作编码
     */
    private String operationCode;

    /**
     * 操作名称
     */
    private String operationName;

    /**
     * 操作者ID
     */
    private String operationById;

    /**
     * 操作者名称
     */
    private String operationByName;

    /**
     * 操作对象
     */
    private String operationTarget;

    /**
     * 操作对象信息
     */
    private String operationTargetInfo;

    /**
     * 业务类型
     */
    private String serviceType;

    /**
     * 日志级别
     */
    private String logLevel;

    /**
     * 状态码
     */
    private String statusCode;

    /**
     * 状态描述
     */
    private String statusMsg;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应参数
     */
    private String responseParams;

    /**
     * 文件
     */
    byte[] file;
}
