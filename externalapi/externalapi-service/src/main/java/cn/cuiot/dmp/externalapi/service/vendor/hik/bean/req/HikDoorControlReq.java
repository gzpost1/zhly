package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 门禁点反控
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikDoorControlReq {

    /**
     * 0-常开 1-门闭 2-门开 3-常闭
     */
    private String controlType;

    /**
     * 资源编码
     */
    private List<String> doorIndexCodes;
}
