package cn.cuiot.dmp.system.infrastructure.entity.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 导出用户
 *
 * @author zhh
 * @version 1.0
 * @date 2020/9/7 20:09
 */
@Data
public class ExportUserCmd {

    /**
     * 组织id
     */
    @NotBlank(message = "请选择所属组织")
    private String deptId;

    /**
     * 登陆人 orgId
     */
    private String loginOrgId;

    /**
     * 登陆人userid
     */
    private String loginUserId;
}
