package cn.cuiot.dmp.system.application.param.command;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 岗位新增与编辑参数
 * @author: wuyongchong
 * @date: 2024/5/6 9:43
 */
@Data
public class SysPostCmd implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 岗位名称
     */
    @NotBlank(message = "请输入岗位名称")
    @Length(max = 30, message = "岗位名称不可超过30字")
    private String postName;

    /**
     * 备注
     */
    @Length(max = 200, message = "岗位名称不可超过200字")
    private String remark;

    /**
     * 当前登录用户-账户id(前端不用管)
     */
    private Long sessionOrgId;

}
