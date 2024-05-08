package cn.cuiot.dmp.system.infrastructure.entity.bo;

import cn.cuiot.dmp.common.log.intf.AbstractResourceParam;
import cn.cuiot.dmp.system.infrastructure.entity.dto.ResetPasswordReqDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhh
 * @description 用户日志操作对象实体
 * @author: YouName
 * @create: 2020-10-28 14:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBo extends AbstractResourceParam {

    /**
     * 用户主键id
     */
    private Long id;

    /**
     * 重置密码实体
     */
    private ResetPasswordReqDTO resetPasswordReqDTO;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 更改后的密码
     */
    private String password;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * id集合
     */
    private List<Long> ids;

    /**
     * 账户id
     */
    private String orgId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 下次登陆是否重置密码
     */
    private Integer resetPassword;

    /**
     * 角色key
     */
    private String roleKey;

    /**
     * 创建者id
     */
    private String createdBy;

    /**
     * 组织id
     */
    private String deptId;

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系人地址
     */
    private String contactAddress;

    /**
     * 长时间登录
     */
    private Integer longTimeLogin;

    /**
     * 登陆用户的userId
     */
    private String loginUserId;

}
