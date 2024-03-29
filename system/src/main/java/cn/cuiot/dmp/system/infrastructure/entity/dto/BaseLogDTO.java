package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lifei
 * @Description:
 * @Date: 2020/9/8
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BaseLogDTO {

    /**
     * 请求时间
     */
    private String requestTime;

    /**
     * 操作编码
     */
    private String operationCode;

    /**
     * 状态码
     */
    private String statusCode;
}
