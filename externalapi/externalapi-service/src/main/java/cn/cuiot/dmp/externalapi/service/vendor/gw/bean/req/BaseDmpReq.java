package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseDmpReq<T> implements Serializable {
    private String app_id;
    private String timestamp;
    private String trans_id;
    private String requestId;
    private String token;
    private T data;
}
