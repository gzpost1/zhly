package cn.cuiot.dmp.system.infrastructure.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @Author: wen
 * @Description: 数据库实体类
 * @Date: 2020/9/8
 */
@Data
public class OperationLogEntity implements Serializable {

    private static final long serialVersionUID = 8062176683480646373L;

    private Long id;

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
    private byte[] file;
}
