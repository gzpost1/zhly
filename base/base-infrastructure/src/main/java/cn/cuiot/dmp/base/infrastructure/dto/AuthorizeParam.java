package cn.cuiot.dmp.base.infrastructure.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by wuyongchong on 2019/8/29.
 */
@Data
public class AuthorizeParam implements Serializable {

    /**
     * 类型ID
     */
    @NotNull(message = "类型ID不能为空")
    private Long orgTypeId;

    /**
     * 菜单ID列表
     */
    private List<String> resourceIds;

    /**
     * 前端不用管
     */
    private String sessionUserId;
}
