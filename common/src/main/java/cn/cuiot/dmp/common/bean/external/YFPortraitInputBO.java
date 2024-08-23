package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 大华宇泛-人脸录像-对接系统参数
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@Data
public class YFPortraitInputBO {

    /**
     * AppKey
     */
    private String appkey;

    /**
     * AppSecret
     */
    private String appSecret;

    /**
     * projectGuid
     */
    private String projectGuid;
}
