package cn.cuiot.dmp.baseconfig.flow.dto.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 我的报修
 * @author pengjian
 * @create 2024/6/12 9:55
 */
@Data
public class RepairReportDto {

    /**
     * 工单id
     */
    private Long procInstId;

    /**
     * 流程名称
     */
    private  String workName;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 工单来源  3 客服代报
     */
    private Byte workSource;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 被待报人id
     */
    private Long actualUserId;
}
