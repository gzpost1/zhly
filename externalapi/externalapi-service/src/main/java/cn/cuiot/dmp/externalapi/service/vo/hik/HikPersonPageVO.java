package cn.cuiot.dmp.externalapi.service.vo.hik;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户信息分页VO
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Data
public class HikPersonPageVO {

    /**
     * 人员编码
     */
    private Long id;

    /**
     * 人员名称，1~32个字符；不能包含特殊字符
     */
    private String personName;

    /**
     * 性别，1：男；2：女；0：未知
     */
    private String gender;

    /**
     * 所属组织标识，必须是已存在组织
     */
    private String orgIndexCode;

    /**
     * 人员所属组织名称
     */
    private String orgName;

    /**
     * 手机号，1-20位数字
     */
    private String phoneNo;

    /**
     * 工号，1-32个字符
     */
    private String jobNo;

    /**
     * 人员照片
     */
    private String faceData;

    /**
     * 人员照片状态（0:未添加，1:已添加）
     */
    private Byte faceDataStatus;

    /**
     * 权限配置状态（0:未配置，1:已配置）
     */
    private Byte authorizeStatus;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
