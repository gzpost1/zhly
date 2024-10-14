package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "工单编号", orderNum = "0", width = 20)
    private Long procInstId;

    /**
     * 业务类型名称
     */
    @Excel(name = "业务类型", orderNum = "1", width = 20)
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
    @Excel(name = "楼盘", orderNum = "2", width = 20)
    private String propertyName;

    /**
     * 工单名称
     */
    @Excel(name = "工单流程", orderNum = "3", width = 20)
    private String workName;

    /**
     * 工单来源 0 计划生成  1 自查报事2客户提单3代录工单
     */
    private Byte workSource;

    @Excel(name = "工单来源", orderNum = "4", width = 20)
    private String workSourceDesc;

    /**
     * 发起人
     */
    private Long createUser;

    /**
     * 发起人名称
     */
    @Excel(name = "创建人", orderNum = "5", width = 20)
    private String createUserName;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发起时间",orderNum = "7",  width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 被报单人
     */
    private Long actualUserId;

    /**
     * 报单人名称
     */
    @Excel(name = "报单人", orderNum = "6", width = 20)
    private String actualUserName;

    /**
     * 状态 1已完结2进行中3已终止4已挂起5已撤回
     */
    private Byte status;

    @Excel(name = "工单状态", orderNum = "8", width = 20)
    private String statusName;

    /**
     * 0未超时 1超时
     */
    private Byte timeOut;
}
