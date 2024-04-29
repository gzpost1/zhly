package cn.cuiot.dmp.base.infrastructure.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by wuyongchong on 2019/8/29.
 */
@Setter
@Getter
@ToString
public class AuthorizeParam implements Serializable {

    @NotNull(message = "roleId不能为空")
    private Long roleId;

    /**
     * 菜单ID列表
     */
    List<String> resourceIds;

    /**
     * 前端不用管
     */
    private String sessionUserId;
}
