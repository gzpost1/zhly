package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductInfoResp {

    @JsonProperty("deviceConnectionProtocol")
    private String deviceConnectionProtocol;
    @JsonProperty("productKey")
    private String productKey;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("nodeType")
    private String nodeType;
    @JsonProperty("createdOn")
    private Long createdOn;
    private String netType;
    private String authType;
    /**
     * 设备与云端之间的数据通信协议类型： 0：JSON； 1：二进制码流； 2：透传/自定义。
     */
    private String dataFormat;
    private String description;
    private Integer deviceCount;

    /**
     * 产品的开发状态。取值： 0：待发布；  1：已发布。
     */
    private String productstatus;
    private String gatewayProtocol;
    private Boolean definition;
    private String categoryKey;
    private String categoryName;
    private String ecoMode;
    private String ecoPeriod;
}
