package cn.cuiot.dmp.baseconfig.workorder.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/4/23 16:20
 */
@Data
public class QueryApprovalInfoDto {

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 所属组织
     */
    private String org;

    /**
     * 流程名称
     */
    private String  processName;

    /**
     * 发起人id
     */
    private String  userId;

    /**
     * 发起时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    private Date createDate;

    /**
     * 工单编号
     */
    private  String workCode;
}
