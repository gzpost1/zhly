package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 流程保存
 * @Date 2024/4/22 20:16
 * @Created by libo
 */
@Data
public class FlowEngineInsertDto {

    /**
     * 流程名称
     */
    @NotBlank(message = "流程名称不能为空")
    private String name;

    /**
     * 业务类型ID
     */
    @NotNull(message = "业务类型ID不能为空")
    private Long businessTypeId;

    /**
     * 所属组织
     */
    @NotEmpty(message = "所属组织不能为空")
    private List<Long> orgId;

    /**
     * 流程定义json
     */
    @NotBlank(message = "流程定义json不能为空")
    private String process;

    /**
     * logo
     */
    @NotNull(message = "logo不能为空")
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
    @Valid
    @NotNull(message = "通用配置不能为空")
    private CommonConfigDto commonConfigDto;

    /**
     * 是否允许客户端发起工单 0否 1是
     */
    private Byte isSelectAppUser;
}
