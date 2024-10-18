package cn.cuiot.dmp.system.infrastructure.entity;

import static cn.cuiot.dmp.common.constant.EntityConstants.BLANK_STR;
import static cn.cuiot.dmp.common.constant.EntityConstants.NULL_STR;

import java.util.Objects;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "操作端", orderNum = "0", width = 20,replace = {"web端_1", "管理端_2", "客户端_3"})
    private String operationSource;

    /**
     * 账户ID
     */
    private String orgId;

    /**
     * 请求时间
     */
    @Excel(name = "操作时间", orderNum = "6", width = 20)
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
    @Excel(name = "操作内容", orderNum = "2", width = 20)
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
     * 操作用户
     */
    @Excel(name = "操作用户", orderNum = "5", width = 20)
    private String operation;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户类型
     */
    @Excel(name = "操作用户类型", orderNum = "4", width = 20,replace = {"管理人员_1", "C端用户_2"})
    private String userTypeName;


    /**
     * 操作对象信息
     */
    @Excel(name = "操作对象", orderNum = "3", width = 20)
    private String operationTargetInfo;

    /**
     * 业务类型
     */
    private String serviceType;

    /**
     * 业务类型名称
     */
    @Excel(name = "操作类型", orderNum = "1", width = 20)
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
