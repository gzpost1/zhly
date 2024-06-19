package cn.cuiot.dmp.system.infrastructure.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * C端用户
 *
 * @author: wuyongchong
 * @date: 2024/6/14 9:25
 */
@Data
public class ClientUserVo implements Serializable {

    /**
     * pk
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 用户类型（1：实体用户）
     */
    private Integer userType;

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 小程序openid
     */
    private String openid;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
}
