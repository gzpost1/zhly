package cn.cuiot.dmp.baseconsumer.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Description 操作日志MongoDB实体
 * @Date 2024/5/8 14:11
 * @Created by libo
 */
@Data
public class OperateLogEntity {

    @Id
    private String id;

    /**
     * 操作端
     */
    private String operationSource;

    /**
     * 账户ID
     */
    private String orgId;

    /**
     * 请求时间
     */
    private String requestTime;

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
     * 用户类型
     */
    private Integer userType;


    /**
     * 操作对象信息
     */
    private String operationTargetInfo;

    /**
     * 业务类型
     */
    private String serviceType;

    /**
     * 业务类型名称
     */
    private String serviceTypeName;

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
}
