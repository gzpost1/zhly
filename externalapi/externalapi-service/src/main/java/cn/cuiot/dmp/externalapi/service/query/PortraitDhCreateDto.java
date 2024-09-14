package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pengjian
 * @create 2024/7/18 19:13
 */
@Data
public class PortraitDhCreateDto implements Serializable {

    /**
     * 图片地址
     */
    private String url;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 备注
     */
    private String tag;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 密码
     */
    private String password;
}
