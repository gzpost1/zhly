package cn.cuiot.dmp.system.infrastructure.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 修改用户
 * @author zhh
 * @version 1.0
 * @date 2020/9/7 20:09
 */
@Data
public class UpdateUserDTO {

    /**
     * 用户主键ID
     */
    @NotNull(message = "用户主键ID不能为空")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "请输入用户名")
    @Length(max = 30,message = "用户名不可超过30字")
    private String username;

    /**
     * 姓名
     */
    @NotBlank(message = "请输入姓名")
    @Length(max = 30,message = "姓名不可超过30字")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "请输入手机号")
    private String phoneNumber;

    /**
     * 组织id
     */
    @NotBlank(message = "请选择所属组织")
    private String deptId;

    /**
     * 角色id
     */
    @NotBlank(message = "请选择角色")
    private String roleId;

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 企业账户id
     */
    private String orgId;

    /**
     * 登陆人userid
     */
    private String loginUserId;
}
