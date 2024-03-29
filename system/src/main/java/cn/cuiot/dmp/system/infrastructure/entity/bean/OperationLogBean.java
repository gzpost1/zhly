package cn.cuiot.dmp.system.infrastructure.entity.bean;

import java.sql.Timestamp;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: suyu
 * @Description:
 * @Date: 2020/9/8
 */
@Builder
@Data
public class OperationLogBean {

    private String operationUserId;
    private String action;
    private Integer serviceType;
    private String status;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer pageSize;
    private Integer pageNum;
    private String orgId;
    private String searchType;
    private String searchContent;
    private String operationName;
    private List<Long> userIds;
    private String path;
}
