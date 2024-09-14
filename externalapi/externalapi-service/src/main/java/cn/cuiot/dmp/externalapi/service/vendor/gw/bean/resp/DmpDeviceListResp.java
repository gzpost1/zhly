package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@NoArgsConstructor
@Data
public class DmpDeviceListResp extends BaseDmpPageResp {

    private static final long serialVersionUID = 7624692093895374019L;

    @JsonProperty("list")
    private List<ListDTO> list;

    @NoArgsConstructor
    @Data
    public static class ListDTO {
        @JsonProperty("deviceName")
        private String deviceName;
        @JsonProperty("deviceKey")
        private String deviceKey;
        @JsonProperty("nodeType")
        private Integer nodeType;
        @JsonProperty("status")
        private String status;
        @JsonProperty("enabled")
        private Boolean enabled;
        @JsonProperty("lastOnlineOn")
        private Long lastOnlineOn;
        @JsonProperty("productKey")
        private String productKey;
        @JsonProperty("createdOn")
        private Long createdOn;
    }
}
