package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: zc
 * @Date: 2024-06-28
 */
@Data
public class CommonOptionSettingReqDTO {

    /**
     * 常用选项设置ID列表
     */
    @NotEmpty(message = "常用选项设置ID列表")
    private List<Long> idList;
}