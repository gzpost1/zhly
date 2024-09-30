package cn.cuiot.dmp.sms.vendor.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 查询模板状态req
 *
 * @Author: zc
 * @Date: 2024-09-23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SmsSignStateReq {

    /**
     * 需要查询的签名id集合，例：[ 1, 2, 3]
     */
    private List<Integer> SignIds;
}
