package cn.cuiot.dmp.baseconfig.flow.dto;

import cn.cuiot.dmp.query.PageQuery;
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
public class QueryApprovalInfoDto extends PageQuery {

    /**
     * 工单编号
     */
    private  String workId;

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
     * 工单状态
     */
    private Byte state;

    /**
     * 工单来源
     */
    private Byte workSource;

    /**
     * 是否超时
     */
    private Byte timeOut;

}
