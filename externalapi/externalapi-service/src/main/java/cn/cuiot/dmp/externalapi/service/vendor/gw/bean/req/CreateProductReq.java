package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class CreateProductReq implements Serializable {

    @JsonProperty("netType")
    private Integer netType;
    @JsonProperty("authType")
    private Integer authType;
    @JsonProperty("dataFormat")
    private Integer dataFormat;
    @JsonProperty("nodeType")
    private Integer nodeType;
    @JsonProperty("deviceConnectionProtocol")
    private Integer deviceConnectionProtocol;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("categoryKey")
    private String categoryKey;
    @JsonProperty("description")
    private String description;
}
