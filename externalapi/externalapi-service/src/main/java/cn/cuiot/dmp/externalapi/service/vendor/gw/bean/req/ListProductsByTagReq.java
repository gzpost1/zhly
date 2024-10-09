package cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-09-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ListProductsByTagReq extends BaseDmpPageReq implements Serializable {
    private List<String> productTagId;
    private List<String> productTagKey;
}
