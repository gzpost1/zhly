package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

/**
 * 添加人员v2
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikPersonAddResp {

    /**
     * 成功添加的人员Id
     */
    private String personId;

    /**
     * 成功添加的人脸Id
     */
    private String faceId;
}
