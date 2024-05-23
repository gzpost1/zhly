package cn.cuiot.dmp.app.dto.user;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 获取用户openId参数
 *
 * @author: wuyongchong
 * @date: 2024/5/22 11:38
 */
@Data
public class Code2SessionDto implements Serializable {

    /**
     * 小程序凭证code
     */
    @NotBlank(message = "序凭证code参数不能为空")
    private String code;

}
