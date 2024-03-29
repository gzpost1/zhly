package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author wen
 * @Description 修改密码入参
 * @Date 2021/12/27 9:31
 * @param
 * @return
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String password;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    /**
     * 新密码2
     */
    @NotBlank(message = "新密码2不能为空")
    private String newPasswordAgain;

}
