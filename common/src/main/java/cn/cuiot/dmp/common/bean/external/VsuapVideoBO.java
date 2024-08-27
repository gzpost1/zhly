package cn.cuiot.dmp.common.bean.external;

import lombok.Data;

/**
 * 监控（云智眼)-对接系统参数
 *
 * @Author: zc
 * @Date: 2024-08-21
 */
@Data
public class VsuapVideoBO {

    /**
     * AccessKeyID
     */
    private String accessKeyId;

    /**
     * AccessKeySecret
     */
    private String accessKeySecret;

    /**
     * 视频播放AK
     */
    private String ak;

    /**
     * 视频播放SK
     */
    private String sk;
}
