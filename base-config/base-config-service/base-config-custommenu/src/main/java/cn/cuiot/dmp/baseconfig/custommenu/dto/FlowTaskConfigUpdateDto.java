package cn.cuiot.dmp.baseconfig.custommenu.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 系统配置-任务配置
 */
@Data
public class FlowTaskConfigUpdateDto {
    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    private Long id;

    /**
     * 任务配置名称
     */
    @NotBlank(message = "任务配置名称不能为空")
    private String name;

    /**
     * 业务分类
     */
    @NotNull(message = "业务分类不能为空")
    private Long businessTypeId;

    /**
     * 所属组织
     */
    @NotNull(message = "所属组织不能为空")
    private Long orgId;

    /**
     * 任务描述
     */
    private String remark;

    /**
     * 任务对象信息
     */
    @Valid
    @NotEmpty(message = "任务对象信息不能为空")
    @Size(max = 100, message = "任务对象信息最多100")
    private List<FlowTaskInfoUpdateDto> taskInfoList;
}