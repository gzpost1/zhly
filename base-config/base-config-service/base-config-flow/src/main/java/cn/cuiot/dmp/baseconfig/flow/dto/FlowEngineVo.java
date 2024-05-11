package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 流程配置vo
 * @Date 2024/5/9 15:26
 * @Created by libo
 */
@Data
public class FlowEngineVo {
    /**
     * 流程名称
     */
    private String name;

    /**
     * 业务类型ID
     */
    private Long businessTypeId;

    /**
     * 所属组织
     */
    private List<Long> orgId;

    /**
     * 流程定义json
     */
    private String process;

    /**
     * logo
     */
    private String logo;

    /**
     * 流程说明
     */
    private String remark;

    /**
     * 消息通知设置
     */
    private String notifySetting;

    /**
     * 通用配置
     */
    private CommonConfigDto commonConfigDto;
}
