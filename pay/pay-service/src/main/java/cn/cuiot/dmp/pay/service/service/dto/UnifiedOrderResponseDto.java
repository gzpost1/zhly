package cn.cuiot.dmp.pay.service.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author huq
 * @ClassName UnifiedOrderResponseDto
 * @Date 2022/6/29 15:20
 **/
@Data
public class UnifiedOrderResponseDto {
    String appId;
    String timeStamp;
    String nonceStr;

    @JsonProperty("package")
    String pkg;

    String signType;

    String paySign;
}
