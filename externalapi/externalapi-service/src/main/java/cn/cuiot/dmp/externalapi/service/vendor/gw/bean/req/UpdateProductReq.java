package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

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
public class UpdateProductReq implements Serializable {

    private String productKey;
    private String productName;
    private String description;
}
