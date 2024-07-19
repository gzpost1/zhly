package cn.cuiot.dmp.system.infrastructure.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author pengjian
 * @create 2024/7/18 20:37
 */
@Data
public class PortraitInputVo {

    /**
     * 名字
     */
    private String name;
    /**
     * url
     */

    private String url;

    /**
     * 手机号
     */

    private String phone;

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

    /**
     * 备注
     */

    private String tag;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;
}
