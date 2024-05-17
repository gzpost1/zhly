package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

/**
 * @Description 流程修改
 * @Date 2024/4/22 20:18
 * @Created by libo
 */
@Data
public class FlowEngineUpdateDto extends FlowEngineInsertDto{
    /**
     * 流程id
     */
    private Long id;
}
