package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 *
 * @author pengjian
 * @since 2024-07-18
 */
@Data
public class PortraitInputCreateDto implements Serializable {
    /**
     * 名字
     */
    private String name;
    /**
    * url
    */
    @Length(max = 200,message = "url长度必须小于200位")
    private String url;

    /**
    * 手机号
    */
    @Length(max = 30,message = "手机号长度必须小于30位")
    private String phone;

    /**
    * 卡号
    */
    @Length(max = 50,message = "卡号长度必须小于50位")
    private String cardNo;

    /**
    * 身份证号
    */
    @Length(max = 50,message = "身份证号长度必须小于50位")
    private String idCardNo;

    /**
    * 密码
    */
    @Length(max = 20,message = "密码长度必须小于20位")
    private String password;

    /**
    * 备注
    */
    @Length(max = 255,message = "备注长度必须小于255位")
    private String tag;

    /**
     * 临时密钥id
     */
    private String kid;

}
