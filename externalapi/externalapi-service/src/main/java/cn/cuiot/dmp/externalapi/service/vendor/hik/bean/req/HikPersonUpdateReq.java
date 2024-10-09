package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 添加人员v2
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikPersonUpdateReq {

    /**
     * 人员id
     */
    private String personId;

    /**
     * 人员名称，1~32个字符；不能包含特殊字符
     */
    private String personName;

    /**
     * 所属组织标识，必须是已存在组织
     */
    private String orgIndexCode;

    /**
     * 性别，1：男；2：女；0：未知
     */
    private Number gender;

    /**
     * 出生日期，格式：YYYY-MM-DD
     */
    private String birthday;

    /**
     * 手机号，1-20位数字
     */
    private String phoneNo;

    /**
     * 邮箱，格式：example@domain.com
     */
    private String email;

    /**
     * 证件类型，参考数据字典
     */
    private String certificateType;

    /**
     * 证件号码，1-20位数字字母
     */
    private String certificateNo;

    /**
     * 工号，1-32个字符
     */
    private String jobNo;
}
