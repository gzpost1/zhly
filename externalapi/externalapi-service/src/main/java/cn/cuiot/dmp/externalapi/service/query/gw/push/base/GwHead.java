package cn.cuiot.dmp.externalapi.service.query.gw.push.base;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 格物响应head
 *
 * @Author: zc
 * @Date: 2024-09-13
 */
@Data
@NoArgsConstructor
public class GwHead {
    private String code;
    private String requestId;
    private String resCode;
    private String resMsg;
}
