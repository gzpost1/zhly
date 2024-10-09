package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 获取门禁事件的图片
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikAcsEventPicturesReq {

    /**
     * 提供picUri处会提供此字段
     */
    private String svrIndexCode;

    /**
     * 图片相对地址
     */
    private String picUri;
}
