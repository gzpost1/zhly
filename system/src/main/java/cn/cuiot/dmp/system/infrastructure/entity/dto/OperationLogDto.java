package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: suyu
 * @Description:
 * @Date: 2020/9/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogDto extends BaseLogDTO {

    private String orgId;

    /**
     * 操作者
     */
    private String operationUserId;

    /**
     * 请求方ip
     */
    private String requestIp;

    /**
     * 操作对象
     */
    private String operationTarget;

    /**
     * 操作类型
     */
    private String operationName;

}
