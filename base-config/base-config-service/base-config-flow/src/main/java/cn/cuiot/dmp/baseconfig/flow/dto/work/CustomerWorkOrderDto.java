package cn.cuiot.dmp.baseconfig.flow.dto.work;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/6/13 9:24
 */
@Data
public class CustomerWorkOrderDto {

    /**
     * 工单id
     */
    private Long procInstId;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 楼盘id
     */
    private Long propertyId;
    /**
     * 楼盘名称
     */
    private String propertyName;

    /**
     * 工单名称
     */
    private String workName;

    /**
     * 工单来源 0 计划生成  1 自查报事2客户提单3代录工单
     */
    private Byte workSource;

    /**
     * 发起人
     */
    private Long createUser;

    /**
     * 发起人名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 被报单人
     */
    private Long actualUserId;

    /**
     * 报单人名称
     */
    private String actualUserName;

    /**
     * 状态 1已完结2进行中3已终止4已挂起5已撤回
     */
    private Byte status;

    /**
     * 0未超时 1超时
     */
    private Byte timeOut;
}
