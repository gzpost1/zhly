package cn.cuiot.dmp.base.application.dto.wechat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * access_token返回参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResp {

   private String access_token;

   private Integer expires_in;
}
