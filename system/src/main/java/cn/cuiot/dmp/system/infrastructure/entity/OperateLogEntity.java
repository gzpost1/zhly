package cn.cuiot.dmp.system.infrastructure.entity;

import static cn.cuiot.dmp.common.constant.EntityConstants.BLANK_STR;
import static cn.cuiot.dmp.common.constant.EntityConstants.NULL_STR;

import java.util.Objects;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * 操作日志MongoDB实体
 *
 * @Description
 * @Date 2024/5/8 14:11
 * @Created by libo
 */
@Data
public class OperateLogEntity {

    @Id
    private String id;

    /**
     * 操作端 1:web端 2:小程序-管理端 3:小程序-客户端
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

    public String getOperationTargetInfo() {
        if(Objects.isNull(operationTargetInfo)||NULL_STR.equals(operationTargetInfo)){
            return BLANK_STR;
        }
        return operationTargetInfo;
    }

    public String getServiceTypeName() {
        if(Objects.isNull(serviceTypeName)||NULL_STR.equals(serviceTypeName)){
            return BLANK_STR;
        }
        return serviceTypeName;
    }
}
