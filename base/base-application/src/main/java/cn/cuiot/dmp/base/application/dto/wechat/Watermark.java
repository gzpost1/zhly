package cn.cuiot.dmp.base.application.dto.wechat;

import lombok.Data;

/**
 * 数据水印
 */
@Data
public class Watermark {
    /**
     * 小程序appid
     */
    private String appid;
    /**
     * 用户获取手机号操作的时间戳
     */
    private Long timestamp;
}
