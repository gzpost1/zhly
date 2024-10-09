package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

/**
 * 修改人脸
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikFaceSingleUpdateResp {

    /**
     * 人脸Id
     */
    private String faceId;
    /**
     * 人脸图片Url
     */
    private String faceUrl;
    /**
     * 人员ID
     */
    private String personId;
}
