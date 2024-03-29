package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author xieshihai
 * @version 1.0
 * @description: 房屋批量删除 入参
 * @date 2022/9/14 18:06
 */
@Data
public class HouseDeleteDto {

    /**
     * 房屋idList
     */
    @NotEmpty(message = "请至少选择一个房屋")
    private List<Long> ids;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 租户id
     */
    private String orgId;

    /**
     * 标签id
     */
    private Integer labelId;

}
